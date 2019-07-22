package mx.qsistemas.infracciones.utils

import android.app.Activity
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.managers.PrintTicketManager
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Ticket {
    companion object {

        fun printTicket(activity: Activity, listener: TicketListener) {
            val printJson = JSONObject()
            val printTest = JSONArray()

            val config = PrintTicketManager.getConfig()
            val header1 = "${config.header_print_1}\n"
            val header2 = "${config.header_print_2}\n"
            val header3 = "${config.header_print_3}\n"
            val header4 = "${config.header_print_4}\n"
            val header5 = "${config.header_print_5}\n"
            val header6 = "${config.header_print_6}\n"

            //Impresión de encabezados
            printTest.put(getPrintObject(header1 + header2 + header3 + header4 + header5 + header6, "2", "center", "1"))
            printTest.put(getPrintObject("\n\nDETECCIÓN   Y   LEVANTAMIENTO", "2", "center", "0"))
            printTest.put(getPrintObject("ELECTRÓNICO     DE     INFRACCIO-", "2", "center", "0"))
            printTest.put(getPrintObject("NES  A CONDUCTORES   DE   VEHÍ-", "2", "center", "0"))
            printTest.put(getPrintObject("CULOS     QUE     CONTRAVENGAN", "2", "center", "0"))
            printTest.put(getPrintObject("LAS   DISPOSICIONES    EN   MATE-", "2", "center", "0"))
            printTest.put(getPrintObject("RIA  DE  TRÁNSITO, EQUILIBRIO  E-", "2", "center", "0"))
            printTest.put(getPrintObject("COLÓGICO       PROTECCIÓN       AL", "2", "center", "0"))
            printTest.put(getPrintObject("AMBIENTE    Y     PARA    LA    PRE-", "2", "left", "0"))
            printTest.put(getPrintObject("VENCIÓN    Y    CONTROL    DE    LA", "2", "left", "0"))
            printTest.put(getPrintObject("CONTAMINACIÓN,   ASI   COMO  PA-", "2", "left", "0"))
            printTest.put(getPrintObject("GO   DE   SANCIONES    Y    APLICA-", "2", "left", "0"))
            printTest.put(getPrintObject("CIÓN    DE   MEDIDAS   DE   SEGURI-", "2", "left", "0"))
            printTest.put(getPrintObject("DAD.", "2", "left", "0"))
            printTest.put(getPrintObject("\n\n", "2", "left", "0"))
            printTest.put(getPrintObject("EL C. AGENTE  QUE  SUSCRIBE  LA", "2", "left", "0"))
            printTest.put(getPrintObject("PRESENTE      BOLETA       DE      IN-", "2", "left", "0"))
            printTest.put(getPrintObject("FRACCIÓN,  ESTA  FACULTADO   EN", "2", "left", "0"))
            printTest.put(getPrintObject("TÉRMINOS   DE   LOS  QUE  SE   ES-", "2", "left", "0"))
            printTest.put(getPrintObject("TABLECE     EN     LOS   ARTÍCULOS", "2", "left", "0"))
            printTest.put(getPrintObject("21   Y  115,  FRACCIÓN  III,   INCISO,", "2", "left", "0"))
            printTest.put(getPrintObject("H)   DE   LA  CONSTITUCION    POLÍ-", "2", "left", "0"))
            printTest.put(getPrintObject("TICA   DE  LOS  ESTADOS   UNIDOS", "2", "left", "0"))
            printTest.put(getPrintObject("MEXICANOS     DE     ACUERDO     A", "2", "left", "0"))
            printTest.put(getPrintObject("LO   ESTABLECIDO   EN   LOS   ARTÍ-", "2", "left", "0"))
            printTest.put(getPrintObject("CULOS   8.3,  8.10,  8.18,  8.19   BIS,", "2", "left", "0"))
            printTest.put(getPrintObject("8.19   TERCERO   Y    8.19   CUARTO", "2", "left", "0"))
            printTest.put(getPrintObject("DEL     CÓDIGO     ADMINISTRATIVO", "2", "left", "0"))
            printTest.put(getPrintObject("DEL    ESTADO    DE    MÉXICO.  ASÍ", "2", "left", "0"))
            printTest.put(getPrintObject("COMO HACER  CONSTAR  LOS  HE-", "2", "left", "0"))
            printTest.put(getPrintObject("CHOS     QUE     MOTIVAN    LA    IN", "2", "left", "0"))
            printTest.put(getPrintObject("FRACCIÓN    EN    TÉRMINOS    DEL", "2", "left", "0"))
            printTest.put(getPrintObject("ARTÍCULO   16  DE NUESTRA CAR-", "2", "left", "0"))
            printTest.put(getPrintObject("TA MAGNA.\n\n", "2", "left", "0"))
            printTest.put(getPrintObject("${SingletonTicket.dateTicket}\n\n", "2", "center", "1"))
            printTest.put(getPrintObject("FOLIO: ${SingletonTicket.folioTicket}\n\n", "2", "right", "0"))

            //Datos del infractor
            printTest.put(getPrintObject("DATOS DEL INFRACTOR:\n\n", "2", "left", "1"))
            printTest.put(getPrintObject("${SingletonTicket.completeNameOffender}\n", "2", "center", "0"))
            printTest.put(getPrintObject("${SingletonTicket.rfcOffender}\n\n", "2", "center", "0"))
            if (SingletonTicket.streetOffender != "-")
                printTest.put(getPrintObject("DOMICILIO: ${SingletonTicket.streetOffender}\n", "2", "left", "0"))
            if (SingletonTicket.noExtOffender != "-")
                printTest.put(getPrintObject("EXTERIOR: ${SingletonTicket.noExtOffender}\n", "2", "left", "0"))
            if (SingletonTicket.noIntOffender != "-")
                printTest.put(getPrintObject("INTERIOR: ${SingletonTicket.noIntOffender}\n", "2", "left", "0"))
            if (SingletonTicket.colonyOffender != "-")
                printTest.put(getPrintObject("COLONIA: ${SingletonTicket.colonyOffender}\n", "2", "left", "0"))
            if (SingletonTicket.stateOffender != "-")
                printTest.put(getPrintObject("ENTIDAD: ${SingletonTicket.stateOffender}\n\n", "2", "left", "0"))

            // Datos de licencia
            if (SingletonTicket.noLicenseOffender != "-")
                printTest.put(getPrintObject("LICENCIA/PERMISO: ${SingletonTicket.noLicenseOffender}\n", "2", "left", "0"))
            if (SingletonTicket.typeLicenseOffender != "-")
                printTest.put(getPrintObject("TIPO LICENCIA: ${SingletonTicket.typeLicenseOffender}\n", "2", "left", "0"))
            if (SingletonTicket.stateLicenseOffender != "-")
                printTest.put(getPrintObject("EXPEDIDA: ${SingletonTicket.stateLicenseOffender}\n\n", "2", "left", "0"))
            else
                printTest.put(getPrintObject("\n\n", "2", "left", "0"))

            // Características del vehículo
            printTest.put(getPrintObject("CARACTERÍSTICAS DEL VEHÍCULO:\n\n", "2", "left", "1"))
            printTest.put(getPrintObject("MARCA: ${SingletonTicket.brandVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("SUBMARCA: ${SingletonTicket.subBrandVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("TIPO: ${SingletonTicket.typeVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("COLOR: ${SingletonTicket.colorVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("MODELO: ${SingletonTicket.modelVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("IDENTIFICADOR: ${SingletonTicket.identifierVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("NÚMERO: ${SingletonTicket.noIdentifierVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("AUTORIDAD QUE EXPIDE: ${SingletonTicket.expeditionAuthVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("EXPEDIDO: ${SingletonTicket.stateExpVehicle}\n\n", "2", "left", "0"))

            // Artículos y Fracciones
            printTest.put(getPrintObject("ARTÍCULOS  DEL  REGLAMENTO\n", "2", "center", "1"))
            printTest.put(getPrintObject("DE  TRÁNSITO DEL ESTADO  DE\n", "2", "center", "1"))
            printTest.put(getPrintObject("MÉXICO\n\n", "2", "center", "1"))
            printTest.put(getPrintObject("ARTÍCULO/FRACCIÓN\t\t\t\t\t\t\t\tU.M.A.\t\t\t\t\t\t\t\tPUNTOS\n", "1", "center", "0"))
            printTest.put(getPrintObject("*******************************\n", "2", "center", "0"))
            SingletonTicket.fractionsList.forEach {
                printTest.put(getPrintObject("${it.article} / ${it.fraction}\t\t\t\t\t\t${it.umas}\t\t\t\t\t${it.points}\n\n", "2", "center", "0"))
                printTest.put(getPrintObject("CONDUCTA   QUE   MOTIVA   LA   INFRACCIÓN\n\n", "1", "center", "1"))
                printTest.put(getPrintObject("${it.motivation}\n\n", "1", "left", "0"))
            }

            // Dirección de infracción
            printTest.put(getPrintObject("CALLE: ${SingletonTicket.streetInfraction}\n", "2", "left", "0"))
            printTest.put(getPrintObject("ENTRE: ${SingletonTicket.betweenStreetInfraction}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Y: ${SingletonTicket.andStreetInfraction}\n", "2", "left", "0"))
            printTest.put(getPrintObject("COLONIA: ${SingletonTicket.colonyInfraction}\n", "2", "left", "0"))
            printTest.put(getPrintObject("MUNICIPIO: ${Application.prefs?.loadData(R.string.sp_township_name, "")}\n\n", "2", "left", "0"))

            // Datos de infracción
            printTest.put(getPrintObject("DOCUMENTO QUE SE RETIENE:\n", "2", "left", "0"))
            printTest.put(getPrintObject("${SingletonTicket.retainedDocumentInfraction}\n\n", "2", "left", "0"))
            printTest.put(getPrintObject("REMISIÓN DEL VEHÍCULO: ${if (SingletonTicket.isRemitedInfraction) "SI" else "NO"}\n", "2", "left", "0"))
            printTest.put(getPrintObject("${SingletonTicket.remitedDispositionInfraction}\n\n", "2", "left", "0"))

            //Responsable del vehículo
            printTest.put(getPrintObject("RESPONSABLE DEL VEHÍCULO:\n", "2", "center", "1"))
            printTest.put(getPrintObject("${SingletonTicket.completeNameOffender}\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("RECIBO DE CONFORMIDAD\n\n\n\n\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("FIRMA\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("AGENTE:\n", "2", "center", "0"))
            printTest.put(getPrintObject("${SingletonTicket.nameAgent}\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("EMPLEADO: ${Application.prefs?.loadData(R.string.sp_no_employee, "")}\n\n\n\n\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("FIRMA\n\n", "2", "center", "0"))

            // Referencia de pago
            if (SingletonTicket.paymentAuthCode.isNotEmpty()) {
                printTest.put(getPrintObject("PAGO C/N TARJETA, AUTORIZACIÓN:\n", "2", "center", "1"))
                printTest.put(getPrintObject("${SingletonTicket.paymentAuthCode}\n\n\n\n", "2", "center", "0"))
            } else {
                printTest.put(getPrintObject("\n\n\n\n", "2", "center", "0"))
            }

            // Descuentos
            SingletonTicket.captureLineList.forEach {
                printTest.put(getPrintBarCode(it.captureLine))
                printTest.put(getPrintObject("${it.captureLine}\n", "2", "center", "0"))
                printTest.put(getPrintObject("${it.labelDiscount}\n", "2", "center", "0"))
                printTest.put(getPrintObject("VIGENCIA: ${it.expirationDiscount}\n", "2", "center", "0"))
                printTest.put(getPrintObject("IMPORTE: ${it.importInfraction}\n\n\n\n", "2", "center", "0"))
            }

            // Pie de página
            printTest.put(getPrintObject("QSISTEMAS XI\n", "2", "center", "0"))
            printTest.put(getPrintObject("QSISTEMAS XII\n", "2", "center", "0"))
            printTest.put(getPrintObject("QSISTEMAS XIII\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("-AVISO DE PRIVACIDAD-\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("FUENTE DE CAPTURA: SIIP\n\n\n\n\n", "2", "center", "0"))

            printJson.put("spos", printTest)
            PaymentsTransfer.print(activity, printJson.toString(), null, object : IPaymentsTransfer.PrintListener {
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

        fun getPrintObject(text: String, size: String, position: String, bold: String): JSONObject {
            val json = JSONObject()
            try {
                json.put("content-type", "txt")
                json.put("content", text)
                json.put("size", size)
                json.put("position", position)
                json.put("offset", "0")
                json.put("bold", bold)
                json.put("italic", "0")
                json.put("height", "-1")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return json
        }

        fun getPrintBarCode(text: String): JSONObject {
            val json = JSONObject()
            try {
                json.put("content-type", "one-dimension")
                json.put("content", text)
                json.put("size", "3")
                json.put("position", "center")
                json.put("offset", "0")
                json.put("bold", "0")
                json.put("italic", "0")
                json.put("height", "2")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return json
        }
    }

    interface TicketListener {
        fun onTicketPrint()
        fun onTicketError()
    }
}