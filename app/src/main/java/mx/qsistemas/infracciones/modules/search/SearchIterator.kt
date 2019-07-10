package mx.qsistemas.infracciones.modules.search

import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import com.google.gson.Gson
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.InfractionItem
import mx.qsistemas.infracciones.db.entities.IdentifierDocument
import mx.qsistemas.infracciones.db.entities.InfractionLocal
import mx.qsistemas.infracciones.db.entities.NonWorkingDay
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.db.managers.SearchManager
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.net.catalogs.InfractionSearch
import mx.qsistemas.infracciones.net.catalogs.ServiceResponse
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.Ticket
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchIterator(private val listener: SearchContracts.Presenter) : SearchContracts.Iterator {

    internal lateinit var identifierDocList: MutableList<IdentifierDocument>
    private lateinit var nonWorkingDays: MutableList<NonWorkingDay>

    private var itemInfraOnline: MutableList<InfractionList.Results> = ArrayList()
    private var itemInfraOffLine: MutableList<InfractionItem> = ArrayList()

    //Para la impresión de la boleta
    private val actualDay = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
    private var fifteenthDay = ""
    private var thirtythDay = ""
    private var newFolio = ""
    private var captureLine1 = ""
    private var captureLine2 = ""


    override fun getDocIdentAdapter(): ArrayAdapter<NewIdentDocument> {
        identifierDocList = CatalogsAdapterManager.getIdentifierDocList()
        val newFilterDocList: MutableList<NewIdentDocument> = ArrayList()

        identifierDocList.forEach { document ->
            val newDoc = NewIdentDocument()

            if (document.id == 0) {
                newDoc.id = document.id
                newDoc.value = "Seleccionar ..."

            } else {

                newDoc.id = document.id
                newDoc.value = document.document
            }
            newFilterDocList.add(newDoc)

        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, newFilterDocList)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun doSearchByFilter(id: Int, filter: String) {
        val rootObject = JSONObject()
        rootObject.put("username", "InfraMobile")
        rootObject.put("password", "CF2E3EF25C90EB567243ADFACD4AA868")
        if (id == 0) {
            rootObject.put("Folio", filter)
            rootObject.put("IdDocumentoIdentificador", 0)
            rootObject.put("NumeroDocumentoIdentificador", "")
        } else {
            rootObject.put("Folio", "")
            rootObject.put("IdDocumentoIdentificador", id)
            rootObject.put("NumeroDocumentoIdentificador", filter)
        }
        Log.d("JSON-SEARCH", rootObject.toString())
        NetworkApi().getNetworkService().doSearchByFilter(rootObject.toString()).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = Gson().fromJson(response.body(), InfractionList::class.java)
                    itemInfraOnline = data.results
                    when {
                        data.flag -> listener.onResultSearch(itemInfraOnline)
                        data.message.contains("No se encontraron") -> listener.onResultSearch(data.results)
                        else -> listener.onError(data.message)
                    }
                    Log.d("SEARCH ---->>>>>", data.toString())

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("SEARCH ---->>>>>", t.message.toString())
                listener.onError(t.message ?: "")
            }

        })
    }

    override fun doSearchByIdInfraction(id: String, origin: Int) {
        val rootObject = JSONObject()
        rootObject.put("IdInfraccion", id)
        rootObject.put("username", "InfraMobile")
        rootObject.put("password", "CF2E3EF25C90EB567243ADFACD4AA868")
        Log.d("JSON-SEARCH", rootObject.toString())
        NetworkApi().getNetworkService().doSearchByIdInfraction(rootObject.toString()).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = Gson().fromJson(response.body(), InfractionSearch::class.java)
                    listener.onResultInfractionById(data, origin)
                    Log.d("SEARCH_BY_ID", data.toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("SEARCH_BY_ID", t.message.toString())
                listener.onError(t.message ?: "")
            }
        })
    }

    override fun savePaymentToService(idInfraction: String, txInfo: TransactionInfo, amount: String, discount: String, totalPayment: String, idPerson: Long) {
        val rootObj = JSONObject()
        val jPayment = JSONObject()
        val jPaymentCard = JSONObject()
        val idRegUser = Application.prefs?.loadDataInt(R.string.sp_id_township_person)!!.toLong()
        //val totalPayment =totalToPay

        rootObj.put("IdInfraccion", idInfraction)
        rootObj.put("username", "InfraMobile")
        rootObj.put("password", "CF2E3EF25C90EB567243ADFACD4AA868")

        jPaymentCard.put("aid", txInfo.aid)
        jPaymentCard.put("app_label", txInfo.appLabel)
        jPaymentCard.put("arqc", txInfo.arqc)
        jPaymentCard.put("auth_nb", txInfo.authorization)
        jPaymentCard.put("entry_type", txInfo.entryType)
        jPaymentCard.put("masked_pan", txInfo.maskedPan)
        jPaymentCard.put("trx_date", txInfo.txDate)
        jPaymentCard.put("trx_nb", "")
        jPaymentCard.put("trx_time", txInfo.txTime)
        jPaymentCard.put("serial_payda", "")
        jPaymentCard.put("id_registro_usuario", idRegUser.toString())
        jPaymentCard.put("afiliacion", txInfo.affiliation)
        jPaymentCard.put("vigencia_tarjeta", txInfo.expirationDate)
        jPaymentCard.put("mensaje", "Aprobado")
        jPaymentCard.put("tipo_tarjeta", txInfo.brandCard)
        jPaymentCard.put("tipo", txInfo.typeCard)
        jPaymentCard.put("banco_emisor", txInfo.bank)
        jPaymentCard.put("referencia", txInfo.reference)
        jPaymentCard.put("importe", totalPayment)
        jPaymentCard.put("tvr", txInfo.tvr)
        jPaymentCard.put("tsi", txInfo.tsi)
        jPaymentCard.put("numero_control", txInfo.noControl)
        jPaymentCard.put("tarjetahabiente", txInfo.cardOwner)
        jPaymentCard.put("emv_data", "")
        jPaymentCard.put("tipo_transaccion", txInfo.typeTx)
        rootObj.put("paymentCard", jPaymentCard)

        jPayment.put("id_forma_pago", 2)
        jPayment.put("subtotal", amount)
        jPayment.put("descuento", discount)
        jPayment.put("total", totalPayment)
        jPayment.put("folio", txInfo.authorization)
        jPayment.put("observacion", "")
        jPayment.put("id_registro_usuario", idPerson)
        rootObj.put("payment", jPayment)
        Log.d("JSON-SAVE-PAYMENT", rootObj.toString())
        NetworkApi().getNetworkService().savePayment(rootObj.toString()).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = Gson().fromJson(response.body(), ServiceResponse::class.java)
                    listener.onResultSavePayment(data.message, data.flag)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                listener.onError(t.message ?: "")
            }
        })
    }

    override fun doSearchByFilterOffLine(id: Int, filter: String) {
        itemInfraOffLine = SearchManager.getItemInfraction()
        if (itemInfraOffLine.size > 0) {
            listener.onResultSearchOffLine(itemInfraOffLine)
        } else {
            listener.onError("Error al obtener datos locales.")
        }
    }

    override fun printTicket(activity: Activity) {

        SingletonTicket.dateTicket = SingletonInfraction.dateInfraction
        SingletonTicket.folioTicket = SingletonInfraction.folioInfraction
        SingletonTicket.completeNameOffender = SingletonInfraction.nameOffender + " " + SingletonInfraction.lastFatherName + " " + SingletonInfraction.lastMotherName
        if (SingletonInfraction.rfcOffenfer.isNotEmpty()) {
            SingletonTicket.rfcOffender = SingletonInfraction.rfcOffenfer
        }
        if (SingletonInfraction.noExtOffender.isNotEmpty()) {
            SingletonTicket.noExtOffender = SingletonInfraction.noExtOffender
        }
        if (SingletonInfraction.noIntOffender.isNotEmpty()) {
            SingletonTicket.noIntOffender = SingletonInfraction.noIntOffender
        }
        if (SingletonInfraction.colonyOffender.isNotEmpty()) {
            SingletonTicket.colonyOffender = SingletonInfraction.colonyOffender
        }
        if (SingletonInfraction.stateOffender.id != 0) {
            SingletonTicket.stateOffender = SingletonInfraction.stateOffender.value
        }
        if (SingletonInfraction.noCirculationCard.isNotEmpty()) {
            SingletonTicket.noLicenseOffender = SingletonInfraction.noCirculationCard
        }
        if (SingletonInfraction.typeLicenseOffender.id != 0) {
            SingletonTicket.typeLicenseOffender = SingletonInfraction.typeLicenseOffender.license_type
        }
        if (SingletonInfraction.licenseIssuedInOffender.id != 0) {
            SingletonTicket.stateLicenseOffender = SingletonInfraction.licenseIssuedInOffender.value
        }
        SingletonTicket.brandVehicle = SingletonInfraction.brandVehicle.vehicle_brand
        if (SingletonInfraction.subBrandVehicle.isNotEmpty()) {
            SingletonTicket.subBrandVehicle = SingletonInfraction.subBrandVehicle
        }
        SingletonTicket.typeVehicle = SingletonInfraction.typeVehicle.type_string
        SingletonTicket.colorVehicle = SingletonInfraction.colorVehicle
        SingletonTicket.modelVehicle = SingletonInfraction.yearVehicle
        SingletonTicket.identifierVehicle = SingletonInfraction.identifierDocument.document
        SingletonTicket.noIdentifierVehicle = SingletonInfraction.noDocument
        SingletonTicket.expeditionAuthVehicle = SingletonInfraction.typeDocument.authority
        SingletonTicket.stateExpVehicle = SingletonInfraction.stateIssuedIn.value
        SingletonInfraction.motivationList.forEach { art ->
            val article = SingletonTicket.ArticleFraction(art.article.article, art.fraction.fraccion, art.fraction.minimum_wages.toString(),
                    art.fraction.penalty_points.toString(), art.motivation)
            SingletonTicket.fractionsList.add(article)
        }
        SingletonTicket.streetInfraction = SingletonInfraction.streetInfraction
        SingletonTicket.betweenStreetInfraction = SingletonInfraction.betweenStreet1
        SingletonTicket.andStreetInfraction = SingletonInfraction.betweenStreet2
        SingletonTicket.colonyInfraction = SingletonInfraction.colonnyInfraction
        SingletonTicket.retainedDocumentInfraction = SingletonInfraction.retainedDocument.document
        SingletonTicket.isRemitedInfraction = SingletonInfraction.isRemited
        if (SingletonInfraction.isRemited) {
            SingletonTicket.remitedDispositionInfraction = SingletonInfraction.dispositionRemited.disposition
        }
        SingletonTicket.captureLineList.add(SingletonTicket.CaptureLine(captureLine1, "CON 50% DE DESCUENTO", fifteenthDay, SingletonInfraction.totalInfraction))
        SingletonTicket.captureLineList.add(SingletonTicket.CaptureLine(captureLine2, "SIN DESCUENTO", thirtythDay, SingletonInfraction.subTotalInfraction))
        Ticket.printTicket(activity, object : Ticket.TicketListener {
            override fun onTicketPrint() {
                listener.onTicketPrinted()
            }

            override fun onTicketError() {
                listener.onError(Application.getContext().getString(R.string.pt_e_print_other_error))
            }
        })
    }

    override fun doSearchByIdInfractionOffLine(id: String, origin: Int) {

        val infraction = SearchManager.getInfractionById(id.toLong())
        val infraFracc = SearchManager.getInfraFracc(id.toLong())
        listener.onResultInfractionByIdOffline(infraction, infraFracc, origin)
    }
}

class NewIdentDocument {
    var id: Int = 0
    var value: String = ""

    override fun toString(): String {
        return value
    }
}
