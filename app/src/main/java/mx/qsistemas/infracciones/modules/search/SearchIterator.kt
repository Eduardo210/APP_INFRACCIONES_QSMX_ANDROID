package mx.qsistemas.infracciones.modules.search

import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.firebase.firestore.Query
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.Application.Companion.TAG
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.NonWorkingDay
import mx.qsistemas.infracciones.db_web.entities.InfractionItem
import mx.qsistemas.infracciones.db_web.entities.InfractionItemList
import mx.qsistemas.infracciones.db_web.entities.InfringementData
import mx.qsistemas.infracciones.db_web.managers.SearchManagerWeb
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.result_web.detail_result.DetailResult
import mx.qsistemas.infracciones.net.result_web.search_result.DataItem
import mx.qsistemas.infracciones.net.result_web.search_result.SearchResult
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.FS_COL_IDENTIF_DOC
import mx.qsistemas.infracciones.utils.Ticket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchIterator(private val listener: SearchContracts.Presenter) : SearchContracts.Iterator {


    internal lateinit var identifierDocList: MutableList<GenericCatalog>
    private lateinit var nonWorkingDays: MutableList<NonWorkingDay>

    private lateinit var itemInfraOnline: MutableList<DataItem>
    private var itemInfraOffLine: MutableList<InfractionItem> = ArrayList()
    private var itemInfraOffLineList: MutableList<InfractionItemList> = ArrayList()

    //Para la impresión de la boleta
    private val actualDay = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
    private var fifteenthDay = ""
    private var thirtythDay = ""
    private var newFolio = ""
    private var captureLine1 = ""
    private var captureLine2 = ""


    override fun doSearchByFilter(filter: String) {
        val token = Application.prefs.loadData(R.string.sp_session_token, "")!!
        NetworkApi().getNetworkService().searchInfraction(token, filter).enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val result = response.body()
                    if (result?.results != null) {

                        itemInfraOnline = result.results as MutableList<DataItem>
                        Log.d(TAG, "${result.results}")
                        if (result.count?.compareTo(0) != 0) {
                            listener.onResultSearch(result.results)
                        } else {
                            listener.onResultSearch(result.results)
                        }
                    } else {//No se encontraron datos
                        listener.onResultSearch(mutableListOf())
                        Log.d(TAG, "${result?.results}")
                    }

                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Log.e(TAG, response.message())
                    listener.onError(response.message().toString())
                } else {
                    Log.e(TAG, response.message())
                    listener.onError(response.message().toString())
                }
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                listener.onError(t.message ?: "")
            }

        })
    }

    override fun doSearchByIdInfraction(id: String, origin: Int) {
        val dataToken: HashMap<String, String> = hashMapOf()
        Log.d(TAG, dataToken.toString())
        val token = Application.prefs.loadData(R.string.sp_session_token, "")!!
        NetworkApi().getNetworkService().detailInfraction((token
                ),
                id).enqueue(object : Callback<DetailResult> {
            override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = response.body()//Gson().fromJson(response.body(), DetailResult::class.java)
                    if (data != null) {
                        listener.onResultInfractionById(data, origin)
                        Log.d(TAG, data.toString())
                    } else {
                        listener.onError("Ocurrió un error al obtener los datos del servidor.")
                    }

                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Log.e(TAG, response.message())
                    listener.onError(response.message())
                } else {
                    listener.onError(response.message())
                }
            }

            override fun onFailure(call: Call<DetailResult>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                listener.onError(t.message ?: "")
            }
        })
    }

    /*override fun savePaymentToService(paymentRequest: PaymentRequest, token: String) {

        NetworkApi().getNetworkService().savePaymentToServer("Bearer ${Application.prefs.loadData(R.string.sp_access_token, "")!!}",
                0L,
                paymentRequest).enqueue(object : Callback<GenericResult> {
            override fun onResponse(call: Call<GenericResult>, response: Response<GenericResult>) {
                val generic: GenericResult?
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    generic = response.body()
                    if (generic?.status == "sucess") {
                        listener.onResultSavePayment("El pago se guardó satisfactoriamente")
                    } else {
                        listener.onResultSavePayment("Error al guardar el pago.")
                    }
                } else if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    listener.onError(response.message())
                }
            }

            override fun onFailure(call: Call<GenericResult>, t: Throwable) {
                listener.onError(t.message ?: "")
            }
        })
    }*/

    override fun doSearchByFilterOffLine(filter: String) {
        val query: SimpleSQLiteQuery
        if (filter.isEmpty()) {
            query = SimpleSQLiteQuery("SELECT " +
                    "infra.id, " +
                    "infra.folio, " +
                    "(infra.date ||\' \' || infra.time) as date ," +
                    "vehicle.num_document, " +
                    "(SELECT reason FROM infringement_relInfraction_infringements) reason, " +
                    "infra.sync, " +
                    "vehicle.sub_brand, " +
                    "vehicle.colour, " +
                    "vehicle.brand " +
                    "FROM Infringement_infringements infra " +
                    "INNER JOIN Vehicle_vehicles vehicle ON infra.vehicle_id = vehicle.id " +
                    "LEFT JOIN driver_divers driver ON infra.driver_id = driver.id " +
                    "INNER JOIN person_townhall oficial ON infra.town_hall_id = oficial.idPersona " +
                    "ORDER BY infra.id DESC LIMIT 1")

        } else {
            query = SimpleSQLiteQuery("SELECT " +
                    "infra.id, " +
                    "infra.folio, " +
                    "(infra.date ||\' \' || infra.time) as date ," +
                    "vehicle.num_document, " +
                    "(SELECT reason FROM infringement_relInfraction_infringements) reason, " +
                    "infra.sync, " +
                    "vehicle.sub_brand, " +
                    "vehicle.colour, " +
                    "vehicle.brand " +
                    "FROM Infringement_infringements infra " +
                    "INNER JOIN Vehicle_vehicles vehicle ON infra.vehicle_id = vehicle.id " +
                    "LEFT JOIN driver_divers driver ON infra.driver_id = driver.id " +
                    "INNER JOIN person_townhall oficial ON infra.town_hall_id = oficial.idPersona " +
                    "WHERE (infra.folio LIKE '%$filter%') " +
                    "OR (driver.paternal LIKE '%$filter%' " +
                    "   OR driver.maternal LIKE '%$filter%' " +
                    "   OR driver.name LIKE '%$filter%') " +
                    "OR (oficial.paternal LIKE '%$filter%' " +
                    "   OR oficial.maternal LIKE'%$filter%' " +
                    "   OR oficial.name LIKE '%$filter%' ) " +
                    "OR (vehicle.num_document LIKE '%$filter%') LIMIT 10")

        }

        itemInfraOffLine = SearchManagerWeb.getItemInfraction(query)
        if (itemInfraOffLine.size > 0) {
            listener.onResultSearchOffLine(itemInfraOffLine)
        } else {
            listener.onError("No se encontraron datos con el filtro ingresado.")
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
        SingletonTicket.colonyInfraction = SingletonInfraction.colonnyInfraction
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

    override suspend fun doSearchByIdInfractionOffLine(id: String, origin: Int) {

        val infringement = InfringementData()
        infringement.driver = SearchManagerWeb.getDriverDriver(id.toLong())
        infringement.driverAddressDriver = SearchManagerWeb.getAddressDriver(id.toLong())
        infringement.infringementAddress = SearchManagerWeb.getAddressInfringement(id.toLong())
        infringement.captureLines = SearchManagerWeb.getCaptureLines(id.toLong())
        infringement.infringement = SearchManagerWeb.getInfraction(id.toLong())
        infringement.vehicleVehicles = SearchManagerWeb.getVehicle(id.toLong())
        infringement.driverLicense = SearchManagerWeb.getDriverLicense(id.toLong())
        infringement.fractions = SearchManagerWeb.getFractionsInfringements(id.toLong())
        infringement.personTownhall = SearchManagerWeb.getTownHallPerson(id.toLong())
        infringement.payOrder = SearchManagerWeb.getPayOrder(id.toLong())
        infringement.electronicBill = SearchManagerWeb.getElectronicBill(id.toLong())

        listener.onResultInfractionByIdOffline(infringement, origin)
    }

    override fun getIdentifierDocAdapter() {
        Application.firestore.collection(FS_COL_IDENTIF_DOC).whereEqualTo("is_active", true).orderBy("value", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            identifierDocList = mutableListOf()
            identifierDocList.add(GenericCatalog("Seleccionar...", true))
            val list = mutableListOf<String>()
            list.add("Seleccionar...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    identifierDocList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onIdentifierDocReady(adapter)
        }
    }

    override fun getPositionIdentifiedDoc(obj: GenericCatalog): Int {
        for (i in 0 until identifierDocList.size) {
            if (identifierDocList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }


}

data class NewIdentDocument(
        var reference: String = "",
        var value: String = "") {
    override fun toString(): String {
        return value
    }
}




