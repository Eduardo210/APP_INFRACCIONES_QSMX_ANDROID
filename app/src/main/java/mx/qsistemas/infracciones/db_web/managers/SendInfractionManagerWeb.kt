package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.*
import java.util.concurrent.Executors

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
                        ?: InfringementAddressInfringement(0, "", "", "", "", "",
                                "", "", "", "", "", "", 0, 0.0,
                                0.0)
            }
        }.execute().get()
    }

    fun getInfractionMotivationList(idInfraction: Long): MutableList<InfringementRelfractionInfringements> {
        return object : AsyncTask<Void, Void, MutableList<InfringementRelfractionInfringements>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfringementRelfractionInfringements> {
                return Application.m_database_web?.infractionFractionDaoWeb()?.selectFractions(idInfraction)
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun getPersonLicense(idInfraction: Long): DriverDriverLicense {
        return object : AsyncTask<Void, Void, DriverDriverLicense>() {
            override fun doInBackground(vararg p0: Void?): DriverDriverLicense {
                return Application.m_database_web?.driverLicenseDaoWeb()?.selectDriverLicense(idInfraction)
                        ?: DriverDriverLicense(0, "", 0, "", "", "", "")
            }
        }.execute().get()
    }

    fun getPersonAddress(idDriver: Long): DriverAddressDriver {
        return object : AsyncTask<Void, Void, DriverAddressDriver>() {
            override fun doInBackground(vararg p0: Void?): DriverAddressDriver {
                return Application.m_database_web?.addressPersonDaoWeb()?.selectPersonAddress(idDriver)
                        ?: DriverAddressDriver(0, "", "", "", "", "", "", "", "", "", "", "", "")
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

    fun getPayerInformation(idInfraction: Long): ElectronicBill {
        return object : AsyncTask<Void, Void, ElectronicBill>() {
            override fun doInBackground(vararg p0: Void?): ElectronicBill {
                return Application.m_database_web?.electronicBillDaoWeb()?.selectElectronicBill(idInfraction)
                        ?: ElectronicBill(0, "", "", "", "", "", 0, "")
            }
        }.execute().get()
    }

    fun getVehicleInformation(idInfraction: Long): VehicleVehicles {
        return object : AsyncTask<Void, Void, VehicleVehicles>() {
            override fun doInBackground(vararg p0: Void?): VehicleVehicles {
                return Application.m_database_web?.vehicleInfractionDaoWeb()?.selectVehicle(idInfraction)
                        ?: VehicleVehicles(0, "", "", "", false, "", "",
                                "", "", "", "", false, "",
                                "", "", "", "", "", "")
            }
        }.execute().get()
    }

    fun getCaptureLines(idInfraction: Long): MutableList<InfringementCapturelines> {
        return object : AsyncTask<Void, Void, MutableList<InfringementCapturelines>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfringementCapturelines> {
                return Application.m_database_web?.captureLineDaoWeb()?.selectCaptureLine(idInfraction)
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun getPayments(): MutableList<InfringementPayorderToSend> {
        return object : AsyncTask<Void, Void, MutableList<InfringementPayorderToSend>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfringementPayorderToSend> {
                return Application.m_database_web?.payorderDaoWeb()?.selectToSend()
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun updateInfractionSend(tokenServer: String, folio: String) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database_web?.infractionDaoWeb()?.updateSendByFolio(tokenServer, folio)
        }
    }

    fun updatePaymentSend(idPayment: Long) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database_web?.payorderDaoWeb()?.update(idPayment)
        }
    }
}