package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.runBlocking
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.*


@SuppressLint("StaticFieldLeak")
object SearchManagerWeb {
    fun getItemInfraction(query: SupportSQLiteQuery): MutableList<InfractionItem> = runBlocking {
        return@runBlocking Application.m_database_web.infractionDaoWeb().searchResumeInfraction(query)
    }

    fun getAddressInfringement(idInfraction: Long): InfringementAddressInfringement = runBlocking {
        return@runBlocking Application.m_database_web.addressInfringementDaoWeb().selectInfractionAddres(idInfraction)
    }

    fun getAddressDriver(idInfraction: Long): DriverAddressDriver = runBlocking {
        return@runBlocking Application.m_database_web.addressPersonDaoWeb().selectPersonAddress(idInfraction)
    }

    fun getDriverLicense(idInfraction: Long): DriverDriverLicense = runBlocking {
        return@runBlocking Application.m_database_web.driverLicenseDaoWeb().selectDriverLicense(idInfraction)
    }

    fun getDriverDriver(idInfraction: Long): DriverDrivers = runBlocking {
        return@runBlocking Application.m_database_web.personDaoWeb().selectPersonInfo(idInfraction)
    }

    fun getCaptureLines(idInfraction: Long): MutableList<InfringementCapturelines> = runBlocking {
        return@runBlocking Application.m_database_web.captureLineDaoWeb().selectCaptureLine(idInfraction)
    }

    fun getInfraction(idInfraction: Long): InfringementInfringements = runBlocking {
        return@runBlocking Application.m_database_web.infractionDaoWeb().selectInfraction(idInfraction)
    }

    fun getFractionsInfringements(idInfraction: Long): MutableList<InfringementRelfractionInfringements> = runBlocking {
        return@runBlocking Application.m_database_web.infractionFractionDaoWeb().selectFractions(idInfraction)
    }

    fun getVehicle(idInfraction: Long): VehicleVehicles = runBlocking {
        return@runBlocking Application.m_database_web.vehicleInfractionDaoWeb().selectVehicle(idInfraction)
    }

    fun getTownHallPerson(idInfraction: Long): PersonTownhall = runBlocking {
        return@runBlocking Application.m_database_web.personTownHallDaoWeb().selectTownPerson(idInfraction)
    }

    fun getPayOrder(idInfraction: Long): InfringementPayorder = runBlocking {
        return@runBlocking Application.m_database_web.payorderDaoWeb().selectPayOrder(idInfraction)
    }

    fun getElectronicBill(idInfraction: Long): ElectronicBill = runBlocking {
        return@runBlocking Application.m_database_web.electronicBillDaoWeb().selectElectronicBill(idInfraction)
    }
}