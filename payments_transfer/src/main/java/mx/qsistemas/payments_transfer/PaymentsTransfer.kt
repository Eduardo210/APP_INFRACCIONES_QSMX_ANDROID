package mx.qsistemas.payments_transfer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.StrictMode
import android.util.Log
import com.basewin.aidl.OnBarcodeCallBack
import com.basewin.aidl.OnPrinterListener
import com.basewin.define.InputPBOCInitData
import com.basewin.define.TransactionType
import com.basewin.interfaces.OnDetectListener
import com.basewin.log.LogUtil
import com.basewin.services.ServiceManager
import com.basewin.utils.BCDHelper
import com.basewin.utils.CUPParam
import com.basewin.utils.LoadParamManage
import com.facebook.stetho.Stetho
import com.pos.sdk.card.PosCardInfo
import mx.qsistemas.payments_transfer.dtos.*
import mx.qsistemas.payments_transfer.net.API_Quetzalcoatl
import mx.qsistemas.payments_transfer.pboc.PbocListener
import mx.qsistemas.payments_transfer.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection


const val CONTROL_NUMBER_TOWNSHIP = "M"
const val CONTROL_NUMBER_TERMINAL = "T"
const val CONTROL_NUMBER_ID = "ID"
const val TRANSACTION_TYPE = "Venta"

@SuppressLint("StaticFieldLeak")
object PaymentsTransfer : Interfaces.Contracts {

    private lateinit var paymentsPreferences: Preferences
    private lateinit var paymentsContext: Context
    private lateinit var txListener: IPaymentsTransfer.TransactionListener
    private lateinit var amount: String
    private lateinit var activity: Activity
    lateinit var modeTx: String
    var isDeviceConfigured: Boolean = false

