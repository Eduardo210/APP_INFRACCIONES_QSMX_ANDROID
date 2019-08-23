package mx.qsistemas.payments_transfer

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import com.basewin.aidl.OnPrinterListener
import com.basewin.models.BitmapPrintLine
import com.basewin.models.PrintLine
import com.basewin.models.TextPrintLine
import com.basewin.services.PrinterBinder
import com.basewin.services.ServiceManager
import mx.qsistemas.payments_transfer.dtos.Voucher
import mx.qsistemas.payments_transfer.utils.*
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
            ServiceManager.getInstence().printer.setPrintFontByAsserts("sans_bold.ttf")
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Banorte", TextPrintLine.FONT_LARGE, PrintLine.CENTER))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Venta\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Quetzalcoátl Sistemas S.A. de C.V.", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Cto. Puericultores 18, Ciudad Satélite", TextPrintLine.FONT_SMALL, PrintLine.CENTER))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Naucalpan de Juárez, Edo. de México CP 53100\n\n", TextPrintLine.FONT_SMALL, PrintLine.CENTER))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Afiliación: ${voucherInfo.afiliacion}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Terminal ID: ${voucherInfo.terminalId}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("No. de control: ${voucherInfo.noControl}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
            if (isCopy) {
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Copia Cliente\n\n", TextPrintLine.FONT_LARGE, PrintLine.CENTER))
            }
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Número de tarjeta", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("**** **** **** ${voucherInfo.noCard}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("${voucherInfo.flagTrans}\n\n", TextPrintLine.FONT_LARGE, PrintLine.CENTER))
            if (voucherInfo.flagTrans == FLAG_TRANS_APPROVE) {
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Tipo de tarjeta: ${voucherInfo.cardBrand}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Tipo:  ${voucherInfo.cardType}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Banco Emisor: ${voucherInfo.bank}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Codigo Autorización: ${voucherInfo.authCode}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Referencia: ${voucherInfo.reference}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Importe:    $${voucherInfo.amount}\n\n\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                if (voucherInfo.needSign) {
                    ServiceManager.getInstence().printer.addPrintLine(getPrintLine("-----------------------------------------------", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
                } else {
                    if (voucherInfo.entryMode == ENTRY_MODE_CHIP)
                        ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Autorizado con firma electrónica\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
                    else if (voucherInfo.entryMode == ENTRY_MODE_CONTACTLESS) {
                        val icon = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_contactless)
                        val bitmapPrintLine = BitmapPrintLine()
                        bitmapPrintLine.type = PrintLine.BITMAP
                        bitmapPrintLine.position = PrintLine.CENTER
                        bitmapPrintLine.bitmap = icon
                        ServiceManager.getInstence().printer.addPrintLine(bitmapPrintLine)
                        ServiceManager.getInstence().printer.addPrintLine(getPrintLine("CONTACTLESS\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
                        ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Autorizado sin firma\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
                    }
                }
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("${voucherInfo.cardBeneficiary}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
            } else {
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("${voucherInfo.detailError.replace("+", " ")}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
            }
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Fecha: ${voucherInfo.date}", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("Hora: ${voucherInfo.hour}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER))
            if ((voucher.entryMode == ENTRY_MODE_CHIP || voucher.entryMode == ENTRY_MODE_CONTACTLESS) && voucherInfo.flagTrans == FLAG_TRANS_APPROVE) {
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("ARQC: ${maskedField(voucherInfo.ARQC)}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("AID: ${voucherInfo.AID}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("TVR: ${voucherInfo.TVR}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                ServiceManager.getInstence().printer.addPrintLine(getPrintLine("TSI: ${voucherInfo.TSI}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                if (voucherInfo.APN.isNotEmpty())
                    ServiceManager.getInstence().printer.addPrintLine(getPrintLine("APN: ${voucherInfo.APN}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
                else
                    ServiceManager.getInstence().printer.addPrintLine(getPrintLine("AL: ${voucherInfo.AL}\n\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
            }
            ServiceManager.getInstence().printer.addPrintLine(getPrintLine("\n\n\n.", TextPrintLine.FONT_NORMAL, PrintLine.LEFT))
            ServiceManager.getInstence().printer.beginPrint(this)
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

    private fun getPrintLine(text: String, size: Int, position: Int): TextPrintLine {
        val textPrintLine = TextPrintLine()
        textPrintLine.type = PrintLine.TEXT
        textPrintLine.position = position
        textPrintLine.size = size
        textPrintLine.content = text
        return textPrintLine
        /*val json = JSONObject()
        try {
            json.put("content-type", "txt")
            json.put("content", text)
            json.put("size", size)
            json.put("position", position)
            json.put("offset", "0")
            json.put("bold", "0")
            json.put("italic", "0")
            json.put("height", "-1")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json*/
    }

    private fun maskedField(field: String): String {
        val range = field.length - 4 until field.length
        var masked = ""
        field.forEachIndexed { index, c ->
            if (index !in range) masked += "*" else masked += c
        }
        return masked
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
            //builder.setNegativeButton("Cancelar") { _, _ -> txListener.onTxVoucherPrinted() }
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