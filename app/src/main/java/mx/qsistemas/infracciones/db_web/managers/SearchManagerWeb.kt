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
        object : AsyncTask<Void, Void, MutableList<InfractionItem>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfractionItem>? {
                return Application.m_database_web?.infractionDaoWeb()?.searchResumeInfraction(query)
            }
        }.execute().get()
    }

    fun getAddressInfringement(idInfraction: Long): InfringementAddressInfringement = runBlocking {
        object : AsyncTask<Void, Void, InfringementAddressInfringement>() {
            override fun doInBackground(vararg p0: Void?): InfringementAddressInfringement {
                return Application.m_database_web?.addressInfringementDaoWeb()?.selectInfractionAddres(idInfraction)
                        ?: InfringementAddressInfringement(0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                0,
                                0.0,
                                0.0)
            }
        }.execute().get()
    }

    fun getAddressDriver(idInfraction: Long): DriverAddressDriver = runBlocking {
        object : AsyncTask<Void, Void, DriverAddressDriver>() {
            override fun doInBackground(vararg p0: Void?): DriverAddressDriver {
                return Application.m_database_web?.addressPersonDaoWeb()?.selectPersonAddress(idInfraction)
                        ?: DriverAddressDriver(0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "")
            }
        }.execute().get()

    }

    fun getDriverLicense(idInfraction: Long): DriverDriverLicense = runBlocking {
        object : AsyncTask<Void, Void, DriverDriverLicense>() {
            override fun doInBackground(vararg p0: Void?): DriverDriverLicense? {
                return Application.m_database_web?.driverLicenseDaoWeb()?.selectDriverLicense(idInfraction)
                        ?: DriverDriverLicense(0, "", 0, "", "","", "")
            }
        }.execute().get()
    }

    fun getDriverDriver(idInfraction: Long): DriverDrivers = runBlocking {
        object : AsyncTask<Void, Void, DriverDrivers>() {
            override fun doInBackground(vararg p0: Void?): DriverDrivers {
                return Application.m_database_web?.personDaoWeb()?.selectPersonInfo(idInfraction)
                        ?: DriverDrivers(0, "", "", "", "")
            }
        }.execute().get()
    }

    fun getCaptureLines(idInfraction: Long): MutableList<InfringementCapturelines> = runBlocking {
        object : AsyncTask<Void, Void, MutableList<InfringementCapturelines>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfringementCapturelines> {
                return Application.m_database_web?.captureLineDaoWeb()?.selectCaptureLine(idInfraction)!!
            }
        }.execute().get()
    }

    fun getInfraction(idInfraction: Long): InfringementInfringements = runBlocking {
        object : AsyncTask<Void, Void, InfringementInfringements>() {
            override fun doInBackground(vararg p0: Void?): InfringementInfringements {
                return Application.m_database_web?.infractionDaoWeb()?.selectInfraction(idInfraction)!!
            }
        }.execute().get()
    }

    fun getFractionsInfringements(idInfraction: Long): MutableList<InfringementRelfractionInfringements> = runBlocking {
        object : AsyncTask<Void, Void, MutableList<InfringementRelfractionInfringements>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfringementRelfractionInfringements> {
                return Application.m_database_web?.infractionFractionDaoWeb()?.selectFractions(idInfraction)!!
            }
        }.execute().get()
    }

    fun getVehicle(idInfraction: Long): VehicleVehicles = runBlocking {
        object : AsyncTask<Void, Void, VehicleVehicles>() {
            override fun doInBackground(vararg p0: Void?): VehicleVehicles {
                return Application.m_database_web?.vehicleInfractionDaoWeb()?.selectVehicle(idInfraction)!!
            }
        }.execute().get()
    }
    fun getTownHallPerson(idInfraction: Long): PersonTownhall = runBlocking {
        object : AsyncTask<Void, Void, PersonTownhall>() {
            override fun doInBackground(vararg p0: Void?): PersonTownhall {
                return Application.m_database_web?.personTownHallDaoWeb()?.selectTownPerson(idInfraction)!!
            }
        }.execute().get()
    }


}