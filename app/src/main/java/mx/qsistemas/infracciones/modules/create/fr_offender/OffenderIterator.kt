package mx.qsistemas.infracciones.modules.create.fr_offender

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import com.google.gson.Gson
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.db.managers.SaveInfractionManager
import mx.qsistemas.infracciones.db_web.entities.*
import mx.qsistemas.infracciones.db_web.managers.SaveInfractionManagerWeb
import mx.qsistemas.infracciones.net.FirebaseEvents
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.*
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.*
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.dtos.Voucher
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class OffenderIterator(val listener: OffenderContracts.Presenter) : OffenderContracts.Iterator {

    internal lateinit var statesList: MutableList<GenericCatalog>
    internal lateinit var stateIssuedLicenseList: MutableList<GenericCatalog>
    internal lateinit var townshipsList: MutableList<Townships>
    internal lateinit var licenseTypeList: MutableList<LicenseType>
    internal lateinit var txInfo: TransactionInfo

    /* Variable From Saved Infraction */
    private val actualDay = SimpleDateFormat("YYYY-MM-DD").format(Date())
    private val actualTime = SimpleDateFormat("HH:MM:SS").format(Date())
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var nonWorkingDays: MutableList<NonWorkingDay>
    private lateinit var config: Config
    private var totalImport = 0F
    private var totalPoints = 0
    private var totalUmas = 0
    private var fifteenthDay = ""
    private var thirtythDay = ""
    private var newFolio = ""
    private var captureLine1 = ""
    private var captureLine2 = ""

    override fun getStatesList() {
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("enable", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener?.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val list = mutableListOf<String>()
                statesList = mutableListOf()
                statesList.add(GenericCatalog("Seleccionar...", true, null))
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    list.add(data.value)
                    statesList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onStatesReady(adapter)
            }
        }
    }

    override fun getStatesIssuedList() {
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("enable", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener?.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val list = mutableListOf<String>()
                stateIssuedLicenseList = mutableListOf()
                stateIssuedLicenseList.add(GenericCatalog("Seleccionar...", true, null))
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    list.add(data.value)
                    stateIssuedLicenseList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onStatesIssuedReady(adapter)
            }
        }
    }

    override fun getTownshipsList(posState: Int) {
        Application.firestore?.collection(FS_COL_CITIES)?.whereEqualTo("reference", statesList[posState].documentReference)?.whereEqualTo("is_active", true)?.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                listener?.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (querySnapshot != null) {
                val list = mutableListOf<String>()
                townshipsList = mutableListOf()
                townshipsList.add(Townships("PRX", 0, "Seleccionar...", null, true, null))
                list.add("Seleccionar..")
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val data = document.toObject(Townships::class.java)!!
                        list.add(data.value)
                        townshipsList.add(data)
                    }
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onTownshipsReady(adapter)
            }
        }
    }

    override fun getTypeLicenseAdapter(): ArrayAdapter<String> {
        licenseTypeList = CatalogsAdapterManager.getLicenseTypeList()
        val strings = mutableListOf<String>()
        licenseTypeList.forEach {
            if (it.id == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.license_type)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getPositionState(obj: GenericCatalog): Int {
        for (i in 0 until statesList.size) {
            if (statesList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionStateLicense(obj: GenericCatalog): Int {
        for (i in 0 until stateIssuedLicenseList.size) {
            if (stateIssuedLicenseList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionTownship(obj: Townships): Int {
        for (i in 0 until townshipsList.size) {
            if (townshipsList[i].childReference == obj.childReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionTypeLicense(obj: LicenseType): Int {
        for (i in 0 until licenseTypeList.size) {
            if (licenseTypeList[i].id == obj.id) {
                return i
            }
        }
        return 0
    }

    override fun saveData(notify: Boolean) {
        SingletonInfraction.idPersonTownship = Application.prefs?.loadDataInt(R.string.sp_id_township_person)!!.toLong()
        /* Get configuration */
        config = SaveInfractionManager.getConfig()
        /* Calculate infraction article variables */
        SingletonInfraction.motivationList.forEach {
            totalImport += config.minimum_salary * it.fraction.uma
            /*totalPoints += it.fraction.penalty_points
            totalUmas += it.fraction.minimum_wages*/
        }
        SingletonInfraction.subTotalInfraction = "%.2f".format(totalImport).replace(",", ".")
        val fiftiethDiscount = totalImport * .5
        SingletonInfraction.discountInfraction = "%.2f".format(fiftiethDiscount).replace(",", ".")
        SingletonInfraction.totalInfraction = "%.2f".format(totalImport - fiftiethDiscount).replace(",", ".")
        /* Get future working days */
        nonWorkingDays = SaveInfractionManager.getNonWorkingDays()
        fifteenthDay = getFutureWorkingDay(15)
        thirtythDay = getFutureWorkingDay(30)
        newFolio = generateNewFolio()
        /* Generate all banking capture lines */
        captureLine1 = Utils.generateCaptureLine(newFolio.replace("-", ""), fifteenthDay, "%.2f".format(fiftiethDiscount), "2")
        captureLine2 = Utils.generateCaptureLine(newFolio.replace("-", ""), thirtythDay, "%.2f".format(totalImport), "2")
        /* Validate if is the first time to insert the infraction */
        if (SingletonInfraction.idNewInfraction == 0L) {
            /* Step 1. Save the infraction information */
            val infraction = InfringementInfringements(
                    0,
                    newFolio,
                    "active",
                    SingletonInfraction.isPersonAbstent,
                    SingletonInfraction.retainedDocument.documentReference?.id ?: "",
                    SingletonInfraction.dispositionRemited.documentReference?.id ?:"",
                    SingletonInfraction.idPersonTownship,
                    SingletonInfraction.dispositionRemited.documentReference?.id ?:"",
                    actualDay,
                    actualTime,
                    SingletonInfraction.retainedDocument.documentReference?.id?.toBoolean() ?:false,
                    false)

            SingletonInfraction.idNewInfraction = SaveInfractionManagerWeb.insertInfraction(infraction)
            /* Step 2. Validate that sub brand doesn't exists */
            /*val brandExisting = SaveInfractionManager.getSubBrandExist(SingletonInfraction.subBrandVehicle, SingletonInfraction.brandVehicle.id)
            // If sub brand not exist, then save it to future possible uses
            if (brandExisting.id_submarking_vehicle == 0) {
                val submarkingVehicle = SubmarkingVehicle(0, SingletonInfraction.brandVehicle.id, SingletonInfraction.subBrandVehicle)
                SaveInfractionManager.saveSubBrandVehicle(submarkingVehicle)
            }*/
            /* Step 3. Save Vehicle Information */
            val vehicleInfraction = VehicleVehicles(
                    0,
                    SingletonInfraction.yearVehicle,
                    SingletonInfraction.colorVehicle.documentReference?.id ?: "",
                    SingletonInfraction.subBrandVehicle.childReference?.id ?: "", //TODO: Pendiente de hacer el proceso
                    SingletonInfraction.identifierDocument.documentReference?.id ?: "",
                    SingletonInfraction.stateIssuedIn.documentReference?.id ?: "",
                    SingletonInfraction.noDocument)

            SaveInfractionManagerWeb.saveVehicleInfraction(vehicleInfraction)

            /* Step 4. Save Person Information */
            val person = DriverDrivers(0, SingletonInfraction.nameOffender, SingletonInfraction.lastFatherName, SingletonInfraction.lastMotherName, SingletonInfraction.rfcOffenfer)
            SingletonInfraction.idNewPersonInfraction = SaveInfractionManagerWeb.savePersonInformation(person)
            /* Step 5. Save Person-Infraction Relation */
            //TODO:Pendiente, preguntar
            SaveInfractionManager.savePersonInfractionRelation(PersonInfringement(SingletonInfraction.idNewInfraction.toInt(), SingletonInfraction.idNewPersonInfraction))
            /* Step 6. Save Person Address If Offender Was On The Moment */


            if (!SingletonInfraction.isPersonAbstent) {
                val personAddress = DriverAddressDriver(
                        0,
                        SingletonInfraction.streetOffender,
                        SingletonInfraction.noExtOffender,
                        SingletonInfraction.noIntOffender,
                        SingletonInfraction.townshipOffender.reference?.id ?: "",
                        SingletonInfraction.colonyOffender.documentReference?.id ?: "",
                        SingletonInfraction.zipCodeOffender.documentReference?.id ?: "",
                        SingletonInfraction.idNewPersonInfraction,
                        SingletonInfraction.stateOffender.documentReference?.id ?: "")
                val idNewPersonAddress = SaveInfractionManagerWeb.saveAddressPerson(personAddress)
                /* Step 6:1. Save Person-Address Relation */
                //TODO:Pendiente, preguntar
                SaveInfractionManager.savePersonAddressRelation(AddressPerson(idNewPersonAddress.toInt(), SingletonInfraction.idNewPersonInfraction))
            }
            /* Step 7. Save Address Information */
            val infractionAddress = InfringementAddressInfringement(
                    0,
                    SingletonInfraction.streetInfraction,
                    SingletonInfraction.betweenStreet1,
                    SingletonInfraction.betweenStreet2,
                    "tokenCity",
                    SingletonInfraction.colonnyInfraction.childReference?.id ?: "",
                    SingletonInfraction.zipCodeInfraction.childReference?.id ?: "",
                    SingletonInfraction.stateInfraction.documentReference?.id ?: "")
            val idInfractionAddress = SaveInfractionManagerWeb.saveAddressInfraction(infractionAddress)
            /* Step 7:1. Save Infraction-Address Relation */
            //TODO:Pendiente, preguntar
            SaveInfractionManager.saveInfractionAddressRelation(AddressInfringement(idInfractionAddress.toInt(), SingletonInfraction.idNewInfraction.toInt()))
            /* Step 8. Save Fractions List */
            SingletonInfraction.motivationList.forEach {
                val trafficViolation = InfringementRelfractionInfringements(
                        0,
                        it.fraction.minimum_wages,
                        it.fraction.penalty_points,
                        it.fraction.fraccion,//TODO: Tiene que existir una Referencia a firebase
                        it.motivation,
                        "AMOUNT".toFloat()) //TODO: No sé de donde viene el monto xd
                SaveInfractionManagerWeb.saveTrafficViolation(trafficViolation)
            }
            /* Step 9. Save Evidence Photos */
            //TODO:Aquí me quedé xd
            val evidencePhoto = InfractionEvidence(0, SingletonInfraction.idNewInfraction.toInt(), SingletonInfraction.evidence1, SingletonInfraction.evidence2, false)
            SaveInfractionManager.saveInfractionEvidence(evidencePhoto)
            /* Step 10. Register Event Infraction */
            FirebaseEvents.registerInfractionFinished()
            /* Notify View That All Data Was Saved */
            if (notify) listener.onDataSaved()
        } else {
            listener.onDataDuplicate()
        }
    }

    override fun updateData() {
        val rootObj = JSONObject()
        val idRegUser = Application.prefs?.loadDataInt(R.string.sp_id_person)!!.toLong()


        rootObj.put("IdInfraccion", SingletonInfraction.idNewInfraction)
        rootObj.put("username", "InfraMobile")
        rootObj.put("password", "CF2E3EF25C90EB567243ADFACD4AA868")
        rootObj.put("name", SingletonInfraction.nameOffender)
        rootObj.put("lastName", SingletonInfraction.lastFatherName)
        rootObj.put("mothersLastName", SingletonInfraction.lastMotherName)
        rootObj.put("idRegUser", idRegUser)

        Log.d("JSON-UPDATE_PERSON", rootObj.toString())

        NetworkApi().getNetworkService().updatePerson(rootObj.toString()).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = Gson().fromJson(response.body(), ServiceResponsePerson::class.java)
                    Log.d("UPDATE-PERSON", "${data.ids[0]}")
                    if (data.flag) {
                        listener.onDataUpdated(data.ids[0])
                    } else {
                        listener.onError(data.message)
                    }

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                listener.onError(t.message ?: "")
            }
        })
    }

    override fun savePaymentToService(idInfraction: String, txInfo: TransactionInfo, amount: String, discount: String, totalPayment: String, idPerson: Long) {
        this.txInfo = txInfo
        val idRegUser = Application.prefs?.loadDataInt(R.string.sp_id_township_person)!!.toLong()
        val paymentCardData = UpdatePaymentRequest.UpdatePaymentCardData(txInfo.aid, txInfo.apn, txInfo.arqc, txInfo.authorization,
                txInfo.entryType, txInfo.maskedPan, txInfo.txDate, "", txInfo.txTime, "", idRegUser.toString(),
                txInfo.affiliation, txInfo.expirationDate, "Aprobado", txInfo.brandCard, txInfo.typeCard,
                txInfo.bank, txInfo.reference, totalPayment, txInfo.tvr, txInfo.tsi, txInfo.noControl,
                txInfo.cardOwner, "", txInfo.typeTx)
        val paymentData = UpdatePaymentRequest.UpdatePaymentData(2, amount, discount, totalPayment,
                txInfo.authorization, "", idPerson)
        val request = UpdatePaymentRequest(idInfraction, "", "InfraMobile", "CF2E3EF25C90EB567243ADFACD4AA868", paymentCardData,
                paymentData)
        NetworkApi().getNetworkService().savePayment(idInfraction.toLong(), Gson().toJson(request)).enqueue(object : Callback<String> {
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

    override fun savePayment(info: TransactionInfo) {
        this.txInfo = info
        /* Step 1. Save Payment Infraction */
        val paymentInfringement = PaymentInfringement(0, SingletonInfraction.idNewInfraction.toInt(), 1, SingletonInfraction.subTotalInfraction.toFloat(), SingletonInfraction.discountInfraction.toFloat(),
                SingletonInfraction.totalInfraction.toFloat(), info.authorization, "", SingletonInfraction.idNewPersonInfraction, 0F)
        SaveInfractionManager.savePaymentInfringement(paymentInfringement)
        /* Step 2. Save Payment Transaction Information */
        val paymentInfringementCard = PaymentInfringementCard(0, info.aid, info.apn, info.arqc, info.authorization, info.entryType, info.maskedPan,
                info.txDate, "", info.txTime, Application.prefs?.loadData(R.string.sp_prefix, "")!!, SingletonInfraction.idNewPersonInfraction, info.affiliation, info.expirationDate, info.flagTransaction, info.brandCard,
                info.typeCard, info.bank, info.reference, SingletonInfraction.totalInfraction, info.tvr, info.tsi, info.noControl, info.cardOwner,
                "", info.typeTx, SingletonInfraction.idNewInfraction)
        SaveInfractionManager.savePaymentInfringementCard(paymentInfringementCard)
        /* Step 3. Update Infraction To Paid */
        SaveInfractionManager.updateInfrationToPaid(SingletonInfraction.idNewInfraction)
        /* Step 4. Save Authorization Code into Singleton */
        SingletonInfraction.paymentAuthCode = info.authorization
    }

    override fun printTicket(activity: Activity) {
        SingletonTicket.dateTicket = actualDay
        SingletonTicket.folioTicket = newFolio
        SingletonTicket.completeNameOffender = SingletonInfraction.nameOffender + " " + SingletonInfraction.lastFatherName + " " + SingletonInfraction.lastMotherName
        if (SingletonInfraction.rfcOffenfer.isNotEmpty()) {
            SingletonTicket.rfcOffender = SingletonInfraction.rfcOffenfer
        }
        if (SingletonInfraction.streetOffender.isNotEmpty()) {
            SingletonTicket.streetOffender = SingletonInfraction.streetOffender
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
        if (SingletonInfraction.typeLicenseOffender.id != 0) {
            SingletonTicket.typeLicenseOffender = SingletonInfraction.typeLicenseOffender.license_type
        }
        if (SingletonInfraction.licenseIssuedInOffender.documentReference != null) {
            SingletonTicket.stateLicenseOffender = SingletonInfraction.licenseIssuedInOffender.value
        }
        //SingletonTicket.brandVehicle = SingletonInfraction.brandVehicle.vehicle_brand
        /*if (SingletonInfraction.subBrandVehicle.isNotEmpty()) {
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
        /*SingletonTicket.colonyInfraction = SingletonInfraction.colonnyInfraction
        SingletonTicket.retainedDocumentInfraction = SingletonInfraction.retainedDocument.document*/
        SingletonTicket.isRemitedInfraction = SingletonInfraction.isRemited
        /*if (SingletonInfraction.isRemited) {
            SingletonTicket.remitedDispositionInfraction = SingletonInfraction.dispositionRemited.disposition
        }*/
        SingletonTicket.nameAgent = "${Application.prefs?.loadData(R.string.sp_person_f_last_name, "")} ${Application.prefs?.loadData(R.string.sp_person_m_last_name, "")} ${Application.prefs?.loadData(R.string.sp_person_name, "")}"
        SingletonTicket.paymentAuthCode = SingletonInfraction.paymentAuthCode
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

    override fun reprintVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener) {
        val voucher = Voucher(txInfo.noControl, txInfo.maskedPan.substring(txInfo.maskedPan.length - 4, txInfo.maskedPan.length), txInfo.expirationDate, txInfo.brandCard,
                txInfo.typeCard, txInfo.bank, txInfo.authorization, txInfo.reference, txInfo.amount, txInfo.cardOwner,
                txInfo.txDate, txInfo.txTime, txInfo.aid, txInfo.tvr, txInfo.tsi, txInfo.apn, txInfo.al, txInfo.flagTransaction,
                txInfo.needSign, PTX_MERCHANT_ID, Utils.getImeiDevice(activity), txInfo.entryType)
        PaymentsTransfer.reprintVoucher(activity, listener, voucher)
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun generateNewFolio(): String {
        val lastFolio = SaveInfractionManager.getLastFolioSaved("%${Application.prefs?.loadData(R.string.sp_prefix, "")}%")
        val incremental = lastFolio.split("-")[1].toInt() + 1
        return "${lastFolio.split("-")[0]}-$incremental"
    }

    private fun getFutureWorkingDay(days: Int): String {
        val datestring: String
        val calendar = Calendar.getInstance()
        var totaldias = 0
        while (totaldias != days) {
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                val calculateDay = dateFormat.format(calendar.time)
                var isNoHabil = false
                nonWorkingDays.forEach {
                    if (it.date == calculateDay) {
                        isNoHabil = true
                    }
                }
                if (!isNoHabil) {
                    totaldias++
                }
            }
            if (totaldias != days) {
                calendar.add(Calendar.DATE, 1)
            }
        }
        datestring = dateFormat.format(calendar.time)
        return datestring
    }
}