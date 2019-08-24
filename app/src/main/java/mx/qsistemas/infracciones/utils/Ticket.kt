package mx.qsistemas.infracciones.utils

import android.app.Activity
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
            val printJson = JSONObject()
            val printTest = JSONArray()

            //Impresión de encabezados
            SingletonTicket.headers.forEach {
                printTest.put(getPrintObject(it, "2", "center", "1"))
            }
            printTest.put(getPrintObject("\n\nDetección y levantamiento electróni-", "2", "center", "0"))
            printTest.put(getPrintObject("co de  infracciones a  conductores de", "2", "center", "0"))
            printTest.put(getPrintObject("vehiculos  que  contravengan  las  dis-", "2", "center", "0"))
            printTest.put(getPrintObject("posiciones  en  materia de  tránsito, e-", "2", "center", "0"))
            printTest.put(getPrintObject("quilibrio  ecológico, protección  al am-", "2", "center", "0"))
            printTest.put(getPrintObject("biente  y  para la prevención y control", "2", "center", "0"))
            printTest.put(getPrintObject("de la  contaminación,  así  como pago", "2", "center", "0"))
            printTest.put(getPrintObject("de sanciones y  aplicación de medidas", "2", "center", "0"))
            printTest.put(getPrintObject("de seguridad.", "2", "left", "0"))
            printTest.put(getPrintObject("\n\n", "2", "left", "0"))
            printTest.put(getPrintObject("El C. Agente que  suscribe  la presente", "2", "left", "0"))
            printTest.put(getPrintObject("boleta  de  infracción,  está  facultado", "2", "left", "0"))
            printTest.put(getPrintObject("en términos  de  los  que se  establece", "2", "left", "0"))
            printTest.put(getPrintObject("en los artículos  21 y  115,   Fracción III,", "2", "left", "0"))
            printTest.put(getPrintObject("inciso H) de la Constitución Política de", "2", "left", "0"))
            printTest.put(getPrintObject("los  Estados  Unidos  Mexicanos de  a-", "2", "left", "0"))
            printTest.put(getPrintObject("cuerdo a lo establecido  en  los artícu-", "2", "left", "0"))
            printTest.put(getPrintObject("los 8.3, 8.10,  8.18,  8.19 BIS,  8.19  Terce-", "2", "left", "0"))
            printTest.put(getPrintObject("ro y 8.19  Cuarto  del Código  Adminis-", "2", "left", "0"))
            printTest.put(getPrintObject("trativo del Estado de México.Así como", "2", "left", "0"))
            printTest.put(getPrintObject("hacer constar los hechos que motivan", "2", "left", "0"))
            printTest.put(getPrintObject("la infracción en términos  del  artículo", "2", "left", "0"))
            printTest.put(getPrintObject("16 de nuestra carta magna.\n\n", "2", "left", "0"))
            printTest.put(getPrintObject("${SingletonTicket.dateTicket}\n\n", "2", "center", "1"))
            printTest.put(getPrintObject("Folio: ${SingletonTicket.folioTicket}\n\n", "2", "right", "0"))

            //Datos del infractor
            printTest.put(getPrintObject("Datos del infractor:\n\n", "2", "left", "1"))
            printTest.put(getPrintObject("${SingletonTicket.completeNameOffender}\n", "2", "center", "0"))
            printTest.put(getPrintObject("${SingletonTicket.rfcOffender}\n\n", "2", "center", "0"))
            if (SingletonTicket.streetOffender != "-")
                printTest.put(getPrintObject("Domicilio: ${SingletonTicket.streetOffender}\n", "2", "left", "0"))
            if (SingletonTicket.noExtOffender != "-")
                printTest.put(getPrintObject("Exterior: ${SingletonTicket.noExtOffender}\n", "2", "left", "0"))
            if (SingletonTicket.noIntOffender != "-")
                printTest.put(getPrintObject("Interior: ${SingletonTicket.noIntOffender}\n", "2", "left", "0"))
            if (SingletonTicket.colonyOffender != "-")
                printTest.put(getPrintObject("Colonia: ${SingletonTicket.colonyOffender}\n", "2", "left", "0"))
            if (SingletonTicket.stateOffender != "-")
                printTest.put(getPrintObject("Entidad: ${SingletonTicket.stateOffender}\n\n", "2", "left", "0"))

            // Datos de licencia
            if (SingletonTicket.noLicenseOffender != "-")
                printTest.put(getPrintObject("Licencia/Permiso: ${SingletonTicket.noLicenseOffender}\n", "2", "left", "0"))
            if (SingletonTicket.typeLicenseOffender != "-")
                printTest.put(getPrintObject("Tipo de licencia: ${SingletonTicket.typeLicenseOffender}\n", "2", "left", "0"))
            if (SingletonTicket.stateLicenseOffender != "-")
                printTest.put(getPrintObject("Expedida: ${SingletonTicket.stateLicenseOffender}\n\n", "2", "left", "0"))
            else
                printTest.put(getPrintObject("\n\n", "2", "left", "0"))

            // Características del vehículo
            printTest.put(getPrintObject("Características del vehículo:\n\n", "2", "left", "1"))
            printTest.put(getPrintObject("Marca: ${SingletonTicket.brandVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Submarca: ${SingletonTicket.subBrandVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Tipo: ${SingletonTicket.typeVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Color: ${SingletonTicket.colorVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Modelo: ${SingletonTicket.modelVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Identificador: ${SingletonTicket.identifierVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Número: ${SingletonTicket.noIdentifierVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Autoridad que expide: ${SingletonTicket.expeditionAuthVehicle}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Expedido: ${SingletonTicket.stateExpVehicle}\n\n", "2", "left", "0"))

            // Artículos y Fracciones
            printTest.put(getPrintObject("Artículos del reglamento\n", "2", "center", "1"))
            printTest.put(getPrintObject("de  tránsito del Estado  de\n", "2", "center", "1"))
            printTest.put(getPrintObject("México\n\n", "2", "center", "1"))
            printTest.put(getPrintObject("Artículo/Fracción\t\t\t\t\t\t\tU.M.A.\n", "2", "center", "0"))
            printTest.put(getPrintObject("*******************************\n", "2", "center", "0"))
            SingletonTicket.fractionsList.forEach {
                printTest.put(getPrintObject("${it.article} / ${it.fraction}\t\t\t\t\t\t\t\t\t\t\t${it.umas}\n\n", "2", "center", "0"))
                printTest.put(getPrintObject("Conducta que motiva la infracción\n\n", "2", "center", "1"))
                printTest.put(getPrintObject("${it.motivation}\n\n", "2", "left", "0"))
            }

            // Dirección de infracción
            printTest.put(getPrintObject("Calle: ${SingletonTicket.streetInfraction}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Entre: ${SingletonTicket.betweenStreetInfraction}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Y: ${SingletonTicket.andStreetInfraction}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Colonia: ${SingletonTicket.colonyInfraction}\n", "2", "left", "0"))
            printTest.put(getPrintObject("Municipio: ${Application.prefs?.loadData(R.string.sp_township_name, "")}\n\n", "2", "left", "0"))

            // Datos de infracción
            printTest.put(getPrintObject("Documento que se retiene:\n", "2", "left", "0"))
            printTest.put(getPrintObject("${SingletonTicket.retainedDocumentInfraction}\n\n", "2", "left", "0"))
            printTest.put(getPrintObject("Remisión del vehículo: ${if (SingletonTicket.isRemitedInfraction) "Sí" else "No"}\n", "2", "left", "0"))
            printTest.put(getPrintObject("${SingletonTicket.remitedDispositionInfraction}\n\n", "2", "left", "0"))

            //Responsable del vehículo
            printTest.put(getPrintObject("Responsable del vehículo:\n", "2", "center", "1"))
            printTest.put(getPrintObject("${SingletonTicket.completeNameOffender}\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("Recibo de conformidad\n\n\n\n\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("Firma\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("Agente:\n", "2", "center", "1"))
            printTest.put(getPrintObject("${SingletonTicket.nameAgent}\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("Empleado: ${SingletonTicket.idAgent}\n\n\n\n\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("Firma\n\n", "2", "center", "0"))

            // Referencia de pago
            if (SingletonTicket.paymentAuthCode.isNotEmpty()) {
                printTest.put(getPrintObject("Pago c/n tarjeta, autorización:\n", "2", "center", "1"))
                printTest.put(getPrintObject("${SingletonTicket.paymentAuthCode}\n\n\n\n", "2", "center", "0"))
            } else {
                printTest.put(getPrintObject("\n\n\n\n", "2", "center", "0"))
            }

            // Descuentos
            SingletonTicket.captureLines.forEach {
                printTest.put(getPrintBarCode(it.captureLine))
                printTest.put(getPrintObject("${it.captureLine}\n", "2", "center", "0"))
                printTest.put(getPrintObject("${it.labelDiscount}\n", "2", "center", "0"))
                printTest.put(getPrintObject("Vigencia: ${it.expirationDiscount}\n", "2", "center", "0"))
                printTest.put(getPrintObject("Importe: ${it.importInfraction}\n\n\n", "2", "center", "0"))
            }

            // Pie de página
            SingletonTicket.footers.forEach {
                printTest.put(getPrintObject(it, "2", "center", "0"))
            }
            printTest.put(getPrintObject("\n\n-Aviso de Privacidad-\n\n", "2", "center", "0"))
            printTest.put(getPrintObject("Fuente de captura: SIIP\n\n\n\n\n", "2", "center", "0"))
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