package mx.qsistemas.payments_transfer.pboc

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.basewin.aidl.OnPBOCListener
import com.basewin.aidl.OnPinInputListener
import com.basewin.define.*
import com.basewin.services.ServiceManager
import com.basewin.utils.BCDASCII
import com.bw.jni.entity.KernelType
import com.bw.jni.message.KernelDataRecord
import com.bw.jni.message.OutcomeParameterSet
import com.google.gson.Gson
import mx.qsistemas.payments_transfer.*
import mx.qsistemas.payments_transfer.PaymentsTransfer.modeTx
import mx.qsistemas.payments_transfer.dialogs.PinInputDialog
import mx.qsistemas.payments_transfer.dtos.*
import mx.qsistemas.payments_transfer.net.API_Banorte
import mx.qsistemas.payments_transfer.net.API_Quetzalcoatl
import mx.qsistemas.payments_transfer.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection

class PbocListener(val amount: String, val activity: Activity, val txListener: IPaymentsTransfer.TransactionListener) : OnPBOCListener,
        OnPinInputListener {

    private var chipExpDate: String
    private var chipMaskedPan: String
    private var chipTrack: String
    private var chipCardNumber: String
    private var chipCardOwner: String
    private var chipIcData: String
    private var chipAid: String
    private var chipArqc: String
    private var chipTvr: String
    private var chipTsi: String
    private var chipApn: String
    private var inputCardType = 0
    private var pinpad_model: Handler
    private var needSignature = false
    private var isOnlinePin = true

    init {
        chipExpDate = ""
        chipMaskedPan = ""
        chipTrack = ""
        chipCardNumber = ""
        chipCardOwner = ""
        chipIcData = ""
        chipAid = ""
        chipArqc = ""
        chipTvr = ""
        chipTsi = ""
        chipApn = ""
        pinpad_model = @SuppressLint("HandlerLeak") object : Handler() {
            var pinDialog: PinInputDialog? = null
            var keylayout = ByteArray(96)

            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    PIN_DIALOG_SHOW -> {
                        DialogStatusHelper.closeDialog()
                        try {
                            ServiceManager.getInstence().pinpad.setPinpadMode(GlobalDef.MODE_FIXED)
                        } catch (e1: Exception) {
                            e1.printStackTrace()
                        }
                        pinDialog = PinInputDialog(activity, chipCardNumber, activity.getString(R.string.pt_t_pin_input), amount,
                                object : Interfaces.OnPinDialogListener {
                                    override fun OnPinInput(result: Int) {
                                    }

                                    override fun OnCreateOver() {
                                        sendEmptyMessage(GETLAYOUT)
                                    }
                                })
                    }
                    PIN_DIALOG_DISMISS -> {
                        DialogStatusHelper.closeDialog()
                        if (pinDialog != null) {
                            pinDialog?.dismiss()
                            pinDialog = null
                        }
                    }
                    PIN_SHOW -> {
                        if (pinDialog != null) {
                            val bundle = msg.data
                            pinDialog?.setPins(bundle.getInt("len"), bundle.getInt("key"))
                        }
                    }
                    SETLAYOUT -> {
                        pinDialog?.setKeyShow(msg.obj as ByteArray, object : OnGetLayoutSucListener {
                            override fun onSuc() {
                                keylayout = pinDialog?.keyLayout ?: byteArrayOf()
                                try {
                                    ServiceManager.getInstence().pinpad.setPinpadLayout(keylayout)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                    }
                    GETLAYOUT ->
                        try {
                            ServiceManager.getInstence().pinpad.setOnPinInputListener(this@PbocListener)
                            if (isOnlinePin) {
                                when (GlobalData.getInstance().pinpadVersion) {
                                    PINPAD_INTERFACE_VERSION1 -> ServiceManager.getInstence().pinpad.inputOnlinePin(chipCardNumber,
                                            byteArrayOf(0, 4, 5, 6, 7, 8, 9, 10, 11, 12))
                                    PINPAD_INTERFACE_VERSION2 -> ServiceManager.getInstence().pinpad.inputOnlinePinNew(
                                            GlobalData.getInstance().tmkId, chipCardNumber,
                                            byteArrayOf(0, 4, 5, 6, 7, 8, 9, 10, 11, 12))
                                    PINPAD_INTERFACE_VERSION3 -> ServiceManager.getInstence().pinpad.inputOnlinePinByArea(
                                            GlobalData.getInstance().area, GlobalData.getInstance().tmkId, chipCardNumber,
                                            byteArrayOf(0, 4, 5, 6, 7, 8, 9, 10, 11, 12))
                                    PINPAD_INTERFACE_DUKPT -> ServiceManager.getInstence().pinpad.inputOnlinePinDukpt(1, 0, 60, chipCardNumber,
                                            byteArrayOf(0, 4, 5, 6, 7, 8, 9, 10, 11, 12))
                                }
                            } else {
                                ServiceManager.getInstence().pinpad.inputOfflinePin(byteArrayOf(0, 4, 5, 6, 7, 8, 9, 10, 11, 12), 60)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                }
            }
        }
    }

    override fun onFindingCard(cardType: Int, data: Intent?) {
        inputCardType = cardType
        when (cardType) {
            CardType.MAG_CARD -> {
                val magCardInfo = OutputMagCardInfo(data)
                val cardBeneficiary = magCardInfo.getTrack(1).split('^')[1]
                var cipherTrack1 = ""
                var cipherTrack2 = ""
                /* If the terminal has a combined IC and magnetic stripe reader, when the magnetic stripe of the card is read and the service code begins with a '2' or a '6'
                indicating that an IC is present, the terminal shall process the transaction using the IC */
                if (magCardInfo.serviceCode == null) {
                    DialogStatusHelper.closeDialog()
                    txListener.onTxFailed(activity.getString(R.string.pt_e_card_not_found))
                } else if (magCardInfo.serviceCode.startsWith("2") || magCardInfo.serviceCode.startsWith("6")) {
                    DialogStatusHelper.closeDialog()
                    txListener.onTxFailed(activity.getString(R.string.pt_e_card_input_incorrect))
                } else {
                    needSignature = true
                    /* Step 1: Cipher Track 1 */
                    cipherData(magCardInfo.getTrack(1), object : Interfaces.CipherDataListener {
                        override fun onCipherData(success: Boolean, value: String) {
                            if (success) {
                                cipherTrack1 = value
                                /* Step 2: Cipher Track 2 */
                                cipherData(magCardInfo.getTrack(2), object : Interfaces.CipherDataListener {
                                    override fun onCipherData(success: Boolean, value: String) {
                                        if (success) {
                                            cipherTrack2 = value
                                            processTx(ENTRY_MODE_BAND, cipherTrack1, cipherTrack2,
                                                    "", magCardInfo.maskedPAN, magCardInfo.expiredDate, cardBeneficiary, needSignature)
                                        } else {
                                            DialogStatusHelper.closeDialog()
                                            txListener.onTxFailed(value)
                                        }
                                    }
                                })
                            } else {
                                DialogStatusHelper.closeDialog()
                                txListener.onTxFailed(value)
                            }
                        }
                    })
                }
            }
            CardType.IC_CARD -> {
            }
            CardType.RF_CARD -> {
            }
            else -> {
                DialogStatusHelper.closeDialog()
                txListener.onTxFailed(activity.getString(R.string.pt_e_card_not_found))
            }
        }
    }

    override fun onFindSelectAid(aid: String?) {
        if (BuildConfig.DEBUG) {
            Log.i(this.javaClass.simpleName, "AID: $aid")
        }
    }

    override fun onStartPBOC() {
        activity.runOnUiThread { DialogStatusHelper.showDialog(activity, activity.getString(R.string.pt_t_reading_card)) }
    }

    override fun onConfirmCardInfo(info: Intent?) {
        activity.runOnUiThread { DialogStatusHelper.showDialog(activity, activity.getString(R.string.pt_t_confirming_card)) }
        val out = OutputCardInfoData(info)
        chipExpDate = out.expiredDate
        chipMaskedPan = out.maskedPAN
        chipTrack = out.track
        chipCardNumber = out.pan
        Looper.prepare()
        try {
            ServiceManager.getInstence().pboc.confirmCardInfo()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        Looper.loop()
    }

    override fun onRequestInputPIN(p0: Boolean, p1: Int) {
        isOnlinePin = p0
        DialogStatusHelper.closeDialog()
        pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW)
    }

    override fun onAARequestOnlineProcess(data: Intent?) {
        val out = OutputPBOCAAData(data)
        cipherData(chipTrack, object : Interfaces.CipherDataListener {
            override fun onCipherData(success: Boolean, value: String) {
                if (success) {
                    var tagOwner = ServiceManager.getInstence().pboc.getEmvTlvData(TAG_CARD_OWNER)
                    if (tagOwner.isNotEmpty()) {
                        chipCardOwner = Utils.hexToAscii(BCDASCII.bytesToHexString(tagOwner))
                    }
                    chipAid = BCDASCII.bytesToHexString(ServiceManager.getInstence().pboc.getEmvTlvData(TAG_AID))
                    chipTvr = BCDASCII.bytesToHexString(ServiceManager.getInstence().pboc.getEmvTlvData(TAG_TVR))
                    chipArqc = BCDASCII.bytesToHexString(ServiceManager.getInstence().pboc.getEmvTlvData(TAG_ARQC))
                    chipTsi = BCDASCII.bytesToHexString(ServiceManager.getInstence().pboc.getEmvTlvData(TAG_TSI))
                    chipApn = Utils.hexToAscii(BCDASCII.bytesToHexString(ServiceManager.getInstence().pboc.getEmvTlvData(TAG_APN)))
                    val panSeq = "5F3401" + BCDASCII.bytesToHexString(ServiceManager.getInstence().pboc.getEmvTlvData(TAG_PAN_SEQ))
                    chipIcData = /*Utils.translateTlv(*/out.icData + panSeq
                    processTx(ENTRY_MODE_CHIP, "", value,
                            chipIcData, chipCardNumber, chipExpDate, chipCardOwner, needSignature, chipAid, chipArqc, chipTvr, chipTsi, chipApn)
                } else {
                    DialogStatusHelper.closeDialog()
                    txListener.onTxFailed(value)
                }
            }
        })
    }

    override fun onRequestAmount() {
        Looper.prepare()
        try {
            ServiceManager.getInstence().pboc.setAmount(Integer.parseInt(StringHelper.changeAmout(amount).replace(".", "")))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Looper.loop()
    }

    override fun onReadECBalance(p0: Intent?) {
        print(p0.toString())
    }

    override fun onContactlessCardType(kernelType: Int) {
        val KernelMode: KernelType
        when (kernelType) {
            1 -> KernelMode = KernelType.QuicsKernel
            2 -> KernelMode = KernelType.QvsdcKernel
            3 -> KernelMode = KernelType.MastrtKernel
            4 -> KernelMode = KernelType.DiscoveKernel
            5 -> KernelMode = KernelType.AmexKernel
        }
    }

    override fun onRequestSinature() {
        try {
            needSignature = true
            ServiceManager.getInstence().pboc.comfirmSinature()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSelectApplication(p0: MutableList<String>?) {
        Looper.prepare()
        try {
            ServiceManager.getInstence().pboc.selectApplication(1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Looper.loop()
    }

    override fun onConfirmCertInfo(p0: String?, p1: String?) {
        print(p0)
    }

    override fun onTransactionResult(result: Int, data: Intent?) {
        if (result == PBOCTransactionResult.QPBOC_ARQC) {
            // quick pay to process(快速交易流程)
            val rf_data = OutputQPBOCResult(data)
            val mDataRecord = KernelDataRecord()
            prepareNfcTx(rf_data, mDataRecord)
            pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW)
        } else if (result == PBOCTransactionResult.APPROVED) {
            if (inputCardType == CardType.RF_CARD) {
                val rf_data = OutputQPBOCResult(data)
                when (rf_data.cvmCode) {
                    OutcomeParameterSet.CONTACTLESS_OPS_CVM_NO_CVM, OutcomeParameterSet.CONTACTLESS_OPS_CVM_ONLINE_PIN, OutcomeParameterSet.CONTACTLESS_OPS_CVM_SIGNATURE, OutcomeParameterSet.CONTACTLESS_OPS_CVM_CONFIRMATION_CODE_VERIFIED -> {
                        val mDataRecord = KernelDataRecord()
                        prepareNfcTx(rf_data, mDataRecord)
                        pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW)
                    }
                    OutcomeParameterSet.CONTACTLESS_OPS_CVM_NA -> DialogStatusHelper.closeDialog()
                    else -> DialogStatusHelper.closeDialog()
                }
            } else {
                try {
                    val data1 = ServiceManager.getInstence().pboc.getEmvTlvData(0x9F26)
                    val data2 = ServiceManager.getInstence().pboc.getEmvTlvData(0x9F12)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (result == PBOCTransactionResult.TERMINATED) {
            Log.e(this.javaClass.simpleName, "Transaction terminated")
            DialogStatusHelper.closeDialog()
        }
    }

    override fun onReadCardOfflineRecord(p0: Intent?) {
        print(p0.toString())
    }

    override fun onError(p0: Intent?) {
        DialogStatusHelper.closeDialog()
        txListener.onTxFailed(activity.getString(R.string.pt_e_unknown))
    }

    /**
     * [OnPinInputListener] methods
     **/
    override fun onInput(len: Int, key: Int) {
        val message = Message()
        message.what = PIN_SHOW
        val bundle = Bundle()
        bundle.putInt("len", len)
        bundle.putInt("key", key)
        message.data = bundle
        pinpad_model.sendMessage(message)
    }

    override fun onError(errorCode: Int) {
        if (errorCode and 0x63c0 == 0x63c0) {
            pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS)
            pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW)
        } else {
            pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS)
            try {
                ServiceManager.getInstence().pboc.comfirmPinpad(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onConfirm(p0: ByteArray?, p1: Boolean) {
        pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS)
        when (inputCardType) {
            CardType.IC_CARD -> {
                try {
                    ServiceManager.getInstence().pboc.comfirmPinpad(if (!p1) p0 else null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            CardType.RF_CARD -> {
                cipherData(chipTrack, object : Interfaces.CipherDataListener {
                    override fun onCipherData(success: Boolean, value: String) {
                        if (success) {
                            processTx(ENTRY_MODE_CHIP, "", value,
                                    chipIcData, chipCardNumber, chipExpDate, chipCardOwner, needSignature, chipAid, chipArqc, chipTvr, chipTsi, chipApn)
                        } else {
                            DialogStatusHelper.closeDialog()
                            txListener.onTxFailed(value)
                        }
                    }
                })
            }
        }
    }

    override fun onCancel() {
        pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS)
        txListener.onTxFailed(activity.getString(R.string.pt_e_canceled))
    }

    override fun onPinpadShow(data: ByteArray) {
        val message = Message()
        message.what = SETLAYOUT
        message.obj = data
        pinpad_model.sendMessage(message)
    }

    /* Prepare parameters of NFC transaction */
    private fun prepareNfcTx(data: OutputQPBOCResult, kernel: KernelDataRecord) {
        kernel.setPiccDataRecord(data.kernelData, null)
        chipIcData = /*Utils.translateTlv(*/BCDASCII.bytesToHexString(data.kernelData, data.kernelData.size)
        chipCardNumber = data.pan
        chipMaskedPan = data.maskedPan
        chipTrack = data.track
        chipExpDate = data.expiredDate
        var tagOwner = kernel.getDataTlv("5F20") ?: ""
        if (tagOwner.isNotEmpty()) {
            chipCardOwner = Utils.hexToAscii(tagOwner)
        }
        chipAid = kernel.getDataTlv("4F") ?: ""
        chipArqc = kernel.getDataTlv("9F26") ?: ""
        chipTvr = kernel.getDataTlv("95") ?: ""
        chipTsi = kernel.getDataTlv("9B") ?: ""
        chipApn = Utils.hexToAscii(kernel.getDataTlv("9F12") ?: "")
    }

    private fun cipherData(value: String, listener: Interfaces.CipherDataListener) {
        activity.runOnUiThread {
            DialogStatusHelper.showDialog(activity, activity.getString(R.string.pt_t_cipher_information))
        }
        val request = CipherData_Request()
        request.body = CipherData_Body()
        request.body?.data = CipherData_BodyData()
        request.body?.data?.value = value
        request.body?.data?.key = Preferences(activity).loadData(R.string.pt_sp_banorte_aes_key, "")
                ?: ""
        API_Quetzalcoatl.getQuetzalcoatlService().encryptData(request).enqueue(object : Callback<CipherData_Result> {
            override fun onResponse(call: Call<CipherData_Result>, response: Response<CipherData_Result>) {
                if (response.code() == HttpsURLConnection.HTTP_OK && response.body()?.body?.data?.result != null) {
                    listener.onCipherData(true, response.body()?.body?.data?.result ?: "")
                } else {
                    listener.onCipherData(false, activity.getString(R.string.pt_e_no_response))
                }
            }

            override fun onFailure(call: Call<CipherData_Result>, t: Throwable) {
                listener.onCipherData(
                        false, t.message
                        ?: activity.getString(R.string.pt_e_unknown)
                )
            }
        })
    }

    private fun processTx(
            entryMode: String, track1: String, track2: String, emvTags: String, maskedPan: String,
            expirationDate: String, nombreTarjetahabiente: String, needSign: Boolean, aid: String = "",
            arqc: String = "", tvr: String = "", tsi: String = "", apn: String = "") {
        activity.runOnUiThread {
            DialogStatusHelper.showDialog(activity, activity.getString(R.string.pt_t_doing_payworks_tx))
        }
        val map = HashMap<String, String>()
        map[CMD_TRANS] = "AUTH"
        map[MERCHANT_ID] = Preferences(activity).loadData(R.string.pt_sp_banorte_merchant_id, "")
                ?: ""
        map[USER] = Preferences(activity).loadData(R.string.pt_sp_banorte_user, "") ?: ""
        map[PASSWORD] = Preferences(activity).loadData(R.string.pt_sp_banorte_black_box, "") ?: ""
        val idTownship = Preferences(activity).loadDataInt(R.string.pt_sp_quetz_id_township)
        val terminalCode = Preferences(activity).loadData(R.string.pt_sp_quetz_id_terminal, "")
        var controlCounter = Preferences(activity).loadDataInt(R.string.pt_sp_banorte_counter_control) + 1
        map[CONTROL_NUMBER] =
                CONTROL_NUMBER_TOWNSHIP + idTownship + CONTROL_NUMBER_TERMINAL + terminalCode + CONTROL_NUMBER_ID + controlCounter.toString()
        map[TERMINAL_ID] = Preferences(activity).loadData(R.string.pt_sp_banorte_serial_number, "")
                ?: ""
        map[AMOUNT] = amount
        map[PAGO_MOVIL] = "0"  // No es PagoMovil
        map[ENTRY_MODE] = entryMode
        map[MODE] = modeTx
        map[RESPONSE_LANGUAGE] = "ES"
        map[CUSTOMER_REF2] = Utils.getImeiDevice(activity)
        map[CUSTOMER_REF5] = "QSISTEMASMX3"
        map[BANORTE_URL] = "https://via.banorte.com/InterredesSeguro"
        map[TRACK1] = track1
        map[TRACK2] = track2
        map[EMV_TAGS] = emvTags
        API_Banorte.getBanorteService().processTx(map).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    val headerJson = Utils.mapHeaders(response.headers())
                    val result = Gson().fromJson(headerJson, Result_ProcessTx::class.java)
                    val paymentsVoucher = PaymentsVoucher(activity, txListener)
                    val c = Calendar.getInstance().time
                    val date = SimpleDateFormat("dd-MM-yyyy").format(c)
                    val hour = SimpleDateFormat("HH:mm:SS").format(c)
                    Preferences(activity).saveDataInt(R.string.pt_sp_banorte_counter_control, controlCounter)
                    when (result.resultadoPayW) {
                        PWR_APROBADA -> {
                            var txInfo = TransactionInfo(aid, apn, arqc, result.codigoAut, entryMode, maskedPan, date,
                                    hour, result.idAfiliacion, expirationDate, result.marcaTarjeta, result.tipoTarjeta, result.bancoEmisor,
                                    result.reference, amount, tvr, tsi, result.noControl,
                                    nombreTarjetahabiente, TRANSACTION_TYPE)
                            var voucher = Voucher(result.noControl, maskedPan.substring(maskedPan.length - 4, maskedPan.length), expirationDate, result.marcaTarjeta,
                                    result.tipoTarjeta, result.bancoEmisor, result.codigoAut, result.reference,
                                    amount, nombreTarjetahabiente, date, hour, aid, tvr, tsi, apn,
                                    FLAG_TRANS_APPROVE, needSign, result.idAfiliacion, map[CUSTOMER_REF2]!!)
                            if (entryMode == ENTRY_MODE_CHIP) {
                                val onlineData = InputPBOCOnlineData()
                                onlineData.responseCode = "00"
                                onlineData.authCode = ""
                                onlineData.icData = ""
                                try {
                                    ServiceManager.getInstence().pboc.inputOnlineProcessResult(onlineData.intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            txListener.onTxApproved(txInfo)
                            paymentsVoucher.printVoucher(activity, voucher, entryMode, false)
                        }
                        PWR_DECLINADA -> {
                            DialogStatusHelper.closeDialog()
                            txListener.onTxFailed(activity.getString(R.string.pt_e_declined))
                            val voucher = Voucher(result.noControl, maskedPan.substring(maskedPan.length - 4, maskedPan.length), expirationDate,
                                    result.marcaTarjeta, result.tipoTarjeta, result.bancoEmisor, result.codigoAut,
                                    result.reference, amount, nombreTarjetahabiente, date, hour,
                                    "", "", "", "", FLAG_TRANS_DECLINE,
                                    needSign, result.idAfiliacion, map[CUSTOMER_REF2]!!)
                            if (entryMode == ENTRY_MODE_BAND) {
                                notifyTxFailToChip()
                            }
                            paymentsVoucher.printVoucher(activity, voucher, entryMode, false)
                        }
                        PWR_RECHAZADA -> {
                            DialogStatusHelper.closeDialog()
                            txListener.onTxFailed(activity.getString(R.string.pt_e_rejected))
                            val voucher = Voucher(result.noControl, maskedPan.substring(maskedPan.length - 4, maskedPan.length),
                                    expirationDate, result.marcaTarjeta, result.tipoTarjeta, result.bancoEmisor, result.codigoAut,
                                    result.reference, amount, nombreTarjetahabiente, date,
                                    hour, "", "", "", "", FLAG_TRANS_REJECTED,
                                    needSign, result.idAfiliacion, map[CUSTOMER_REF2]!!)
                            if (entryMode == ENTRY_MODE_CHIP) {
                                notifyTxFailToChip()
                            }
                            paymentsVoucher.printVoucher(activity, voucher, entryMode, false)
                        }
                        PWR_SIN_RESPUESTA -> {
                            val voucher = Voucher(result.noControl, maskedPan.substring(maskedPan.length - 4, maskedPan.length), expirationDate,
                                    result.marcaTarjeta, result.tipoTarjeta, result.bancoEmisor, result.codigoAut, result.reference,
                                    amount, nombreTarjetahabiente, date, hour, "",
                                    "", "", "", FLAG_TRANS_TIMEOUT,
                                    needSign, result.idAfiliacion, map[CUSTOMER_REF2]!!)
                            DialogStatusHelper.closeDialog()
                            txListener.onTxFailed(activity.getString(R.string.pt_e_no_response))
                            if (entryMode == ENTRY_MODE_CHIP) {
                                notifyTxFailToChip()
                            }
                            paymentsVoucher.printVoucher(activity, voucher, entryMode, false)
                        }
                    }
                } else if (response.code() == HttpsURLConnection.HTTP_CLIENT_TIMEOUT) {
                    DialogStatusHelper.closeDialog()
                    txListener.onTxFailed(activity.getString(R.string.pt_e_no_response))
                    val paymentsVoucher = PaymentsVoucher(activity, txListener)
                    val c = Calendar.getInstance().time
                    val date = SimpleDateFormat("dd-MM-yyyy").format(c)
                    val hour = SimpleDateFormat("HH:mm:SS").format(c)
                    val voucher = Voucher(map[CONTROL_NUMBER]!!, maskedPan.substring(maskedPan.length - 4, maskedPan.length), expirationDate, "",
                            "", "", "", "", amount, nombreTarjetahabiente, date, hour, aid, tvr, tsi, apn,
                            FLAG_TRANS_TIMEOUT, needSign, map[MERCHANT_ID]!!, map[CUSTOMER_REF2]!!)
                    paymentsVoucher.printVoucher(activity, voucher, entryMode, false)
                    if (entryMode == ENTRY_MODE_CHIP) {
                        notifyTxFailToChip()
                    }
                } else {
                    DialogStatusHelper.closeDialog()
                    txListener.onTxFailed(activity.getString(R.string.pt_e_no_response))
                    if (entryMode == ENTRY_MODE_CHIP) {
                        notifyTxFailToChip()
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                DialogStatusHelper.closeDialog()
                txListener.onTxFailed(t.message ?: activity.getString(R.string.pt_e_unknown))
                val paymentsVoucher = PaymentsVoucher(activity, txListener)
                val c = Calendar.getInstance().time
                val date = SimpleDateFormat("dd-MM-yyyy").format(c)
                val hour = SimpleDateFormat("HH:mm:SS").format(c)
                val voucher = Voucher(map[CONTROL_NUMBER]!!, maskedPan.substring(maskedPan.length - 4, maskedPan.length), expirationDate, "",
                        "", "", "", "", amount, nombreTarjetahabiente, date, hour, aid, tvr, tsi, apn,
                        FLAG_TRANS_OFFLINE, needSign, map[MERCHANT_ID]!!, map[CUSTOMER_REF2]!!)
                paymentsVoucher.printVoucher(activity, voucher, entryMode, false)
                if (entryMode == ENTRY_MODE_CHIP) {
                    notifyTxFailToChip()
                }
            }
        })
    }

    private fun notifyTxFailToChip() {
        val onlineData = InputPBOCOnlineData()
        onlineData.responseCode = "05"
        try {
            ServiceManager.getInstence().pboc.inputOnlineProcessResult(onlineData.intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}