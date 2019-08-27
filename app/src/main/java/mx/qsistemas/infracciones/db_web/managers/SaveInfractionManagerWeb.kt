package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.*
import java.util.concurrent.Executors

@SuppressLint("StaticFieldLeak")
object SaveInfractionManagerWeb {
    fun insertInfraction(infraction: InfringementInfringements): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database_web?.infractionDaoWeb()?.insert(infraction)!!
            }
        }.execute().get()
    }

    fun saveVehicleInfraction(vehicleInfraction: VehicleVehicles): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database_web?.vehicleInfractionDaoWeb()?.insert(vehicleInfraction)!!
            }
        }.execute().get()
    }

    fun savePersonInformation(person: DriverDrivers): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database_web?.personDaoWeb()?.insert(person)!!
            }
        }.execute().get()
    }

    fun saveAddressPerson(address: DriverAddressDriver): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database_web?.addressPersonDaoWeb()?.insert(address)!!
            }
        }.execute().get()
    }

    fun saveAddressInfraction(address: InfringementAddressInfringement): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database_web?.addressInfringementDaoWeb()?.insert(address)!!
            }
        }.execute().get()
    }

    fun saveTrafficViolation(trafficViolationFraction: InfringementRelfractionInfringements) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database_web?.infractionFractionDaoWeb()?.insert(trafficViolationFraction)
        }
    }

    fun getLastFolioSaved(prefix: String): String {
        return object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                return Application.m_database_web?.infractionDaoWeb()?.selectLastFolio(prefix)
                        ?: "$prefix-0".replace("%", "")
            }
        }.execute().get()
    }

    fun saveInfractionEvidence(infracionEvidence: InfringementPicturesInfringement) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database_web?.infractionEvidenceDaoWeb()?.insert(infracionEvidence)
        }
    }

    fun saveDriverLicense(driverLicense: DriverDriverLicense) {
        GlobalScope.launch {
            Application.m_database_web?.driverLicenseDaoWeb()?.insert(driverLicense)
        }

    }

    fun saveCaptureLine(captureLines: MutableList<InfringementCapturelines>) {
        GlobalScope.launch {
            Application.m_database_web?.captureLineDaoWeb()?.insertList(captureLines)
        }
    }

    fun savePayOrder(payorder: InfringementPayorder) {
        GlobalScope.launch {
            Application.m_database_web?.payorderDaoWeb()?.insert(payorder)
        }
    }
    fun saveOficial(oficial: PersonTownhall) {
        GlobalScope.launch {
            Application.m_database_web?.personTownHallDaoWeb()?.insert(oficial)
        }
    }

}