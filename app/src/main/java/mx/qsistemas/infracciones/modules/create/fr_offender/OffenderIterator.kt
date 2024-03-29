package mx.qsistemas.infracciones.modules.create.fr_offender

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db_web.entities.*
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.City
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.db_web.managers.SaveInfractionManagerWeb
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.infracciones.utils.*
import mx.qsistemas.infracciones.utils.Utils.Companion.getFutureWorkingDay
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.dtos.Voucher
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class OffenderIterator(val listener: OffenderContracts.Presenter) : OffenderContracts.Iterator {

    internal lateinit var statesList: MutableList<GenericCatalog>
    internal lateinit var townshipList: MutableList<City>
    internal lateinit var zipCodesList: MutableList<ZipCodes>
    internal lateinit var holidayList: MutableList<String>
    internal lateinit var stateIssuedLicenseList: MutableList<GenericCatalog>
    internal lateinit var coloniesList: MutableList<Colony>
    internal lateinit var licenseTypeList: MutableList<GenericCatalog>
    internal lateinit var txInfo: TransactionInfo

    /* Variable From Saved Infraction */
    private val actualDay = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private val actualTime = SimpleDateFormat("HH:mm:ss").format(Date())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd") //"dd/MM/yyyy"
    private var totalImport = 0F
    private var newFolio = ""
    private var captureLineList = mutableListOf<InfringementCapturelines>()

    override fun getStatesList() {
        Application.firestore.collection(FS_COL_STATES).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
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
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onStatesReady(adapter)
            }
    }

    override fun getTownshipsList(reference: DocumentReference?) {
        townshipList = if (reference != null)
            CatalogsFirebaseManager.getCitiesByStateReference("%${reference.id}%")
        else
            mutableListOf()
        val list = mutableListOf<String>()
        townshipList.add(0, City(0, "", "Seleccionar...", reference?.id ?: "", true))
        townshipList.forEach {
            list.add(it.value)
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        listener.onTownshipsReady(adapter)
    }

    /*override fun getZipCodesList(reference: String) {
        zipCodesList = if (reference.isNotBlank())
            CatalogsFirebaseManager.getZipCodesByCityId("%$reference%")
        else
            mutableListOf()
        zipCodesList.add(0, ZipCodes(0, "", "Selecciona...", "", true))
        val list = mutableListOf<String>()
        zipCodesList.forEach {
            list.add(it.value)
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        listener.onZipCodesReady(adapter)
    }*/

    /*override fun getColoniesList(reference: String) {
        coloniesList = if (reference.isNotBlank())
            CatalogsFirebaseManager.getColoniesByZipCode(if (reference.isBlank()) 0 else reference.toInt())
        else
            mutableListOf()
        val list = mutableListOf<String>()
        coloniesList.add(0, Colony(0, "", "Selecciona...", reference, true))
        coloniesList.forEach {
            list.add(it.value)
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        listener.onColoniesReady(adapter)
    }*/

    override fun getTypeLicenseAdapter() {
        Application.firestore.collection(FS_COL_TYPE_LIC).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
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
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onTypeLicenseReady(adapter)
            }
    }

    override fun getStatesIssuedList() {
        Application.firestore.collection(FS_COL_STATES).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
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
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onStatesIssuedReady(adapter)
            }
    }

    override fun getHolidays() {
        Application.firestore.collection(FS_COL_HOLIDAYS)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
                }
                holidayList = mutableListOf()
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

    override fun getPositionTownship(obj: City): Int {
        for (i in 0 until townshipList.size)
            if (townshipList[i].key == obj.key)
                return i
        return 0
    }

    /* override fun getPositionZipCode(obj: ZipCodes): Int {
         for (i in 0 until zipCodesList.size)
             if (zipCodesList[i].key == obj.key)
                 return i
         return 0
     }*/

    override fun getPositionStateLicense(obj: GenericCatalog): Int {
        for (i in 0 until stateIssuedLicenseList.size)
            if (stateIssuedLicenseList[i].documentReference == obj.documentReference)
                return i
        return 0
    }

    /*override fun getPositionColony(obj: Colony): Int {
        for (i in 0 until coloniesList.size)
            if (coloniesList[i].key == obj.key)
                return i
        return 0
    }*/

    override fun getPositionTypeLicense(obj: GenericCatalog): Int {
        for (i in 0 until licenseTypeList.size)
            if (licenseTypeList[i].documentReference == obj.documentReference)
                return i
        return 0
    }

    override fun saveData(notify: Boolean) {
        var totalUmas = 0F
        SingletonInfraction.idOfficer = Application.prefs.loadData(R.string.sp_id_officer)
        /* Get configuration */
        //config = SaveInfractionManager.getConfig()
        /* Calculate infraction article variables */
        SingletonInfraction.motivationList.forEach {
            totalImport += SingletonInfraction.townshipInfraction.uma_rate * it.fraction.uma
            totalUmas += it.fraction.uma
        }
        SingletonInfraction.subTotalInfraction = "%.2f".format(totalImport).replace(",", ".")
        /* Get the percent discount of the same day */
        var percentSameDay = 0F
        SingletonInfraction.townshipInfraction.discount.entries.forEach {
            if (it.value.size == 1 && it.value[0] == 0) percentSameDay =
                it.key.replace("%", "").toFloat() / 100
        }
        val discount = totalImport * percentSameDay
        SingletonInfraction.discountInfraction = "%.2f".format(discount).replace(",", ".")
        SingletonInfraction.totalInfraction =
            "%.2f".format(totalImport - discount).replace(",", ".")
        /* Generate new folio of infraction */
        newFolio = generateNewFolio()
        /* Validate if is the first time to insert the infraction */
        if (SingletonInfraction.idNewInfraction == 0L) {
            /* Step 1. Save Vehicle Information */
            val vehicleInfraction = VehicleVehicles(
                0,
                SingletonInfraction.yearVehicle,
                SingletonInfraction.colorVehicle.documentReference?.id
                    ?: SingletonInfraction.colorVehicle.value,
                SingletonInfraction.colorVehicle.value,
                SingletonInfraction.isNewColor,
                SingletonInfraction.typeVehicle.documentReference?.id ?: "",
                SingletonInfraction.typeVehicle.value,
                SingletonInfraction.brandVehicle.documentReference?.id ?: "",
                SingletonInfraction.brandVehicle.value,
                SingletonInfraction.subBrandVehicle.childReference?.id
                    ?: SingletonInfraction.subBrandVehicle.value,
                SingletonInfraction.subBrandVehicle.value,
                SingletonInfraction.isNewSubBrand,
                SingletonInfraction.identifierDocument.documentReference?.id ?: "",
                SingletonInfraction.identifierDocument.value,
                SingletonInfraction.stateIssuedIn.documentReference?.id ?: "",
                SingletonInfraction.stateIssuedIn.value,
                SingletonInfraction.noDocument,
                SingletonInfraction.typeDocument.documentReference?.id ?: "",
                SingletonInfraction.typeDocument.value
            )
            val idVehicle = SaveInfractionManagerWeb.saveVehicleInfraction(vehicleInfraction)
            /* Step 2. Save Person Information */
            val person = DriverDrivers(
                0,
                SingletonInfraction.nameOffender,
                SingletonInfraction.lastFatherName,
                SingletonInfraction.lastMotherName,
                SingletonInfraction.rfcOffenfer
            )
            SingletonInfraction.idNewPersonInfraction =
                SaveInfractionManagerWeb.savePersonInformation(person)
            /* Step 3. Save the infraction information */
            val retainedAnyDocument =
                (SingletonInfraction.retainedDocument.documentReference?.id != null)
            val infraction = InfringementInfringements(
                0,
                newFolio,
                SingletonInfraction.isRemited,
                SingletonInfraction.typeService.value,
                totalUmas.toInt(),
                false,
                "active",
                SingletonInfraction.isPersonAbstent,
                SingletonInfraction.retainedDocument.documentReference?.id ?: "",
                if (SingletonInfraction.retainedDocument.documentReference?.id != null) SingletonInfraction.retainedDocument.value else "",
                SingletonInfraction.dispositionRemited.documentReference?.id ?: "",
                if (SingletonInfraction.dispositionRemited.documentReference?.id != null) SingletonInfraction.dispositionRemited.value else "",
                SingletonInfraction.idOfficer,
                idVehicle,
                actualDay,
                actualTime,
                "",
                retainedAnyDocument, // is insured
                false,
                SingletonInfraction.typeLicenseOffender.documentReference?.id ?: "",
                "%.2f".format(totalImport).toFloat(),
                SingletonInfraction.idNewPersonInfraction,
                totalUmas,
                ""
            )
            SingletonInfraction.idNewInfraction =
                SaveInfractionManagerWeb.insertInfraction(infraction)
            /* Step 4. Save Person Address If Offender Was On The Moment */
            if (!SingletonInfraction.isPersonAbstent) {
                val personAddress = DriverAddressDriver(
                    0,
                    SingletonInfraction.streetOffender,
                    SingletonInfraction.noExtOffender,
                    SingletonInfraction.noIntOffender,
                    SingletonInfraction.townshipOffender.key,
                    if (SingletonInfraction.townshipOffender.key.isNotBlank()) SingletonInfraction.townshipOffender.value else "",
                    SingletonInfraction.colonyOffender,
                    if (SingletonInfraction.colonyOffender.isNotBlank()) SingletonInfraction.colonyOffender else "",
                    SingletonInfraction.zipCodeOffender.key,
                    if (SingletonInfraction.zipCodeOffender.key.isNotBlank()) SingletonInfraction.zipCodeOffender.value else "",
                    SingletonInfraction.idNewPersonInfraction.toString(),
                    SingletonInfraction.stateOffender.documentReference?.id ?: "",
                    if (SingletonInfraction.stateOffender.documentReference?.id != null) SingletonInfraction.stateOffender.value else ""
                )
                val idNewPersonAddress = SaveInfractionManagerWeb.saveAddressPerson(personAddress)
                /* Step 4.1 Save DriverRequest License */
                val driverLicense = DriverDriverLicense(
                    0,
                    SingletonInfraction.noLicenseOffender,
                    SingletonInfraction.idNewPersonInfraction,
                    SingletonInfraction.typeLicenseOffender.documentReference?.id ?: "",
                    SingletonInfraction.licenseIssuedInOffender.documentReference?.id ?: "",
                    if (SingletonInfraction.licenseIssuedInOffender.documentReference?.id != null) SingletonInfraction.licenseIssuedInOffender.value else "",
                    if (SingletonInfraction.typeLicenseOffender.documentReference?.id != null) SingletonInfraction.typeLicenseOffender.value else ""
                )
                SaveInfractionManagerWeb.saveDriverLicense(driverLicense)
            }
            /* Step 5. Generate Capture Lines */
            captureLineList = mutableListOf()
            SingletonInfraction.townshipInfraction.discount.toSortedMap(reverseOrder()).entries.forEachIndexed { index, mutableEntry ->
                val expDate = getFutureWorkingDay(
                    mutableEntry.value[mutableEntry.value.size - 1],
                    holidayList
                )  // Get last day of validity
                val discount = totalImport * mutableEntry.key.replace("%", "").toFloat() / 100
                val total = totalImport - discount
                val codeCaptureLine = Utils.generateCaptureLine(
                    newFolio.replace("-", ""),
                    expDate,
                    "%.2f".format(total),
                    "2"
                )
                captureLineList.add(
                    InfringementCapturelines(
                        0,
                        codeCaptureLine,
                        SimpleDateFormat("dd/MM/yyyy").format(dateFormat.parse(expDate)),
                        "%.2f".format(total).toFloat(),
                        "Bancaria",
                        index + 1,
                        SingletonInfraction.idNewInfraction.toString(),
                        mutableEntry.key
                    )
                )
            }
            /* Step 6. Save Capture Lines */
            SaveInfractionManagerWeb.saveCaptureLine(captureLineList)
            /* Step 7. Save Address Information */
            val infractionAddress = InfringementAddressInfringement(
                0,
                SingletonInfraction.streetInfraction,
                SingletonInfraction.betweenStreet1,
                SingletonInfraction.betweenStreet2,
                SingletonInfraction.townshipInfraction.childReference?.id ?: "",
                SingletonInfraction.townshipInfraction.value,
                SingletonInfraction.colonnyInfraction,
                SingletonInfraction.zipCodeInfraction.key,
                SingletonInfraction.zipCodeInfraction.value,
                SingletonInfraction.stateInfraction.documentReference?.id ?: "",
                SingletonInfraction.stateInfraction.value,
                SingletonInfraction.idNewInfraction,
                SingletonInfraction.latitudeInfraction,
                SingletonInfraction.longitudeInfraction
            )
            SaveInfractionManagerWeb.saveAddressInfraction(infractionAddress)
            /* Step 8. Save Fractions List */
            SingletonInfraction.motivationList.forEach {
                val trafficViolation = InfringementRelfractionInfringements(
                    0,
                    it.fraction.uma,
                    it.fraction.reference?.id ?: "",//it.fraction.childReference?.id!!,
                    it.article.number,
                    it.fraction.childReference?.id!!,
                    it.fraction.number,
                    SingletonInfraction.idNewInfraction,
                    it.motivation, SingletonInfraction.townshipInfraction.uma_rate * it.fraction.uma
                )
                SaveInfractionManagerWeb.saveTrafficViolation(trafficViolation)
            }
            /* Step 9. Save Evidence Photos */
            val evidence1 = InfringementPicturesInfringement(
                0,
                SingletonInfraction.evidence1,
                "",
                SingletonInfraction.idNewInfraction,
                "${SingletonInfraction.noDocument}_${newFolio}_I.jpg", 0
            )
            val evidence2 = InfringementPicturesInfringement(
                0,
                SingletonInfraction.evidence1,
                "",
                SingletonInfraction.idNewInfraction,
                "${SingletonInfraction.noDocument}_${newFolio}_2.jpg", 0
            )
            SaveInfractionManagerWeb.saveInfractionEvidence(evidence1)
            SaveInfractionManagerWeb.saveInfractionEvidence(evidence2)
            /* Step 9. */
            val oficial = PersonTownhall(
                Application.prefs.loadData(R.string.sp_id_officer),
                Application.prefs.loadData(R.string.sp_person_name, "") ?: "", "", "",
                SingletonInfraction.idNewInfraction
            )
            //Application.prefs.loadDataInt(R.string.sp_id_officer)!!.toLong()
            SaveInfractionManagerWeb.saveOfficial(oficial)
            /* Notify View That All Data Was Saved */
            if (notify) listener.onDataSaved()
        }
    }

    override fun savePayment(info: TransactionInfo) {
        this.txInfo = info
        /* Step 1. Save Pay Order Into Database */
        val payorder = InfringementPayorder(
            0,
            SingletonInfraction.subTotalInfraction.toFloat(),
            0F,
            SingletonInfraction.discountInfraction.toFloat(),
            0F,
            SingletonInfraction.totalInfraction.toFloat(),
            actualDay,
            "", "", "CARD",
            info.authorization.toLong(),
            SingletonInfraction.idNewInfraction,
            info.reference,
            false,
            "",
            info.al,
            info.affiliation,
            info.bank,
            info.entryType,
            info.noControl,
            Application.prefs.loadData(R.string.sp_prefix, "")!!,
            info.cardOwner,
            info.typeTx,
            info.authorization,
            info.txDate,
            info.txTime
        )
        SaveInfractionManagerWeb.savePayOrder(payorder)
        /* Step 2. Save Authorization Code into Singleton */
        SingletonInfraction.paymentAuthCode = info.authorization
    }

    override fun printTicket(activity: Activity) {
        SingletonTicket.headers =
            SingletonInfraction.townshipInfraction.headers.values.toMutableList()
        SingletonTicket.dateTicket = "$actualDay $actualTime"
        SingletonTicket.folioTicket = newFolio
        SingletonTicket.completeNameOffender =
            SingletonInfraction.nameOffender + " " + SingletonInfraction.lastFatherName + " " + SingletonInfraction.lastMotherName
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
        SingletonTicket.colonyOffender = SingletonInfraction.colonyOffender
        if (SingletonInfraction.stateOffender.documentReference != null) {
            SingletonTicket.stateOffender = SingletonInfraction.stateOffender.value
        }
        if (SingletonInfraction.noLicenseOffender.isNotEmpty()) {
            SingletonTicket.noLicenseOffender = SingletonInfraction.noLicenseOffender
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
            val article = SingletonTicket.ArticleFraction(
                art.article.number,
                art.fraction.number,
                art.fraction.uma.toString(),
                art.motivation
            )
            SingletonTicket.fractionsList.add(article)
        }
        SingletonTicket.streetInfraction = SingletonInfraction.streetInfraction
        SingletonTicket.betweenStreetInfraction = SingletonInfraction.betweenStreet1
        SingletonTicket.andStreetInfraction = SingletonInfraction.betweenStreet2
        SingletonTicket.colonyInfraction = SingletonInfraction.colonnyInfraction
        SingletonTicket.retainedDocumentInfraction = SingletonInfraction.retainedDocument.value
        SingletonTicket.isRemitedInfraction = SingletonInfraction.isRemited
        if (SingletonInfraction.isRemited) {
            SingletonTicket.remitedDispositionInfraction =
                SingletonInfraction.dispositionRemited.value
        }
        SingletonTicket.nameAgent = "${Application.prefs.loadData(R.string.sp_person_name, "")}"
        SingletonTicket.noEmployee = Application.prefs.loadData(R.string.sp_id_officer).toInt()
        SingletonTicket.paymentAuthCode = SingletonInfraction.paymentAuthCode

        captureLineList.forEach {
            SingletonTicket.captureLineList.add(
                SingletonTicket.CaptureLine(
                    it.key,
                    if (it.discount == "0%") "Sin descuento" else "Con ${it.discount} de descuento",
                    it.date,
                    "%.2f".format(it.amount)
                )
            )
        }

        SingletonTicket.footers =
            SingletonInfraction.townshipInfraction.footer.values.toMutableList()
        Ticket.printTicket(activity, object : Ticket.TicketListener {
            override fun onTicketPrint() {
                listener.onTicketPrinted()
            }

            override fun onTicketError() {
                listener.onError(
                    Application.getContext().getString(R.string.pt_e_print_other_error)
                )
            }
        })
    }

    override fun reprintVoucher(
        activity: Activity,
        listener: IPaymentsTransfer.TransactionListener
    ) {
        val voucher = Voucher(
            txInfo.noControl,
            txInfo.maskedPan.substring(txInfo.maskedPan.length - 4, txInfo.maskedPan.length),
            txInfo.expirationDate,
            txInfo.brandCard,
            txInfo.typeCard,
            txInfo.bank,
            txInfo.authorization,
            txInfo.reference,
            txInfo.amount,
            txInfo.cardOwner,
            txInfo.txDate,
            txInfo.txTime,
            txInfo.aid,
            txInfo.tvr,
            txInfo.tsi,
            txInfo.apn,
            txInfo.al,
            txInfo.arqc,
            txInfo.flagTransaction,
            txInfo.needSign,
            PTX_MERCHANT_ID,
            Utils.getImeiDevice(activity),
            txInfo.entryType
        )
        PaymentsTransfer.reprintVoucher(activity, listener, voucher)
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun generateNewFolio(): String {
        val prefix = Application.prefs.loadData(R.string.sp_prefix, "")
        val lastFolio = SaveInfractionManagerWeb.getLastFolioSaved("%$prefix%")
        val incremental: Int = if (lastFolio.isNullOrEmpty()) {
            1
        } else {
            lastFolio.split("-")[1].toInt() + 1
        }

        return "$prefix-$incremental"
    }
}