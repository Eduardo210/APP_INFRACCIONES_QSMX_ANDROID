package mx.qsistemas.payments_transfer

import android.app.Activity
import android.content.Context
import com.basewin.aidl.OnPrinterListener
import com.basewin.services.PrinterBinder
import com.basewin.services.ServiceManager
import mx.qsistemas.payments_transfer.dtos.Voucher
import mx.qsistemas.payments_transfer.utils.AlertDialogHelper
import mx.qsistemas.payments_transfer.utils.DialogStatusHelper
import mx.qsistemas.payments_transfer.utils.ENTRY_MODE_CHIP
import mx.qsistemas.payments_transfer.utils.FLAG_TRANS_APPROVE
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PaymentsVoucher(val context: Context, val txListener: IPaymentsTransfer.TransactionListener) : OnPrinterListener {

    private var printJson = JSONObject()
    private var hasPrintedCopy = false
    private lateinit var activity: Activity
    private lateinit var voucher: Voucher

    fun printVoucher(activity: Activity, voucherInfo: Voucher, isCopy: Boolean) {
        this.activity = activity
        this.voucher = voucherInfo
        try {
            activity.runOnUiThread { DialogStatusHelper.showDialog(activity, activity.getString(R.string.pt_t_printing_voucher)) }
            ServiceManager.getInstence().printer.setPrintGray(1000)
            val printTest = JSONArray()
            ServiceManager.getInstence().printer.setPrintFontByAsserts("sans_bold.ttf")
            printTest.put(getPrintObject("               Banorte", "3"))
            printTest.put(getPrintObject("                              Venta\n\n", "2"))
            printTest.put(getPrintObject("  Quetzalcoátl Sistemas S.A de C.V.", "2"))
            printTest.put(getPrintObject("              Cto. Puericultores 18, Ciudad Satélite", "1"))
            printTest.put(getPrintObject("      Naucalpan de Juárez, Edo. de México CP 53100\n\n", "1"))
            printTest.put(getPrintObject("Afiliación: ${voucherInfo.afiliacion}", "2"))
            printTest.put(getPrintObject("Terminal ID: ${voucherInfo.terminalId}", "2"))
            printTest.put(getPrintObject("No. de control: ${voucherInfo.noControl}\n\n", "2"))
            if (isCopy) {
                printTest.put(getPrintObject("        Copia Cliente\n\n", "3"))
            }
            printTest.put(getPrintObject("     Número de tarjeta     Vigencia", "2"))
            printTest.put(getPrintObject("     **** **** **** ${voucherInfo.noCard}        ${voucherInfo.caducity}\n\n", "2"))
            printTest.put(getPrintObject("             ${voucherInfo.flagTrans}\n\n", "3"))
            if (voucherInfo.flagTrans == FLAG_TRANS_APPROVE) {
                printTest.put(getPrintObject("Tipo de tarjeta: ${voucherInfo.cardBrand}", "2"))
                printTest.put(getPrintObject("Tipo:  ${voucherInfo.cardType}", "2"))
                printTest.put(getPrintObject("Banco Emisor: ${voucherInfo.bank}\n\n", "2"))
                printTest.put(getPrintObject("Codigo Autorización: ${voucherInfo.authCode}", "2"))
                printTest.put(getPrintObject("Referencia: ${voucherInfo.reference}\n\n", "2"))
                printTest.put(getPrintObject("Importe:    $${voucherInfo.amount}\n\n\n\n", "2"))
                if (voucherInfo.needSign) {
                    printTest.put(getPrintObject("-----------------------------------------------", "2"))
                    printTest.put(getPrintObject(" ${voucherInfo.cardBeneficiary}\n\n", "2"))
                } else {
                    printTest.put(getPrintObject("  Autorizado con firma electrónica\n\n\n", "2"))
                }
            }
            printTest.put(getPrintObject("          Fecha: ${voucherInfo.date}", "2"))
            printTest.put(getPrintObject("          Hora: ${voucherInfo.hour}\n\n", "2"))
            if (voucher.entryMode == ENTRY_MODE_CHIP && voucherInfo.flagTrans == FLAG_TRANS_APPROVE) {
                printTest.put(getPrintObject("AID: ${voucherInfo.AID}", "2"))
                printTest.put(getPrintObject("TVR: ${voucherInfo.TVR}", "2"))
                printTest.put(getPrintObject("TSI: ${voucherInfo.TSI}", "2"))
                printTest.put(getPrintObject("APN: ${voucherInfo.APN}\n\n", "2"))
            }
            printTest.put(getPrintObject("\n\n\n", "2"))
            printJson.put("spos", printTest)
            ServiceManager.getInstence().printer.print(printJson.toString(), null, this)
        } catch (e: JSONException) {
            e.printStackTrace()
            DialogStatusHelper.closeDialog()
            txListener.onTxVoucherFailer(context.getString(R.string.pt_e_print_other_error))
        } catch (e: Exception) {
            e.printStackTrace()
            DialogStatusHelper.closeDialog()
            txListener.onTxVoucherFailer(context.getString(R.string.pt_e_print_other_error))
        }
    }

    private fun getPrintObject(text: String, size: String): JSONObject {
        val json = JSONObject()
        try {
            json.put("content-type", "txt")
            json.put("content", text)
            json.put("size", size)
            json.put("position", "left")
            json.put("offset", "0")
            json.put("bold", "0")
            json.put("italic", "0")
            json.put("height", "-1")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json
    }

    override fun onFinish() {
        DialogStatusHelper.closeDialog()
        if (!hasPrintedCopy && voucher.flagTrans == FLAG_TRANS_APPROVE) {
            val builder = AlertDialogHelper.getGenericBuilder(
                    activity.getString(R.string.pt_s_title_dialog_payment), activity.getString(R.string.pt_s_subtitle_dialog_payment), activity)
            builder.setPositiveButton("Aceptar") { _, _ ->
                hasPrintedCopy = true
                printVoucher(activity, voucher, true)
            }
            builder.setNegativeButton("Cancelar") { _, _ -> txListener.onTxVoucherPrinted() }
            builder.show()
        } else {
            txListener.onTxVoucherPrinted()
        }
    }

    override fun onError(errorCode: Int, p1: String?) {
        if (errorCode == PrinterBinder.PRINTER_ERROR_NO_PAPER) {
            DialogStatusHelper.closeDialog()
            txListener.onTxVoucherFailer(context.getString(R.string.pt_e_print_run_out_paper))
        }
        if (errorCode == PrinterBinder.PRINTER_ERROR_OVER_HEAT) {
            DialogStatusHelper.closeDialog()
            txListener.onTxVoucherFailer(context.getString(R.string.pt_e_print_overheat))
        }
        if (errorCode == PrinterBinder.PRINTER_ERROR_OTHER) {
            DialogStatusHelper.closeDialog()
            txListener.onTxVoucherFailer(context.getString(R.string.pt_e_print_other_error))
        }
    }

    override fun onStart() {
    }
}