package mx.qsistemas.infracciones.modules.create.fr_offender

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class OffenderIterator(val listener: OffenderContracts.Presenter) : OffenderContracts.Iterator {

    private val actualDay = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var nonWorkingDays: MutableList<NonWorkingDay>
    internal lateinit var statesList: MutableList<States>
    internal lateinit var townshipsList: MutableList<Townships>
    internal lateinit var licenseTypeList: MutableList<LicenseType>

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
        val config = SaveInfractionManager.getConfig()
        /* Calculate infraction article variables */
        var totalImport = 0F
        var totalPoints = 0
        var totalUmas = 0
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
        val fifteenthDay = getFutureWorkingDay(15)
        val thirtythDay = getFutureWorkingDay(30)
        val newFolio = generateNewFolio()
        /* Generate all banking capture lines */
        val captureLine1 = Utils.generateCaptureLine(newFolio.replace("-", ""), fifteenthDay, "%.2f".format(fiftiethDiscount), "2")
        val captureLine2 = Utils.generateCaptureLine(newFolio.replace("-", ""), thirtythDay, "%.2f".format(totalImport), "2")
        /* Step 1. Save the infraction information */
        val infraction = Infraction(0, newFolio, SingletonInfraction.noLicenseOffender, SingletonInfraction.typeLicenseOffender.id, SingletonInfraction.stateIssuedIn.value,
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