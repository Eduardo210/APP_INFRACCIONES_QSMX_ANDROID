package mx.qsistemas.infracciones.modules.create.fr_offender

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.db.managers.SaveInfractionManager
import mx.qsistemas.infracciones.db_web.entities.*
import mx.qsistemas.infracciones.db_web.managers.SaveInfractionManagerWeb
import mx.qsistemas.infracciones.net.FirebaseEvents
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.GenericSubCatalog
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.net.catalogs.UpdatePaymentRequest
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.*
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.dtos.Voucher
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class OffenderIterator(val listener: OffenderContracts.Presenter) : OffenderContracts.Iterator {

    internal lateinit var statesList: MutableList<GenericCatalog>
    internal lateinit var townshipList: MutableList<Townships>
    internal lateinit var zipCodesList: MutableList<GenericSubCatalog>
    internal lateinit var holidayList: MutableList<String>
    internal lateinit var stateIssuedLicenseList: MutableList<GenericCatalog>
    internal lateinit var coloniesList: MutableList<GenericSubCatalog>
    internal lateinit var licenseTypeList: MutableList<GenericCatalog>
    internal lateinit var txInfo: TransactionInfo

    /* Variable From Saved Infraction */
    private val actualDay = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private val actualTime = SimpleDateFormat("HH:mm:ss").format(Date())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd") //"dd/MM/yyyy"
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
    private var uma_rate: Float = 0.0f

    override fun getStatesList() {
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            statesList = mutableListOf()
            statesList.add(GenericCatalog("Seleccionar...", true))
            val list = mutableListOf<String>()
            list.add("Seleccionar...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    statesList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onStatesReady(adapter)
        }
    }

    override fun getTownshipsList(reference: DocumentReference?) {
        Application.firestore?.collection(FS_COL_CITIES)?.whereEqualTo("reference", reference)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            townshipList = mutableListOf()
            townshipList.add(Townships("PRX", 0, "Seleccionar...", reference, true))
            val list = mutableListOf<String>()
            list.add("Seleccionar...")
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    val data = document.toObject(Townships::class.java)!!
                    data.childReference = document.reference
                    list.add(data.value)
                    townshipList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onTownshipsReady(adapter)
        }
    }

    override fun getZipCodesList(reference: DocumentReference?) {
        Application.firestore?.collection(FS_COL_ZIP_CODES)?.whereEqualTo("reference", reference)?.whereEqualTo("is_active", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            zipCodesList = mutableListOf()
            zipCodesList.add(GenericSubCatalog("Seleccionar...", reference, true))
            val list = mutableListOf<String>()
            list.add("Seleccionar...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericSubCatalog::class.java)!!
                    data.childReference = document.reference
                    data.value = document.id
                    list.add(data.value)
                    zipCodesList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onZipCodesReady(adapter)
        }
    }

    override fun getColoniesList(reference: DocumentReference?) {
        Application.firestore?.collection(FS_COL_COLONIES)?.whereEqualTo("reference", reference)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            coloniesList = mutableListOf()
            coloniesList.add(GenericSubCatalog("Selecciona...", reference, true))
            val list = mutableListOf<String>()
            list.add("Selecciona...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericSubCatalog::class.java)!!
                    data.childReference = document.reference
                    list.add(data.value)
                    coloniesList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onColoniesReady(adapter)
        }
    }

    override fun getTypeLicenseAdapter() {
        Application.firestore?.collection(FS_COL_TYPE_LIC)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            licenseTypeList = mutableListOf()
            licenseTypeList.add(GenericCatalog("Selecciona...", true))
            val list = mutableListOf<String>()
            list.add("Selecciona...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    licenseTypeList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onTypeLicenseReady(adapter)
        }
    }

    override fun getStatesIssuedList() {
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            stateIssuedLicenseList = mutableListOf()
            stateIssuedLicenseList.add(GenericCatalog("Seleccionar...", true))
            val list = mutableListOf<String>()
            list.add("Seleccionar...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    stateIssuedLicenseList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onStatesIssuedReady(adapter)
        }
    }

    override fun getHolidays() {
        Application.firestore?.collection(FS_COL_HOLIDAYS)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            holidayList = mutableListOf()
            val list = mutableListOf<String>()
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    holidayList.add(document.id)
                }
            }
        }
    }

    override fun getPositionState(obj: GenericCatalog): Int {
        for (i in 0 until statesList.size)
            if (statesList[i].documentReference == obj.documentReference)
                return i
        return 0
    }

    override fun getPositionTownship(obj: Townships): Int {
        for (i in 0 until townshipList.size)
            if (townshipList[i].childReference == obj.childReference)
                return i
        return 0
    }

    override fun getPositionZipCode(obj: GenericSubCatalog): Int {
        for (i in 0 until zipCodesList.size)
            if (zipCodesList[i].childReference == obj.childReference)
                return i
        return 0
    }

    override fun getPositionStateLicense(obj: GenericCatalog): Int {
        for (i in 0 until stateIssuedLicenseList.size)
            if (stateIssuedLicenseList[i].documentReference == obj.documentReference)
                return i
        return 0
    }

    override fun getPositionColony(obj: GenericSubCatalog): Int {
        for (i in 0 until coloniesList.size)
            if (coloniesList[i].childReference == obj.childReference)
                return i
        return 0
    }

    override fun getPositionTypeLicense(obj: GenericCatalog): Int {
        for (i in 0 until licenseTypeList.size)
            if (licenseTypeList[i].documentReference == obj.documentReference)
                return i
        return 0
    }

    override fun saveData(notify: Boolean) {
        var totalUmas: Float = 0F
        SingletonInfraction.idOfficer = Application.prefs?.loadDataInt(R.string.sp_id_officer)!!.toLong()
        /* Get configuration */
        //config = SaveInfractionManager.getConfig()
        /* Calculate infraction article variables */
        SingletonInfraction.motivationList.forEach {
            totalImport += SingletonInfraction.townshipInfraction.uma_rate * it.fraction.uma
            totalUmas += it.fraction.uma
        }
        SingletonInfraction.subTotalInfraction = "%.2f".format(totalImport).replace(",", ".")
        val fiftiethDiscount = totalImport * .5
        SingletonInfraction.discountInfraction = "%.2f".format(fiftiethDiscount).replace(",", ".")
        SingletonInfraction.totalInfraction = "%.2f".format(totalImport - fiftiethDiscount).replace(",", ".")
        /* Get future working days */
        fifteenthDay = getFutureWorkingDay(15)
        thirtythDay = getFutureWorkingDay(30)
        newFolio = generateNewFolio()
        /* Generate all banking capture lines */
        captureLine1 = Utils.generateCaptureLine(newFolio.replace("-", ""), fifteenthDay, "%.2f".format(fiftiethDiscount), "2")
        captureLine2 = Utils.generateCaptureLine(newFolio.replace("-", ""), thirtythDay, "%.2f".format(totalImport), "2")
        /* Validate if is the first time to insert the infraction */
        if (SingletonInfraction.idNewInfraction == 0L) {

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
                    SingletonInfraction.isNewColor,
                    SingletonInfraction.typeVehicle.documentReference?.id ?: "",
                    SingletonInfraction.subBrandVehicle.reference?.id ?: "",
                    SingletonInfraction.subBrandVehicle.childReference?.id ?: "",
                    SingletonInfraction.isNewSubBrand,
                    SingletonInfraction.identifierDocument.documentReference?.id ?: "",
                    SingletonInfraction.stateIssuedIn.documentReference?.id ?: "",
                    SingletonInfraction.noDocument,
                    SingletonInfraction.typeDocument.documentReference?.id ?: "")

            val idVehicle = SaveInfractionManagerWeb.saveVehicleInfraction(vehicleInfraction)

            /* Step 4. Save Person Information */
            val person = DriverDrivers(0, SingletonInfraction.nameOffender, SingletonInfraction.lastFatherName, SingletonInfraction.lastMotherName, SingletonInfraction.rfcOffenfer)
            SingletonInfraction.idNewPersonInfraction = SaveInfractionManagerWeb.savePersonInformation(person)

            /* Step 1. Save the infraction information */
            val retainedAnyDocument = (SingletonInfraction.retainedDocument.documentReference?.id != null)
            val infraction = InfringementInfringements(
                    0,
                    newFolio,
                    0, //TODO: umas
                    false,
                    "active",
                    SingletonInfraction.isPersonAbstent,
                    SingletonInfraction.retainedDocument.documentReference?.id ?: "",
                    SingletonInfraction.dispositionRemited.documentReference?.id ?: "",
                    SingletonInfraction.idOfficer,
                    idVehicle,
                    actualDay,
                    actualTime,
                    "",
                    retainedAnyDocument, // is insured
                    false,
                    SingletonInfraction.typeLicenseOffender.documentReference?.id ?: "",
                    0.0f, //amount //TODO: AMOUNT
                    SingletonInfraction.idNewPersonInfraction,
                    totalUmas)
            SingletonInfraction.idNewInfraction = SaveInfractionManagerWeb.insertInfraction(infraction)

            /* Step 5. Save Person-Infraction Relation */
            //TODO:Pendiente, preguntar
            //SaveInfractionManager.savePersonInfractionRelation(PersonInfringement(SingletonInfraction.idNewInfraction.toInt(), SingletonInfraction.idNewPersonInfraction))
            /* Step 6. Save Person Address If Offender Was On The Moment */


            if (!SingletonInfraction.isPersonAbstent) {
                val personAddress = DriverAddressDriver(
                        0,
                        SingletonInfraction.streetOffender,
                        SingletonInfraction.noExtOffender,
                        SingletonInfraction.noIntOffender,
                        SingletonInfraction.townshipOffender.reference?.id ?: "",
                        SingletonInfraction.colonyOffender.childReference?.id ?: "",
                        SingletonInfraction.zipCodeOffender.childReference?.id ?: "",
                        SingletonInfraction.idNewPersonInfraction.toString(),
                        SingletonInfraction.stateOffender.documentReference?.id ?: "")

                val idNewPersonAddress = SaveInfractionManagerWeb.saveAddressPerson(personAddress)
                //* Step 6.1 Save Driver License
                val driverLicense = DriverDriverLicense(
                        0,
                        SingletonInfraction.noLicenseOffender,
                        idNewPersonAddress,
                        SingletonInfraction.typeLicenseOffender.documentReference?.id ?: "",
                        SingletonInfraction.licenseIssuedInOffender.documentReference?.id ?: "")

                SaveInfractionManagerWeb.saveDriverLicense(driverLicense)
                /* Step 6:1. Save Person-Address Relation */


                val previousFifteen = dateFormat.parse(fifteenthDay)!!
                val previousThirty = dateFormat.parse(thirtythDay)!!

                val fifteenthDayF = SimpleDateFormat("dd/MM/yyyy").format(previousFifteen)
                val thirtythDayF = SimpleDateFormat("dd/MM/yyyy").format(previousThirty)

                val captureLine1 = InfringementCapturelines(0,
                        captureLine1,
                        fifteenthDayF.toString(),
                        SingletonInfraction.totalInfraction.toFloat(),
                        "Bancaria",
                        1,
                        SingletonInfraction.idNewInfraction.toString())

                SaveInfractionManagerWeb.saveCaptureLine(captureLine1)
                val captureLine2 = InfringementCapturelines(0,
                        captureLine2,
                        thirtythDayF.toString(),
                        SingletonInfraction.subTotalInfraction.toFloat(),
                        "Bancaria",
                        2,
                        SingletonInfraction.idNewInfraction.toString())

                SaveInfractionManagerWeb.saveCaptureLine(captureLine2)
            }
            /* Step 7. Save Address Information */
            val infractionAddress = InfringementAddressInfringement(
                    0,
                    SingletonInfraction.streetInfraction,
                    SingletonInfraction.betweenStreet1,
                    SingletonInfraction.betweenStreet2,
                    SingletonInfraction.townshipInfraction.childReference?.id ?: "",
                    SingletonInfraction.colonnyInfraction.childReference?.id ?: "",
                    SingletonInfraction.zipCodeInfraction.childReference?.id ?: "",
                    SingletonInfraction.stateInfraction.documentReference?.id ?: "",
                    SingletonInfraction.idNewInfraction,
                    SingletonInfraction.latitudeInfraction,
                    SingletonInfraction.longitudeInfraction)
            val idInfractionAddress = SaveInfractionManagerWeb.saveAddressInfraction(infractionAddress)
            /* Step 7:1. Save Infraction-Address Relation */
            //TODO:Pendiente, preguntar
            SaveInfractionManager.saveInfractionAddressRelation(AddressInfringement(idInfractionAddress.toInt(), SingletonInfraction.idNewInfraction.toInt()))
            /* Step 8. Save Fractions List */
            SingletonInfraction.motivationList.forEach {
                val trafficViolation = InfringementRelfractionInfringements(
                        0,
                        it.fraction.uma,
                        it.fraction.childReference?.id!!,
                        //TODO: Tiene que existir una Referencia a firebase
                        SingletonInfraction.idNewInfraction,
                        it.motivation,
                        "250".toFloat()) //TODO: No sé de donde viene el monto xd
                SaveInfractionManagerWeb.saveTrafficViolation(trafficViolation)
            }
            /* Step 9. Save Evidence Photos */
            //TODO:Aquí me quedé xd
            val evidence1 = InfringementPicturesInfringement(0, SingletonInfraction.evidence1, "", SingletonInfraction.idNewInfraction)
            val evidence2 = InfringementPicturesInfringement(0, SingletonInfraction.evidence1, "", SingletonInfraction.idNewInfraction)
            //val evidencePhoto = InfractionEvidence(0, SingletonInfraction.idNewInfraction.toInt(), SingletonInfraction.evidence1, SingletonInfraction.evidence2, false)
            SaveInfractionManagerWeb.saveInfractionEvidence(evidence1)
            SaveInfractionManagerWeb.saveInfractionEvidence(evidence2)


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
        val idRegUser = Application.prefs?.loadDataInt(R.string.sp_id_officer)!!.toLong()


        rootObj.put("IdInfraccion", SingletonInfraction.idNewInfraction)
        rootObj.put("username", "InfraMobile")
        rootObj.put("password", "CF2E3EF25C90EB567243ADFACD4AA868")
        rootObj.put("name", SingletonInfraction.nameOffender)
        rootObj.put("lastName", SingletonInfraction.lastFatherName)
        rootObj.put("mothersLastName", SingletonInfraction.lastMotherName)
        rootObj.put("idRegUser", idRegUser)

        Log.d("JSON-UPDATE_PERSON", rootObj.toString())

        /* NetworkApi().getNetworkService().updatePerson(rootObj.toString()).enqueue(object : Callback<String> {
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
         })*/
    }

    override fun savePaymentToService(idInfraction: String, txInfo: TransactionInfo, amount: String, discount: String, totalPayment: String, idPerson: Long) {
        this.txInfo = txInfo
        val idRegUser = Application.prefs?.loadDataInt(R.string.sp_id_officer)!!.toLong()
        val paymentCardData = UpdatePaymentRequest.UpdatePaymentCardData(txInfo.aid, txInfo.apn, txInfo.arqc, txInfo.authorization,
                txInfo.entryType, txInfo.maskedPan, txInfo.txDate, "", txInfo.txTime, "", idRegUser.toString(),
                txInfo.affiliation, txInfo.expirationDate, "Aprobado", txInfo.brandCard, txInfo.typeCard,
                txInfo.bank, txInfo.reference, totalPayment, txInfo.tvr, txInfo.tsi, txInfo.noControl,
                txInfo.cardOwner, "", txInfo.typeTx)
        val paymentData = UpdatePaymentRequest.UpdatePaymentData(2, amount, discount, totalPayment,
                txInfo.authorization, "", idPerson)
        val request = UpdatePaymentRequest(idInfraction, "", "InfraMobile", "CF2E3EF25C90EB567243ADFACD4AA868", paymentCardData,
                paymentData)
        /*NetworkApi().getNetworkService().savePayment(idInfraction.toLong(), Gson().toJson(request)).enqueue(object : Callback<String> {
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
        if (SingletonInfraction.colonyOffender.reference != null) {
            SingletonTicket.colonyOffender = SingletonInfraction.colonyOffender.value
        }
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
        SingletonTicket.brandVehicle = SingletonInfraction.brandVehicle.value
        if (SingletonInfraction.subBrandVehicle.reference != null) {
            SingletonTicket.subBrandVehicle = SingletonInfraction.subBrandVehicle.value
        }
        SingletonTicket.typeVehicle = SingletonInfraction.typeVehicle.value
        SingletonTicket.colorVehicle = SingletonInfraction.colorVehicle.value
        SingletonTicket.modelVehicle = SingletonInfraction.yearVehicle
        SingletonTicket.identifierVehicle = SingletonInfraction.identifierDocument.value
        SingletonTicket.noIdentifierVehicle = SingletonInfraction.noDocument
        SingletonTicket.expeditionAuthVehicle = SingletonInfraction.typeDocument.value
        SingletonTicket.stateExpVehicle = SingletonInfraction.stateIssuedIn.value
        SingletonInfraction.motivationList.forEach { art ->
            val article = SingletonTicket.ArticleFraction(art.article.number, art.fraction.number, art.fraction.uma.toString(), art.motivation)
            SingletonTicket.fractionsList.add(article)
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
        SingletonTicket.nameAgent = "${Application.prefs?.loadData(R.string.sp_person_f_last_name, "")} ${Application.prefs?.loadData(R.string.sp_person_m_last_name, "")} ${Application.prefs?.loadData(R.string.sp_person_name, "")}"
        SingletonTicket.idAgent = Application.prefs?.loadDataInt(R.string.sp_id_officer)!!.toString()
        SingletonTicket.paymentAuthCode = SingletonInfraction.paymentAuthCode

        /*Regresar la fecha a su forma original*/

        val previousFifteen = dateFormat.parse(fifteenthDay)!!
        val previousThirty = dateFormat.parse(thirtythDay)!!

        val fifteenthDayF = SimpleDateFormat("dd/MM/yyyy").format(previousFifteen)
        val thirtythDayF = SimpleDateFormat("dd/MM/yyyy").format(previousThirty)

        SingletonTicket.captureLineList.add(SingletonTicket.CaptureLine(captureLine1, "CON 50% DE DESCUENTO", fifteenthDayF.toString(), SingletonInfraction.totalInfraction))
        SingletonTicket.captureLineList.add(SingletonTicket.CaptureLine(captureLine2, "SIN DESCUENTO", thirtythDayF.toString(), SingletonInfraction.subTotalInfraction))
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
        val lastFolio = SaveInfractionManagerWeb.getLastFolioSaved("%${Application.prefs?.loadData(R.string.sp_prefix, "")}%")
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
                holidayList.forEach {
                    if (it == calculateDay) {
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