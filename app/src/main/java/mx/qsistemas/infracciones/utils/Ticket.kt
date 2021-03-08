package mx.qsistemas.infracciones.utils

import android.app.Activity
import com.basewin.models.BitmapPrintLine
import com.basewin.models.PrintLine
import com.basewin.models.TextPrintLine
import com.basewin.zxing.utils.QRUtil
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Ticket {
    companion object {

        fun printTicket(activity: Activity, listener: TicketListener) {
            val printlines = mutableListOf<PrintLine>()


            //Impresión de encabezados
            SingletonTicket.headers.forEach {
                printlines.add(getPrintLine(it, TextPrintLine.FONT_NORMAL, PrintLine.CENTER, true))
            }
            printlines.add(getPrintLine("\nDETECCIÓN   Y   LEVANTAMIENTO", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("ELECTRÓNICO     DE     INFRACCIO-", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("NES  A CONDUCTORES   DE   VEHÍ-", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("CULOS     QUE     CONTRAVENGAN", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("LAS   DISPOSICIONES    EN   MATE-", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("RIA  DE  TRÁNSITO, EQUILIBRIO  E-", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("COLÓGICO       PROTECCIÓN       AL", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("AMBIENTE    Y     PARA    LA    PRE-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("VENCIÓN    Y    CONTROL    DE    LA", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("CONTAMINACIÓN,   ASI COMO  PA-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("GO   DE   SANCIONES    Y    APLICA-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("CIÓN    DE   MEDIDAS   DE   SEGURI-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("DAD.\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("EL C. AGENTE  QUE  SUSCRIBE  LA", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("PRESENTE      BOLETA       DE      IN-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("FRACCIÓN,  ESTA  FACULTADO   EN", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("TÉRMINOS   DE   LOS  QUE  SE   ES-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("TABLECE     EN     LOS   ARTÍCULOS", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("21   Y  115,  FRACCIÓN  III,   INCISO,", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("H)   DE   LA  CONSTITUCION    POLÍ-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("TICA   DE  LOS  ESTADOS   UNIDOS", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("MEXICANOS     DE     ACUERDO     A", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("LO   ESTABLECIDO   EN   LOS   ARTÍ-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("CULOS   8.3,  8.10,  8.18,  8.19   BIS,", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("8.19   TERCERO   Y    8.19   CUARTO", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("DEL     CÓDIGO     ADMINISTRATIVO", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("DEL    ESTADO    DE    MÉXICO.  ASÍ", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("COMO HACER  CONSTAR  LOS  HE-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("CHOS     QUE     MOTIVAN    LA    IN", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("FRACCIÓN    EN    TÉRMINOS    DEL", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("ARTÍCULO   16  DE NUESTRA CAR-", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("TA MAGNA.\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("${SingletonTicket.dateTicket}\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, true))
            printlines.add(getPrintLine("FOLIO: ${SingletonTicket.folioTicket}\n", TextPrintLine.FONT_NORMAL, PrintLine.RIGHT, false))

            //Datos del infractor
            printlines.add(getPrintLine("DATOS DEL INFRACTOR:\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, true))
            printlines.add(getPrintLine(SingletonTicket.completeNameOffender, TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine(SingletonTicket.rfcOffender, TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            if (SingletonTicket.streetOffender != "-")
                printlines.add(getPrintLine("DOMICILIO: ${SingletonTicket.streetOffender}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            if (SingletonTicket.noExtOffender != "-")
                printlines.add(getPrintLine("EXTERIOR: ${SingletonTicket.noExtOffender}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            if (SingletonTicket.noIntOffender != "-")
                printlines.add(getPrintLine("INTERIOR: ${SingletonTicket.noIntOffender}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            if (SingletonTicket.colonyOffender != "-")
                printlines.add(getPrintLine("COLONIA: ${SingletonTicket.colonyOffender}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            if (SingletonTicket.stateOffender != "-")
                printlines.add(getPrintLine("ENTIDAD: ${SingletonTicket.stateOffender}\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))

            // Datos de licencia
            if (SingletonTicket.noLicenseOffender != "-")
                printlines.add(getPrintLine("LICENCIA/PERMISO: ${SingletonTicket.noLicenseOffender}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            if (SingletonTicket.typeLicenseOffender != "-")
                printlines.add(getPrintLine("TIPO LICENCIA: ${SingletonTicket.typeLicenseOffender}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            if (SingletonTicket.stateLicenseOffender != "-")
                printlines.add(getPrintLine("EXPEDIDA: ${SingletonTicket.stateLicenseOffender}\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))

            // Características del vehículo
            printlines.add(getPrintLine("CARACTERÍSTICAS DEL VEHÍCULO:\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, true))
            printlines.add(getPrintLine("MARCA: ${SingletonTicket.brandVehicle}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("SUBMARCA: ${SingletonTicket.subBrandVehicle}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("TIPO: ${SingletonTicket.typeVehicle}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("COLOR: ${SingletonTicket.colorVehicle}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("MODELO: ${SingletonTicket.modelVehicle}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("IDENTIFICADOR: ${SingletonTicket.identifierVehicle}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("NÚMERO: ${SingletonTicket.noIdentifierVehicle}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("AUTORIDAD QUE EXPIDE: ${SingletonTicket.expeditionAuthVehicle}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("EXPEDIDO: ${SingletonTicket.stateExpVehicle}\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))

            // Artículos y Fracciones
            printlines.add(getPrintLine("ARTÍCULOS  DEL  REGLAMENTO", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, true))
            printlines.add(getPrintLine("DE  TRÁNSITO DEL ESTADO  DE", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, true))
            printlines.add(getPrintLine("MÉXICO\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, true))
            printlines.add(getPrintLine("ARTÍCULO/FRACCIÓN\t\t\tU.M.A.\t\t\tPUNTOS", TextPrintLine.FONT_SMALL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("*******************************", TextPrintLine.FONT_SMALL, PrintLine.CENTER, false))
            SingletonTicket.fractionsList.forEach {
                printlines.add(getPrintLine("${it.article} / ${it.fraction}\t\t\t${it.umas}\t\t\t${it.points}", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
                printlines.add(getPrintLine("CONDUCTA   QUE   MOTIVA   LA   INFRACCIÓN", TextPrintLine.FONT_SMALL, PrintLine.CENTER, true))
                printlines.add(getPrintLine("${it.motivation}\n", TextPrintLine.FONT_SMALL, PrintLine.LEFT, false))
            }
            printlines.add(getPrintLine("LA CANTIDAD  DE  LA  MULTA  FUE", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("IMPUESTA  CON  BASE  A  LAS  TA-", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("BLAS   DE    EQUIVALENCIAS    QUE", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("MARCA    EL    REGLAMENTO     DE", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("TRÁNSITO     DEL     ESTADO    DE", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("MÉXICO\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))

            // Dirección de infracción
            printlines.add(getPrintLine("CALLE: ${SingletonTicket.streetInfraction}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("ENTRE: ${SingletonTicket.betweenStreetInfraction}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("Y: ${SingletonTicket.andStreetInfraction}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("COLONIA: ${SingletonTicket.colonyInfraction}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("MUNICIPIO: ${Application.prefs?.loadData(R.string.sp_township_name, "")}\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))

            // Datos de infracción
            printlines.add(getPrintLine("DOCUMENTO QUE SE RETIENE:", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine(SingletonTicket.retainedDocumentInfraction, TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("REMISIÓN DEL VEHÍCULO: ${if (SingletonTicket.isRemitedInfraction) "SI" else "NO"}", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))
            printlines.add(getPrintLine("${SingletonTicket.remitedDispositionInfraction}\n", TextPrintLine.FONT_NORMAL, PrintLine.LEFT, false))

            //Responsable del vehículo
            printlines.add(getPrintLine("RESPONSABLE DEL VEHÍCULO:", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, true))
            printlines.add(getPrintLine("${SingletonTicket.completeNameOffender}\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("RECIBO DE CONFORMIDAD\n\n\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("FIRMA\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("AGENTE:", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine(SingletonTicket.nameAgent, TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("EMPLEADO: ${SingletonTicket.noEmployee}\n\n\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("FIRMA\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))

            // Referencia de pago
            if (SingletonTicket.paymentAuthCode.isNotEmpty()) {
                if (!SingletonTicket.isPaymentCoDi)
                    printlines.add(getPrintLine("PAGO C/N TARJETA, AUTORIZACIÓN:", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, true))
                else
                    printlines.add(getPrintLine("PAGO C/N CODI, CLAVE RASTREO:", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, true))
                printlines.add(getPrintLine("${SingletonTicket.paymentAuthCode}\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            }

            // Descuentos
            SingletonTicket.captureLineList.forEach {
                printlines.add(getPrintBarCode(it.captureLine))
                printlines.add(getPrintLine("${it.captureLine}", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
                printlines.add(getPrintLine("${it.labelDiscount}", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
                printlines.add(getPrintLine("VIGENCIA: ${it.expirationDiscount}", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
                printlines.add(getPrintLine("IMPORTE: ${it.importInfraction}\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            }

            // Pie de página
            printlines.add(getPrintLine("QSISTEMAS XI", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("QSISTEMAS XII", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("QSISTEMAS XIII\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("-AVISO DE PRIVACIDAD-\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))
            printlines.add(getPrintLine("FUENTE DE CAPTURA: SIIP\n\n\n", TextPrintLine.FONT_NORMAL, PrintLine.CENTER, false))

            PaymentsTransfer.print(activity, printlines, null, object : IPaymentsTransfer.PrintListener {
                override fun onStart() {
                }

                override fun onError(var1: Int, var2: String) {
                    SingletonTicket.cleanData()
                    listener.onTicketError()
                }

                override fun onFinish() {
                    SingletonTicket.cleanData()
                    listener.onTicketPrint()
                }
            })
        }

        private fun getPrintLine(text: String, size: Int, position: Int, bold: Boolean): PrintLine {
            val printline = TextPrintLine()
            printline.type = PrintLine.TEXT
            printline.size = size
            printline.position = position
            printline.isBold = bold
            printline.content = text
            return printline
        }

        private fun getPrintBarCode(text: String): PrintLine {
            val printline = BitmapPrintLine()
            printline.type = PrintLine.BITMAP
            printline.position = PrintLine.CENTER
            printline.bitmap = QRUtil.getBarcodeBMP(text, 380, 120)
            return printline
        }
    }

    interface TicketListener {
        fun onTicketPrint()
        fun onTicketError()
    }
}