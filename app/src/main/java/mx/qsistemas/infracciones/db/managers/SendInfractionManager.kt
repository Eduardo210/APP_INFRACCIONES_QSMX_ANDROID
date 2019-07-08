package mx.qsistemas.infracciones.db.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.*
import java.util.concurrent.Executors

@SuppressLint("StaticFieldLeak")
object SendInfractionManager {

    fun getInfractionsToSend(): MutableList<Infraction> {
        return object : AsyncTask<Void, Void, MutableList<Infraction>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<Infraction> {
                return Application?.m_database?.infractionDao()?.selectInfractionsToSend()
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun getInfractionAddress(idInfraction: Long): Address {
        return object : AsyncTask<Void, Void, Address>() {
            override fun doInBackground(vararg p0: Void?): Address {
                return Application.m_database?.addressDao()?.selectInfractionAddres(idInfraction)
                        ?: Address(0, 1, 0, 0, "", "", "", "",
                                0, "", "", 0.0, 0.0)
            }
        }.execute().get()
    }

    fun getInfractionMotivationList(idInfraction: Long): MutableList<TrafficViolationFraction> {
        return object : AsyncTask<Void, Void, MutableList<TrafficViolationFraction>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<TrafficViolationFraction> {
                return Application.m_database?.trafficViolationDao()?.selectViolationsByInfraction(idInfraction)
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun getPersonAddress(idInfraction: Long): Address {
        return object : AsyncTask<Void, Void, Address>() {
            override fun doInBackground(vararg p0: Void?): Address {
                return Application.m_database?.addressDao()?.selectPersonAddress(idInfraction)
                        ?: Address(0, 1, 0, 0, "", "", "", "",
                                0, "", "", 0.0, 0.0)
            }
        }.execute().get()
    }

    fun getPersonInformation(idInfraction: Long): Person {
        return object : AsyncTask<Void, Void, Person>() {
            override fun doInBackground(vararg p0: Void?): Person {
                return Application.m_database?.personDao()?.selectPersonInfo(idInfraction)
                        ?: Person(0, "", "", "", "")
            }
        }.execute().get()
    }

    fun getPaymentInfrigment(idInfraction: Long): PaymentInfringement {
        return object : AsyncTask<Void, Void, PaymentInfringement>() {
            override fun doInBackground(vararg p0: Void?): PaymentInfringement {
                return Application.m_database?.paymentInfringementDao()?.selectPaymentOfInfraction(idInfraction)
                        ?: PaymentInfringement(0, idInfraction.toInt(), 2, 0F, 0F, 0F,
                                "", "", 0, 0F)
            }
        }.execute().get()
    }

    fun getPaymentTransactionInfo(idInfraction: Long): PaymentInfringementCard {
        return object : AsyncTask<Void, Void, PaymentInfringementCard>() {
            override fun doInBackground(vararg p0: Void?): PaymentInfringementCard {
                return Application.m_database?.paymentInfringementCardDao()?.selectTransactionInfo(idInfraction)
                        ?: PaymentInfringementCard(0, "", "", "", "", "", "",
                                "", "", "", "", 0, "", "", "",
                                "", "", "", "", "", "", "", "",
                                "", "", "", idInfraction)
            }
        }.execute().get()
    }

    fun getVehileInformation(idInfraction: Long): VehicleInfraction {
        return object : AsyncTask<Void, Void, VehicleInfraction>() {
            override fun doInBackground(vararg p0: Void?): VehicleInfraction {
                return Application.m_database?.vehicleInfractionDao()?.selectVehicleOfInfraction(idInfraction)
                        ?: VehicleInfraction(0, 0, "", "", "", "", 0, "",
                                "", 0, "", "", idInfraction.toInt())
            }
        }.execute().get()
    }

    fun updateInfractionToSend(folio: String) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.infractionDao()?.updateSendByFolio(folio)
        }
    }

    fun updatePaymentToSend(idInfraction: Long) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.paymentInfringementDao()?.updateToSend(idInfraction)
        }
    }

    fun getPhotosToSend(): MutableList<InfractionEvidence> {
        return object : AsyncTask<Void, Void, MutableList<InfractionEvidence>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfractionEvidence> {
                return Application.m_database?.infractionEvidenceDao()?.selectPhotosToSend()
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun getFolioOfInfraction(idInfraction: Long): String {
        return object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                return Application.m_database?.infractionDao()?.selectFolioByIdInfraction(idInfraction)
                        ?: ""
            }
        }.execute().get()
    }

    fun getInfractionById(idInfraction: Long): Infraction {
        return object : AsyncTask<Void, Void, Infraction>() {
            override fun doInBackground(vararg p0: Void?): Infraction {
                return Application.m_database?.infractionDao()?.selectByIdInfraction(idInfraction)!!
            }
        }.execute().get()
    }

    fun updateImageToSend(idInfraction: Long) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.infractionEvidenceDao()?.updateSendByIdInfraction(idInfraction)
        }
    }

    fun deleteSendImages() {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.infractionEvidenceDao()?.deleteSendImages()
        }
    }
}