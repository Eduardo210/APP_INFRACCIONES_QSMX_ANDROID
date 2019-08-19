package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.*

@SuppressLint("StaticFieldLeak")
object SendInfractionManagerWeb {
    fun getInfractionsToSend(): MutableList<InfringementInfringements> {
        return object : AsyncTask<Void, Void, MutableList<InfringementInfringements>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfringementInfringements> {
                return Application?.m_database_web?.infractionDaoWeb()?.selectInfractionsToSend()
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun getInfractionPictures(idInfraction: Long): MutableList<InfringementPicturesInfringement> {
        return object : AsyncTask<Void, Void, MutableList<InfringementPicturesInfringement>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfringementPicturesInfringement> {
                return Application.m_database_web?.pictureInfractionDaoWeb()?.selectPicturesToSend(idInfraction)
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun getInfractionAddress(idInfraction: Long): InfringementAddressInfringement {
        return object : AsyncTask<Void, Void, InfringementAddressInfringement>() {
            override fun doInBackground(vararg p0: Void?): InfringementAddressInfringement {
                return Application.m_database_web?.addressInfringementDaoWeb()?.selectInfractionAddres(idInfraction)
                        ?: InfringementAddressInfringement(0, "", "", "", "", "", "", "", "", 0.0, 0.0)
            }
        }.execute().get()
    }

    fun getInfractionMotivationList(idInfraction: Long): MutableList<InfringementRelfractionInfringements> {
        return object : AsyncTask<Void, Void, MutableList<InfringementRelfractionInfringements>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfringementRelfractionInfringements> {
                return Application.m_database_web?.infractionFractionDaoWeb()?.selectByArticle(idInfraction)
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun getPersonLicense(idDriver: Long): DriverDriverLicense {
        return object : AsyncTask<Void, Void, DriverDriverLicense>() {
            override fun doInBackground(vararg p0: Void?): DriverDriverLicense {
                return Application.m_database_web?.driverLicenseDaoWeb()?.selectDriverLicense(idDriver)
                        ?: DriverDriverLicense(0, "", 0, "", "")
            }
        }.execute().get()
    }

    fun getPersonAddress(idDriver: Long): DriverAddressDriver {
        return object : AsyncTask<Void, Void, DriverAddressDriver>() {
            override fun doInBackground(vararg p0: Void?): DriverAddressDriver {
                return Application.m_database_web?.addressPersonDaoWeb()?.selectPersonAddress(idDriver)
                        ?: DriverAddressDriver(0, "", "", "", "", "", "", "", "")
            }
        }.execute().get()
    }

    fun getPersonInformation(idInfraction: Long): DriverDrivers {
        return object : AsyncTask<Void, Void, DriverDrivers>() {
            override fun doInBackground(vararg p0: Void?): DriverDrivers {
                return Application.m_database_web?.personDaoWeb()?.selectPersonInfo(idInfraction)
                        ?: DriverDrivers(0, "", "", "", "")
            }
        }.execute().get()
    }

    fun getVehicleInformation(idInfraction: Long): VehicleVehicles {
        return object : AsyncTask<Void, Void, VehicleVehicles>() {
            override fun doInBackground(vararg p0: Void?): VehicleVehicles {
                return Application.m_database_web?.vehicleInfractionDaoWeb()?.selectVehicleOfInfraction(idInfraction)
                        ?: VehicleVehicles(0, "", "", false, "", "", "", false, "", "", "", "")
            }
        }.execute().get()
    }
}