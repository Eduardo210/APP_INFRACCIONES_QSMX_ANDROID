package mx.qsistemas.infracciones.modules.create.fr_offender

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.db.managers.SaveInfractionManager
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.FS_COL_STATES
import mx.qsistemas.infracciones.utils.FS_COL_TOWNSHIPS
import mx.qsistemas.infracciones.utils.Utils
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class OffenderIterator(val listener: OffenderContracts.Presenter) : OffenderContracts.Iterator {

    internal lateinit var statesList: MutableList<States>
    internal lateinit var stateIssuedLicenseList: MutableList<States>
    internal lateinit var townshipsList: MutableList<Townships>
    internal lateinit var licenseTypeList: MutableList<LicenseType>

    /* Variable From Saved Infraction */
    private val actualDay = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
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
                statesList.add(States(0, true, "Seleccionar..."))
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(States::class.java)!!
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
                stateIssuedLicenseList.add(States(0, true, "Seleccionar..."))
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(States::class.java)!!
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
        Application.firestore?.collection(FS_COL_TOWNSHIPS)?.whereEqualTo("id_state", statesList[posState].id)?.whereEqualTo("enable", true)?.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                listener?.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (querySnapshot != null) {
                val list = mutableListOf<String>()
                townshipsList = mutableListOf()
                townshipsList.add(Townships(id_town = 0, value = "Seleccionar...", id_state = 0))
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

    override fun getPositionState(obj: States): Int {
        for (i in 0 until statesList.size) {
            if (statesList[i].id == obj.id) {
                return i
            }
        }
        return 0
    }

    override fun getPositionStateLicense(obj: States): Int {
        for (i in 0 until stateIssuedLicenseList.size) {
            if (stateIssuedLicenseList[i].id == obj.id) {
                return i
            }
        }
        return 0
    }

    override fun getPositionTownship(obj: Townships): Int {
        for (i in 0 until townshipsList.size) {
            if (townshipsList[i].id_town == obj.id_town) {
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
            totalImport += config.minimum_salary * it.fraction.minimum_wages
            totalPoints += it.fraction.penalty_points
            totalUmas += it.fraction.minimum_wages
        }
        SingletonInfraction.subTotalInfraction = "%.2f".format(totalImport)
        val fiftiethDiscount = totalImport * .5
        SingletonInfraction.discountInfraction = "%.2f".format(fiftiethDiscount)
        SingletonInfraction.totalInfraction = "%.2f".format(totalImport - fiftiethDiscount)
        /* Get future working days */
        nonWorkingDays = SaveInfractionManager.getNonWorkingDays()
        fifteenthDay = getFutureWorkingDay(15)
        thirtythDay = getFutureWorkingDay(30)
        newFolio = generateNewFolio()
        /* Generate all banking capture lines */
        captureLine1 = Utils.generateCaptureLine(newFolio.replace("-", ""), fifteenthDay, "%.2f".format(fiftiethDiscount), "2")
        captureLine2 = Utils.generateCaptureLine(newFolio.replace("-", ""), thirtythDay, "%.2f".format(totalImport), "2")
        /* Step 1. Save the infraction information */
        val infraction = Infraction(0, newFolio, SingletonInfraction.noLicenseOffender, SingletonInfraction.typeLicenseOffender.id, SingletonInfraction.licenseIssuedInOffender.value,
                "", SingletonInfraction.isRemited.toInt(), SingletonInfraction.retainedDocument.document, totalUmas, totalImport, config.minimum_salary,
                SingletonInfraction.idPersonTownship, actualDay, false.toInt(), 1, 4, SingletonInfraction.idPersonTownship.toInt(), 1, 0,
                SingletonInfraction.typeDocument.id, "", SingletonInfraction.dispositionRemited.id, SingletonInfraction.isPersonAbstent.toInt(), 0,
                "", "", captureLine1, captureLine2, "", fifteenthDay, thirtythDay, "", fiftiethDiscount.toFloat(),
                totalImport, 0F, 1, "", "", 0F, false)
        SingletonInfraction.idNewInfraction = SaveInfractionManager.insertInfraction(infraction)
        /* Step 2. Validate that sub brand doesn't exists */
        val brandExisting = SaveInfractionManager.getSubBrandExist(SingletonInfraction.subBrandVehicle, SingletonInfraction.brandVehicle.id)
        // If sub brand not exist, then save it to future possible uses
        if (brandExisting.id_submarking_vehicle == 0) {
            val submarkingVehicle = SubmarkingVehicle(0, SingletonInfraction.brandVehicle.id, SingletonInfraction.subBrandVehicle)
            SaveInfractionManager.saveSubBrandVehicle(submarkingVehicle)
        }
        /* Step 3. Save Vehicle Information */
        val vehicleInfraction = VehicleInfraction(0, SingletonInfraction.brandVehicle.id, SingletonInfraction.subBrandVehicle, SingletonInfraction.typeVehicle.type_string,
                SingletonInfraction.colorVehicle, SingletonInfraction.yearVehicle, SingletonInfraction.identifierDocument.id, SingletonInfraction.noDocument,
                SingletonInfraction.stateIssuedIn.value, SingletonInfraction.idPersonTownship, "", SingletonInfraction.noCirculationCard, SingletonInfraction.idNewInfraction.toInt())
        SaveInfractionManager.saveVehicleInfraction(vehicleInfraction)
        /* Step 4. Save Person Information */
        val person = Person(0, SingletonInfraction.nameOffender, SingletonInfraction.lastFatherName, SingletonInfraction.lastMotherName, SingletonInfraction.rfcOffenfer)
        val idNewPerson = SaveInfractionManager.savePersonInformation(person)
        /* Step 5. Save Person-Infraction Relation */
        SaveInfractionManager.savePersonInfractionRelation(PersonInfringement(SingletonInfraction.idNewInfraction.toInt(), idNewPerson))
        /* Step 6. Save Person Address If Offender Was On The Moment */
        if (!SingletonInfraction.isPersonAbstent) {
            val personAddress = Address(0, 0, SingletonInfraction.stateOffender.id, SingletonInfraction.townshipOffender.id_town, SingletonInfraction.colonyOffender,
                    "", SingletonInfraction.noExtOffender, SingletonInfraction.noIntOffender, SingletonInfraction.idPersonTownship, "", "", 0.0, 0.0)
            val idNewPersonAddress = SaveInfractionManager.saveAddress(personAddress)
            /* Step 6:1. Save Person-Address Relation */
            SaveInfractionManager.savePersonAddressRelation(AddressPerson(idNewPersonAddress.toInt(), idNewPerson))
        }
        /* Step 7. Save Address Information */
        val infractionAddress = Address(0, 1, 0, 0, SingletonInfraction.colonnyInfraction, SingletonInfraction.streetInfraction,
                "", "", SingletonInfraction.idPersonTownship, SingletonInfraction.betweenStreet1, SingletonInfraction.betweenStreet2,
                SingletonInfraction.latitudeInfraction, SingletonInfraction.longitudeInfraction)
        val idInfractionAddress = SaveInfractionManager.saveAddress(infractionAddress)
        /* Step 7:1. Save Infraction-Address Relation */
        SaveInfractionManager.saveInfractionAddressRelation(AddressInfringement(idInfractionAddress.toInt(), SingletonInfraction.idNewInfraction.toInt()))
        /* Step 8. Save Fractions List */
        SingletonInfraction.motivationList.forEach {
            val trafficViolation = TrafficViolationFraction(0, SingletonInfraction.idNewInfraction, it.fraction.id.toInt(), it.fraction.minimum_wages, SingletonInfraction.idPersonTownship, it.fraction.penalty_points, it.motivation)
            SaveInfractionManager.saveTrafficViolation(trafficViolation)
        }
        /* Step 9. Save Evidence Photos */
        val evidencePhoto = InfractionEvidence(0, SingletonInfraction.idNewInfraction.toInt(), SingletonInfraction.evidence1, SingletonInfraction.evidence2)
        SaveInfractionManager.saveInfractionEvidence(evidencePhoto)
        /* Notify View That All Data Was Saved */
        if (notify) listener.onDataSaved()
    }

    override fun updateData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun savePayment(info: TransactionInfo) {
        /* Step 1. Save Payment Infraction */
        val paymentInfringement = PaymentInfringement(0, SingletonInfraction.idNewInfraction.toInt(), 2, SingletonInfraction.subTotalInfraction.toFloat(), SingletonInfraction.discountInfraction.toFloat(),
                SingletonInfraction.totalInfraction.toFloat(), info.authorization, "", SingletonInfraction.idPersonTownship, 0F)
        SaveInfractionManager.savePaymentInfringement(paymentInfringement)
        /* Step 2. Save Payment Transaction Information */
        val paymentInfringementCard = PaymentInfringementCard(0, info.aid, info.appLabel, info.arqc, info.authorization, info.entryType, info.maskedPan,
                info.txDate, "", info.txTime, Application.prefs?.loadData(R.string.sp_prefix, "")!!, SingletonInfraction.idPersonTownship, info.affiliation, info.expirationDate, "Aprobado", info.brandCard,
                info.typeCard, info.bank, info.reference, SingletonInfraction.totalInfraction, info.tvr, info.tsi, info.noControl, info.cardOwner,
                "", info.typeTx)
        SaveInfractionManager.savePaymentInfringementCard(paymentInfringementCard)
        /* Step 3. Update Infraction To Paid */
        SaveInfractionManager.updateInfrationToPaid(SingletonInfraction.idNewInfraction)
    }

    override fun printTicket(activity: Activity) {
        val printJson = JSONObject()
        val normal_size = "2"
        val printTest = JSONArray()
        val infractor_data = StringBuilder()
        val card_data = StringBuilder()
        val vehicle = StringBuilder()
        val address_infra = StringBuilder()
        val retained_doc = StringBuilder()
        val responsible = StringBuilder()
        val data_footer = StringBuilder()

        val header1 = "QSISTEMAS_I\n"
        val header2 = "QSISTEMAS_II\n"
        val header3 = "QSISTEMAS_III\n"
        val header4 = "QSISTEMAS_IV\n"
        val header5 = "QSISTEMAS_V\n"
        val header6 = "QSISTEMAS_VI\n"
        val footer1 = "QSISTEMAS_VII"
        val footer2 = "QSISTEMAS_VIII"
        val footer3 = "QSISTEMAS_IX"

        //Impresión de encabezados
        printTest.put(getPrintObject(header1 + header2 + header3 + header4 + header5 + header6, normal_size, "center", "1"))
        printTest.put(getPrintObject("\n\nDETECCION Y LEVANTAMIENTO ELECTRONICO DE INFRACCIONES A CONDUCTORES DE VEHICULOS QUE CONTRAVENGAN LAS DISPOSICIONES EN MATERIA DE TRANSITO, EQUILIBRIO ECOLOGICO, PROTECCIÓN AL AMBIENTE Y PARA LA PREVENCION Y CONTROL DE LA CONTAMINACION, ASI COMO PAGO DE SANCIONES Y APLICACION DE MEDIDAS DE SEGURIDAD.\n\nEL C. AGENTE QUE SUSCRIBE LA PRESENTE BOLETA DE INFRACCION, ESTA FACULTADO EN TERMINOS DE LOS QUE SE ESTABLECE EN LOS ARTICULOS 21 Y 115, FRACCION III, INCISO H), DE LA CONSTITUCION  POLITICA DE LOS ESTADOS UNIDOS MEXICANOS DE ACUERDO A LO ESTABLECIDO EN LOS ARTICULOS 8.3, 8.10, 8.18, 8.19 BIS, 8.19 TERCERO Y 8.19 CUARTO, DEL CODIGO ADMINISTRATIVO DEL ESTADO DE MEXICO. ASI COMO HACER CONSTAR LOS HECHOS QUE MOTIVAN LA INFRACCION EN TERMINOS DEL ARTICULO 16 DE NUESTRA CARTA MAGNA.\n\n\n", normal_size, "left", "0"))
        printTest.put(getPrintObject("$actualDay\nFOLIO: $newFolio\n\n\n", normal_size, "right", "0"))

        //Datos del infractor
        if (!SingletonInfraction.isPersonAbstent) {
            infractor_data.append(SingletonInfraction.nameOffender + " " + SingletonInfraction.lastFatherName + " " + SingletonInfraction.lastMotherName)
            if (SingletonInfraction.rfcOffenfer.isNotEmpty()) {
                infractor_data.append("\n" + SingletonInfraction.rfcOffenfer)
            }
            if (SingletonInfraction.noExtOffender.isNotEmpty()) {
                infractor_data.append("\n" + SingletonInfraction.noExtOffender)
            }
            if (SingletonInfraction.noIntOffender.isNotEmpty()) {
                infractor_data.append("\n" + SingletonInfraction.noIntOffender)
            }
            if (SingletonInfraction.colonyOffender.isNotEmpty()) {
                infractor_data.append("\n" + SingletonInfraction.colonyOffender)
            }
            printTest.put(getPrintObject(infractor_data.toString(), normal_size, "center", "0"))
            if (SingletonInfraction.noCirculationCard.isNotEmpty()) {
                card_data.append("\n" + "LICENCIA/PERMISO: " + SingletonInfraction.noCirculationCard)
            }
            if (SingletonInfraction.typeLicenseOffender.id != 0) {
                card_data.append("\n" + "TIPO LICENCIA: " + SingletonInfraction.typeLicenseOffender.license_type)
            }
            if (SingletonInfraction.licenseIssuedInOffender.id != 0) {
                card_data.append("\n" + "EXPEDIDA: " + SingletonInfraction.licenseIssuedInOffender.value)
            }
            printTest.put(getPrintObject(card_data.toString(), normal_size, "left", "0"))
        } else {
            printTest.put(getPrintObject(SingletonInfraction.nameOffender + " " + SingletonInfraction.lastFatherName + " " + SingletonInfraction.lastMotherName + "\n\n", normal_size, "center", "0"))
        }
        //Características del vehículo
        vehicle.append("\nCARACTERÍSTICAS DEL VEHÍCULO: ")
        vehicle.append("\nMARCA: " + SingletonInfraction.brandVehicle.vehicle_brand)
        if (SingletonInfraction.subBrandVehicle.isNotEmpty()) {
            vehicle.append("\nSUBMARCA: " + SingletonInfraction.subBrandVehicle)
        }
        vehicle.append("\nTIPO: " + SingletonInfraction.typeVehicle.type_string)
        vehicle.append("\nCOLOR: " + SingletonInfraction.colorVehicle)
        vehicle.append("\nMODELO: " + SingletonInfraction.yearVehicle)
        vehicle.append("\nIDENTIFICADOR: " + SingletonInfraction.identifierDocument.document)
        vehicle.append("\nNUMERO: " + SingletonInfraction.noDocument)
        vehicle.append("\nAUTORIDAD QUE EXPIDE: " + SingletonInfraction.typeDocument.authority)
        vehicle.append("\nEXPEDIDO: " + SingletonInfraction.stateIssuedIn.value)
        vehicle.append("\nARTICULOS DEL REGLAMENTO DE TRÁNSITO DEL ESTADO DE MÉXICO: ")

        printTest.put(getPrintObject(vehicle.toString(), normal_size, "left", "0"))
        //Artículos y fracciones
        printTest.put(getPrintObject("\n\nARTICULO/FRACCION\t\t\tU.M.A.\t\t\tPUNTOS\n*******************", "1", "center", "1"))

        SingletonInfraction.motivationList.forEach { art ->
            printTest.put(getPrintObject(art.article.article + "/" + art.fraction.fraccion + "\t\t\t" + art.fraction.minimum_wages + "\t\t\t" + art.fraction.penalty_points, normal_size, "center", "0"))
            printTest.put(getPrintObject("\nCONDUCTA QUE MOTIVA LA INFRACCIÓN: ${art.motivation}", "1", "left", "0"))
        }
        address_infra.append("\n\nCALLE: " + SingletonInfraction.streetInfraction)
        address_infra.append("\nENTRE: " + SingletonInfraction.betweenStreet1)
        address_infra.append("\nY: " + SingletonInfraction.betweenStreet2)
        address_infra.append("\nCOLONIA: " + SingletonInfraction.colonnyInfraction)

        printTest.put(getPrintObject(address_infra.toString(), normal_size, "left", "0"))

        //Documento que se retiene

        retained_doc.append("\n\nDOCUMENTO QUE SE RETIENE: ")
        retained_doc.append("\n" + SingletonInfraction.retainedDocument.document)
        if (SingletonInfraction.isRemited) {
            retained_doc.append("\nREMISION DEL VEHICULO: SI")
            retained_doc.append("\n" + SingletonInfraction.dispositionRemited.disposition)
        }
        printTest.put(getPrintObject(retained_doc.toString(), normal_size, "left", "0"))

        //Responsable del vehíiculo
        printTest.put(getPrintObject("RESPONSABLE DEL VEHÍCULO", normal_size, "center", "1"))
        responsible.append("\n\n" + SingletonInfraction.nameOffender + " " + SingletonInfraction.lastFatherName + " " + SingletonInfraction.lastMotherName + "\n")
        responsible.append("\nRECIBO DE CONFORMIDAD\n\n\n\n")
        responsible.append("\nFIRMA\n")
        responsible.append("\nAGENTE: \n")
        responsible.append("${Application.prefs?.loadData(R.string.sp_person_f_last_name, "")} ${Application.prefs?.loadData(R.string.sp_person_m_last_name, "")} ${Application.prefs?.loadData(R.string.sp_person_name, "")}")
        responsible.append("\nEMPLEADO: ${Application.prefs?.loadData(R.string.sp_no_employee, "")}\n\n") //TODO: reemplazar

        printTest.put(getPrintObject(responsible.toString(), normal_size, "center", "0"))
        printTest.put(getPrintObject("\nFIRMA\n\n", normal_size, "center", "0"))
        printTest.put(getPrintObject("\n\n\n", normal_size, "center", "0"))
        printTest.put(getPrintObject("\n\n\n", normal_size, "center", "0"))

        //Descuento con el 70%
        /* printTest.put(getPrintBarCode(captureLine1))
         printTest.put(getPrintObject(captureLine1, normal_size, "center", "0"))
         printTest.put(getPrintObject("\nCON 70% DE DESCUENTO\nVIGENCIA: $fifteenthDay \nIMPORTE: ${SingletonInfraction.totalInfraction}\n", normal_size, "center", "0"))*/

        //Descuento con el 50%
        printTest.put(getPrintBarCode(captureLine1))
        printTest.put(getPrintObject(captureLine1, normal_size, "center", "0"))
        printTest.put(getPrintObject("\nCON 50% DE DESCUENTO\nVIGENCIA: $fifteenthDay \nIMPORTE: ${SingletonInfraction.totalInfraction}\n\n", normal_size, "center", "0"))

        //Sin descuento
        printTest.put(getPrintBarCode(captureLine2))
        printTest.put(getPrintObject(captureLine2, normal_size, "center", "0"))
        printTest.put(getPrintObject("\nSIN DESCUENTO\nVIGENCIA: $thirtythDay \nIMPORTE: ${SingletonInfraction.subTotalInfraction}\n", normal_size, "center", "0"))

        data_footer.append("\n\n\n$footer1")
        data_footer.append("\n$footer2")
        data_footer.append("\n$footer3")
        data_footer.append("\n\n-AVISO DE PRIVACIDAD-\n\n")
        data_footer.append("\nFUENTE DE CAPTURA: SIIP\n\n\n")
        printTest.put(getPrintObject(data_footer.toString(), normal_size, "center", "0"))
        printJson.put("spos", printTest)
        PaymentsTransfer.print(activity, printJson.toString(), null, object : IPaymentsTransfer.PrintListener {
            override fun onStart() {
            }

            override fun onError(var1: Int, var2: String) {
            }

            override fun onFinish() {
                listener.onTicketPrinted()
            }
        })
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

    private fun getPrintObject(text: String, size: String, position: String, bold: String): JSONObject {
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

    private fun getPrintBarCode(text: String): JSONObject {
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