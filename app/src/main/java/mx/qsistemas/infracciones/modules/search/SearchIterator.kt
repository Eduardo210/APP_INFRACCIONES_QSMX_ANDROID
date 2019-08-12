package mx.qsistemas.infracciones.modules.search

import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.IdentifierDocument
import mx.qsistemas.infracciones.db.entities.NonWorkingDay
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.db_web.entities.InfractionItemList
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.db_web.managers.SearchManagerWeb
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.FS_COL_COLORS
import mx.qsistemas.infracciones.utils.FS_COL_INSURED_DOC
import mx.qsistemas.infracciones.utils.FS_COL_MODELS
import mx.qsistemas.infracciones.utils.Ticket
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchIterator(private val listener: SearchContracts.Presenter) : SearchContracts.Iterator {

    internal lateinit var identifierDocList: MutableList<IdentifierDocument>
    private lateinit var nonWorkingDays: MutableList<NonWorkingDay>

    private var itemInfraOnline: MutableList<InfractionList.Results> = ArrayList()
    private var itemInfraOffLine: MutableList<mx.qsistemas.infracciones.db_web.entities.InfractionItem> = ArrayList()
    private var itemInfraOffLineList: MutableList<InfractionItemList> = ArrayList()

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
        /* NetworkApi().getNetworkService().doSearchByFilter(rootObject.toString()).enqueue(object : Callback<String> {
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

         })*/
    }

    override fun doSearchByIdInfraction(id: String, origin: Int) {
        /* val rootObject = JSONObject()
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
         })*/
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
        jPaymentCard.put("app_label", txInfo.apn)
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

        jPayment.put("id_forma_pago", 1)
        jPayment.put("subtotal", amount)
        jPayment.put("descuento", discount)
        jPayment.put("total", totalPayment)
        jPayment.put("folio", txInfo.authorization)
        jPayment.put("observacion", "")
        jPayment.put("id_registro_usuario", idPerson)
        rootObj.put("payment", jPayment)
        Log.d("JSON-SAVE-PAYMENT", rootObj.toString())
        /* NetworkApi().getNetworkService().savePayment(idInfraction.toLong(), rootObj.toString()).enqueue(object : Callback<String> {
             override fun onResponse(call: Call<String>, response: Response<String>) {
                 if (response.code() == HttpURLConnection.HTTP_OK) {
                     val data = Gson().fromJson(response.body(), ServiceResponse::class.java)
                     listener.onResultSavePayment(data.message, data.flag)
                 }
             }

             override fun onFailure(call: Call<String>, t: Throwable) {
                 listener.onError(t.message ?: "")
             }
         })*/
    }

    override suspend fun doSearchByFilterOffLine(id: Int, filter: String) {
        var insuredDocument = ""
        var brand = ""
        var model = ""
        var colour = ""

        /*Empiezo con la nueva búsqueda*/
        val query = when (id) {
            0 -> {
                if (filter.isBlank()) { //Traer el último registro
                    SimpleSQLiteQuery("SELECT infra.id, " +
                            "infra.folio, " +
                            "infra.date, " +
                            "vehicle.num_document, " +
                            "(SELECT reason " +
                            "   FROM infringement_relInfraction_infringements " +
                            "   WHERE infringements_id = infra.id LIMIT 1) reason, " +
                            "infra.sync, " +
                            "vehicle.identifier_document_id, " +
                            "vehicle.sub_brand_id, " +
                            "vehicle.colour_id " +
                            "FROM infringement_infringements infra " +
                            "INNER JOIN vehicle_vehicles vehicle ON infra.vehicle_id = vehicle.id LIMIT 1")
                } else {//Búsqueda por folio
                    SimpleSQLiteQuery("SELECT infra.id, " +
                            "infra.folio, " +
                            "infra.date, " +
                            "vehicle.num_document, " +
                            "(SELECT reason FROM infringement_relInfraction_infringements WHERE infringements_id = infra.id LIMIT 1) reason, " +
                            "infra.sync " +
                            "vehicle.identifier_document_id, " +
                            "vehicle.sub_brand_id, " +
                            "vehicle.colour_id " +
                            "FROM infringement_infringements infra " +
                            "INNER JOIN vehicle_vehicles vehicle ON infra.vehicle_id = vehicle.id " +
                            "WHERE infra.folio = \'$filter\' " +
                            "ORDER BY infra.folio DESC LIMIT 1")
                }
            }
            else -> {
                SimpleSQLiteQuery("SELECT infra.id, " +
                        "infra.folio, " +
                        "infra.date, " +
                        "vehicle.num_document, " +
                        "(SELECT reason " +
                        "   FROM infringement_relInfraction_infringements " +
                        "   WHERE infringements_id = infra.id LIMIT 1) reason, " +
                        "infra.sync " +
                        "vehicle.identifier_document_id, " +
                        "vehicle.sub_brand_id, " +
                        "vehicle.colour_id " +
                        "FROM infringement_infringements infra " +
                        "INNER JOIN vehicle_vehicles vehicle ON infra.vehicle_id = vehicle.id " +
                        "WHERE vehicle.identifier_document_id = $id " +
                        "AND vehicle.num_document = \'$filter\'")
            }
        }
        itemInfraOffLine = SearchManagerWeb.getItemInfraction(query)
        if (itemInfraOffLine.size > 0) {
            //1.-Iterar la lista y hacer consulta a firebase para obtener elementos de catálogos.
            //1.1.- Generar la nueva lista.
            itemInfraOffLine.forEach { infra ->
                val job = GlobalScope.launch(Dispatchers.Main) {
                    insuredDocument = CatalogsFirebaseManager.getValue(infra.id_doc_ident, FS_COL_INSURED_DOC)
                    //brand = CatalogsFirebaseManager.getValue("G3PYAeJ289WSeJMliq3l", FS_COL_BRANDS)
                    model = CatalogsFirebaseManager.getValue(infra.sub_brand_id, FS_COL_MODELS)
                    colour = CatalogsFirebaseManager.getValue(infra.colour_id, FS_COL_COLORS)
                }
                job.join()

                itemInfraOffLineList.add(InfractionItemList(
                        infra.id_infraction,
                        infra.folio,
                        infra.num_document,
                        infra.reason,
                        infra.sync,
                        insuredDocument,
                        "",
                        model,
                        colour,
                        infra.date
                ))
            }
            listener.onResultSearchOffLine(itemInfraOffLineList)
        } else {
            listener.onError("No se encontraron infracciones")
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
        /*if (SingletonInfraction.colonyOffender.isNotEmpty()) {
            SingletonTicket.colonyOffender = SingletonInfraction.colonyOffender
        }*/
        if (SingletonInfraction.stateOffender.documentReference != null) {
            SingletonTicket.stateOffender = SingletonInfraction.stateOffender.value
        }
        if (SingletonInfraction.noCirculationCard.isNotEmpty()) {
            SingletonTicket.noLicenseOffender = SingletonInfraction.noCirculationCard
        }
        if (SingletonInfraction.typeLicenseOffender.documentReference != null) {
            SingletonTicket.typeLicenseOffender = SingletonInfraction.typeLicenseOffender.value
        }
        if (SingletonInfraction.licenseIssuedInOffender.documentReference != null) {
            SingletonTicket.stateLicenseOffender = SingletonInfraction.licenseIssuedInOffender.value
        }
        //SingletonTicket.brandVehicle = SingletonInfraction.brandVehicle.vehicle_brand
        /* if (SingletonInfraction.subBrandVehicle.isNotEmpty()) {
             SingletonTicket.subBrandVehicle = SingletonInfraction.subBrandVehicle
         }*/
        //SingletonTicket.typeVehicle = SingletonInfraction.typeVehicle.type_string
        //SingletonTicket.colorVehicle = SingletonInfraction.colorVehicle
        SingletonTicket.modelVehicle = SingletonInfraction.yearVehicle
        //SingletonTicket.identifierVehicle = SingletonInfraction.identifierDocument.document
        SingletonTicket.noIdentifierVehicle = SingletonInfraction.noDocument
        //SingletonTicket.expeditionAuthVehicle = SingletonInfraction.typeDocument.authority
        SingletonTicket.stateExpVehicle = SingletonInfraction.stateIssuedIn.value
        SingletonInfraction.motivationList.forEach { art ->
            /* val article = SingletonTicket.ArticleFraction(art.article.article, art.fraction.fraccion, art.fraction.minimum_wages.toString(),
                     art.fraction.penalty_points.toString(), art.motivation)
             SingletonTicket.fractionsList.add(article)*/
        }
        SingletonTicket.streetInfraction = SingletonInfraction.streetInfraction
        SingletonTicket.betweenStreetInfraction = SingletonInfraction.betweenStreet1
        SingletonTicket.andStreetInfraction = SingletonInfraction.betweenStreet2
        SingletonTicket.colonyInfraction = SingletonInfraction.colonnyInfraction.value
        SingletonTicket.retainedDocumentInfraction = SingletonInfraction.retainedDocument.value
        SingletonTicket.isRemitedInfraction = SingletonInfraction.isRemited
        if (SingletonInfraction.isRemited) {
            SingletonTicket.remitedDispositionInfraction = SingletonInfraction.dispositionRemited.value
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
        /* val insuredDocument = CatalogsFirebaseManager.getInsuredDocument("0QAIC5Vofiw5Xh7s3mS1")
         //Log.d("INSURED_FIREBASE", insuredDocument)
         val infraction = SearchManager.getInfractionById(id.toLong())
         val infraFracc = SearchManager.getInfraFracc(id.toLong())
         listener.onResultInfractionByIdOffline(infraction, infraFracc, origin)*/
    }


}

class NewIdentDocument {
    var id: Int = 0
    var value: String = ""

    override fun toString(): String {
        return value
    }
}
