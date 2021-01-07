package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.*

@SuppressLint("StaticFieldLeak")
object SaveInfractionManagerWeb {
    fun insertInfraction(infraction: InfringementInfringements): Long = runBlocking {
        return@runBlocking Application.m_database_web.infractionDaoWeb().insert(infraction)
    }

    fun saveVehicleInfraction(vehicleInfraction: VehicleVehicles): Long = runBlocking {
        return@runBlocking Application.m_database_web.vehicleInfractionDaoWeb().insert(vehicleInfraction)
    }

    fun savePersonInformation(person: DriverDrivers): Long = runBlocking {
        return@runBlocking Application.m_database_web.personDaoWeb().insert(person)
    }

    fun saveAddressPerson(address: DriverAddressDriver): Long = runBlocking {
        return@runBlocking Application.m_database_web.addressPersonDaoWeb().insert(address)
    }

    fun saveAddressInfraction(address: InfringementAddressInfringement): Long = runBlocking {
        return@runBlocking Application.m_database_web.addressInfringementDaoWeb().insert(address)
    }

    fun saveTrafficViolation(trafficViolationFraction: InfringementRelfractionInfringements) {
        GlobalScope.launch {
            Application.m_database_web.infractionFractionDaoWeb().insert(trafficViolationFraction)
        }
    }

    fun getLastFolioSaved(prefix: String): String = runBlocking {
        return@runBlocking Application.m_database_web.infractionDaoWeb().selectLastFolio(prefix)
    }

    fun saveInfractionEvidence(infracionEvidence: InfringementPicturesInfringement) {
        GlobalScope.launch {
            Application.m_database_web.infractionEvidenceDaoWeb().insert(infracionEvidence)
        }
    }

    fun saveDriverLicense(driverLicense: DriverDriverLicense) {
        GlobalScope.launch {
            Application.m_database_web.driverLicenseDaoWeb().insert(driverLicense)
        }
    }

    fun saveCaptureLine(captureLines: MutableList<InfringementCapturelines>) {
        GlobalScope.launch {
            Application.m_database_web.captureLineDaoWeb().insertList(captureLines)
        }
    }

    fun savePayOrder(payorder: InfringementPayorder) {
        GlobalScope.launch {
            Application.m_database_web.payorderDaoWeb().insert(payorder)
        }
    }

    fun saveOfficial(official: PersonTownhall) {
        GlobalScope.launch {
            Application.m_database_web.personTownHallDaoWeb().insert(official)
        }
    }

    fun savePayerInformation(payer: ElectronicBill) {
        GlobalScope.launch {
            Application.m_database_web.electronicBillDaoWeb().insert(payer)
        }
    }
}