    override fun initialize(context: Context) {
        paymentsContext = context
        /* Granted permission to access to storage*/
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        /* Init Device Server */
        ServiceManager.getInstence().init(context)
        /* Init the GlobalData cache         */
        GlobalData.getInstance().init(context)
        /* Init shared preferences of the library */
        paymentsPreferences = Preferences(paymentsContext)
        isDeviceConfigured = paymentsPreferences.loadDataBoolean(R.string.sp_quetz_is_device_config, false)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(paymentsContext)
            LogUtil.openLog()
        }
    }

    override fun configDevice(idTownship: Int, terminalCode: String) {
        /* Save the control number of initialize */
        paymentsPreferences.saveDataInt(R.string.sp_quetz_id_township, idTownship)
        paymentsPreferences.saveData(R.string.sp_quetz_id_terminal, terminalCode)
        paymentsPreferences.saveDataInt(R.string.sp_quetz_id_township, 0)
        /* Load Params of the terminal */
        ServiceManager.getInstence().pinpad.loadProtectKeyByArea(1, "B0030345E0B41AE3AB93AA836BA5CE38")
        ServiceManager.getInstence().pinpad.loadMainKeyWithKcvByArea(
                1,
                1,
                "F9FA6ED854CCCDB23E1B036F4B893AA5",
                "6D89EC94"
        )
        /* Set time to terminal */
        ServiceManager.getInstence().deviceinfo.spTime = BCDHelper.StrToBCD(System.currentTimeMillis().toString())
        ServiceManager.getInstence().pinpad.loadMacKeyByArea(1, 1, "585EEED9AEFEFDF2C7918E080B560D68", "BE0BDFA1")
        ServiceManager.getInstence().pinpad.loadTDKeyByArea(1, 1, "585EEED9AEFEFDF2C7918E080B560D68", "BE0BDFA1")
        ServiceManager.getInstence().pinpad.loadPinKeyByArea(1, 1, "F9FA6ED854CCCDB23E1B036F4B893AA5", "BE0BDFA1")
        GlobalData.getInstance().pinpadVersion = PINPAD_INTERFACE_VERSION3
        GlobalData.getInstance().area = 1
        GlobalData.getInstance().tmkId = 1
        GlobalData.getInstance().pinkeyFlag = true
        ServiceManager.getInstence().pboc.setEmvParamSetBySdk(false)
        var posEmvParam = ServiceManager.getInstence().pboc.posTermPara
        System.arraycopy(byteArrayOf(0x04, 132.toByte()), 0, posEmvParam.TransCurrCode, 0, 2)
        System.arraycopy(byteArrayOf(0x04, 132.toByte()), 0, posEmvParam.CountryCode, 0, 2)
        posEmvParam.TerminalType =
                0x16  // https://www.emvco.com/wp-content/uploads/2017/05/EMV_v4.3_Book_4_Other_Interfaces_20120607062305603.pdf (Pag. 129)
        posEmvParam.Capability[0] = 0xe0.toByte()
        posEmvParam.Capability[1] = 0xf9.toByte()//0xb8.toByte()
        posEmvParam.Capability[2] = 0x00.toByte()//0xc8.toByte()
        ServiceManager.getInstence().pboc.posTermPara = posEmvParam
        posEmvParam = ServiceManager.getInstence().pboc.posTermPara
        if (BuildConfig.DEBUG) {
            Log.i(this.javaClass.simpleName, "posEmvParam:$posEmvParam")
        }
        LoadParamManage.getInstance().DeleteAllTerParamFile()
        for (j in CUPParam.aid_data.indices) {
            ServiceManager.getInstence().pboc.updateAID(0, CUPParam.aid_data[j])
        }
        for (i in CUPParam.ca_data.indices) {
            ServiceManager.getInstence().pboc.updateRID(0, CUPParam.ca_data[i])
        }
        GlobalData.getInstance().login = true
        paymentsPreferences.saveDataBool(R.string.sp_quetz_is_device_config, true)
    }

    override fun loadKeyDevice(activity: Activity, data: LoadKeyData, listener: IPaymentsTransfer.LoadKeyListener) {
        activity.runOnUiThread {
            DialogStatusHelper.showDialog(activity, paymentsContext.getString(R.string.t_obtaining_key))
        }
        if (paymentsPreferences.loadData(R.string.sp_banorte_aes_key, "") == "") {
            val request = GetKey_Request()
            request.body = GetKey_Body()
            request.body?.data = GetKey_BodyData()
            request.body?.data?.serialNumber = data.serialNumber
            request.body?.data?.merchantId = data.merchantId
            request.body?.data?.user = data.user
            request.body?.data?.psw = data.psw
            val idTownship = paymentsPreferences.loadDataInt(R.string.sp_quetz_id_township)
            val terminalCode = paymentsPreferences.loadData(R.string.sp_quetz_id_terminal, "")
            var controlCounter = paymentsPreferences.loadDataInt(R.string.sp_banorte_counter_control) + 1
            request.body?.data?.controlNumber =
                    CONTROL_NUMBER_TOWNSHIP + idTownship + CONTROL_NUMBER_TERMINAL + terminalCode + CONTROL_NUMBER_ID + controlCounter.toString()
            API_Quetzalcoatl.getQuetzalcoatlService().getKey(request).enqueue(object : Callback<GetKey_Result> {
                override fun onResponse(call: Call<GetKey_Result>, response: Response<GetKey_Result>) {
                    if (response.code() == HttpsURLConnection.HTTP_OK && response.body()?.body != null) {
                        paymentsPreferences.saveData(R.string.sp_banorte_aes_key, response.body()?.body?.data?.result!!)
                        paymentsPreferences.saveDataInt(R.string.sp_banorte_counter_control, controlCounter)
                        paymentsPreferences.saveData(R.string.sp_banorte_serial_number, data.serialNumber)
                        paymentsPreferences.saveData(R.string.sp_banorte_merchant_id, data.merchantId)
                        paymentsPreferences.saveData(R.string.sp_banorte_user, data.user)
                        paymentsPreferences.saveData(R.string.sp_banorte_black_box, data.psw)
                        DialogStatusHelper.closeDialog()
                        listener.onLoadKey(true, paymentsContext.getString(R.string.s_load_key))
                    } else {
                        DialogStatusHelper.closeDialog()
                        listener.onLoadKey(false, paymentsContext.getString(R.string.e_no_response))
                    }
                }

                override fun onFailure(call: Call<GetKey_Result>, t: Throwable) {
                    DialogStatusHelper.closeDialog()
                    listener.onLoadKey(
                            false, t.message
                            ?: paymentsContext.getString(R.string.e_unknown)
                    )
                }
            })
        } else {
            DialogStatusHelper.closeDialog()
            listener.onLoadKey(true, paymentsContext.getString(R.string.s_load_key2))
        }
    }

    override fun runTransaction(activity: Activity, amount: String, modeTx: String, listener: IPaymentsTransfer.TransactionListener) {
        this.activity = activity
        this.modeTx = modeTx
        txListener = listener
        this.amount = amount
        activity.runOnUiThread {
            DialogStatusHelper.showDialog(activity, paymentsContext.getString(R.string.t_waiting_card))
        }
        if (paymentsPreferences.loadData(R.string.sp_banorte_aes_key, "") != "") {
            val intent = Intent()
            intent.putExtra(InputPBOCInitData.AMOUNT_FLAG, formatAmount(amount))
            intent.putExtra(InputPBOCInitData.IS_PBOC_FORCE_ONLINE, false)
            intent.putExtra(InputPBOCInitData.USE_DEVICE_FLAG, InputPBOCInitData.USE_MAG_CARD or InputPBOCInitData.USE_RF_CARD or InputPBOCInitData.USE_IC_CARD)
            intent.putExtra(InputPBOCInitData.TIMEOUT, 60)
            ServiceManager.getInstence().pboc.startTransfer(TransactionType.ONLINE_PAY, intent, PbocListener(amount, activity, txListener))
        } else {
            DialogStatusHelper.closeDialog()
            txListener.onTxFailed(paymentsContext.getString(R.string.e_need_to_load_key))
        }
    }

    override fun print(activity: Activity, json: String, bitmap: Array<Bitmap>?, listener: IPaymentsTransfer.PrintListener) {
        ServiceManager.getInstence().printer.print(json, bitmap, object : OnPrinterListener {
            override fun onStart() {
                activity.runOnUiThread {
                    DialogStatusHelper.showDialog(activity, paymentsContext.getString(R.string.t_printing))
                }
                listener.onStart()
            }

            override fun onError(p0: Int, p1: String?) {
                DialogStatusHelper.closeDialog()
                listener.onError(p0, p1 ?: "")
            }

            override fun onFinish() {
                DialogStatusHelper.closeDialog()
                listener.onFinish()
            }
        })
    }

    override fun scanCard(activity: Activity, timeout: Int, listener: IPaymentsTransfer.ScanCardListener) {
        val cardService = ServiceManager.getInstence().card
        cardService.openM1AndDetect(timeout, object : OnDetectListener {
            override fun onSuccess(p0: Int) {
                val posCardInfo = PosCardInfo()
                try {
                    ServiceManager.getInstence().card.getCardInfo(p0, posCardInfo);
                    listener.onSuccess(BCDHelper.bcdToString(posCardInfo.mSerialNum, 0, posCardInfo.mSerialNum.size))
                } catch (e: Exception) {
                    listener.onError(0, "")
                }
            }

            override fun onError(p0: Int, p1: String?) {
                listener.onError(p0, p1 ?: "")
            }
        })

    }

    override fun scanCode(activity: Activity, timeout: Long, listener: IPaymentsTransfer.ScanCodeListener) {
        ServiceManager.getInstence().scan.startScan(timeout, object : OnBarcodeCallBack {
            override fun onScanResult(p0: String?) {
                listener.onSuccess(p0 ?: "")
            }

            override fun onFinish(p0: Int, p1: String?) {
                listener.onError(p0, p1 ?: "")
            }
        })
    }

    private fun formatAmount(amount: String): Int? {
        return Integer.parseInt(StringHelper.changeAmout(amount).replace(".", ""))
    }
}


internal class Interfaces {
    interface Contracts {
        fun initialize(context: Context)
        fun configDevice(idTownship: Int, terminalCode: String)
        fun loadKeyDevice(activity: Activity, data: LoadKeyData, listener: IPaymentsTransfer.LoadKeyListener)
        fun runTransaction(
                activity: Activity,
                amount: String,
                modeTx: String,
                listener: IPaymentsTransfer.TransactionListener
        )

        fun print(activity: Activity, json: String, bitmap: Array<Bitmap>?, listener: IPaymentsTransfer.PrintListener)
        fun scanCard(activity: Activity, timeout: Int, listener: IPaymentsTransfer.ScanCardListener)
        fun scanCode(activity: Activity, timeout: Long, listener: IPaymentsTransfer.ScanCodeListener)
    }

    interface CipherDataListener {
        fun onCipherData(success: Boolean, value: String)
    }

    interface OnPinDialogListener {
        fun OnPinInput(result: Int)
        fun OnCreateOver()
    }
}