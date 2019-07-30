package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
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

}