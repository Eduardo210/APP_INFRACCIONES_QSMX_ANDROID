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
                return Application.m_database.infractionDao().selectInfractionsToSend()
            }
        }.execute().get()
    }

    fun getInfractionAddress(idInfraction: Long): Address {
        return object : AsyncTask<Void, Void, Address>() {
            override fun doInBackground(vararg p0: Void?): Address {
                return Application.m_database.addressDao().selectInfractionAddres(idInfraction)
            }
        }.execute().get()
    }

    fun getInfractionMotivationList(idInfraction: Long): MutableList<TrafficViolationFraction> {
        return object : AsyncTask<Void, Void, MutableList<TrafficViolationFraction>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<TrafficViolationFraction> {
                return Application.m_database.trafficViolationDao().selectViolationsByInfraction(idInfraction)
            }
        }.execute().get()
    }

    fun getPersonAddress(idInfraction: Long): Address {
        return object : AsyncTask<Void, Void, Address>() {
            override fun doInBackground(vararg p0: Void?): Address {
                return Application.m_database.addressDao().selectPersonAddress(idInfraction)
            }
        }.execute().get()
    }

    fun getPersonInformation(idInfraction: Long): Person {
        return object : AsyncTask<Void, Void, Person>() {
            override fun doInBackground(vararg p0: Void?): Person {
                return Application.m_database.personDao().selectPersonInfo(idInfraction)
            }
        }.execute().get()
    }

    fun getPaymentInfrigment(idInfraction: Long): PaymentInfringement {
        return object : AsyncTask<Void, Void, PaymentInfringement>() {
            override fun doInBackground(vararg p0: Void?): PaymentInfringement {
                return Application.m_database.paymentInfringementDao().selectPaymentOfInfraction(idInfraction)
            }
        }.execute().get()
    }

    fun getPaymentTransactionInfo(idInfraction: Long): PaymentInfringementCard {
        return object : AsyncTask<Void, Void, PaymentInfringementCard>() {
            override fun doInBackground(vararg p0: Void?): PaymentInfringementCard {
                return Application.m_database.paymentInfringementCardDao().selectTransactionInfo(idInfraction)
            }
        }.execute().get()
    }

    fun getVehileInformation(idInfraction: Long): VehicleInfraction {
        return object : AsyncTask<Void, Void, VehicleInfraction>() {
            override fun doInBackground(vararg p0: Void?): VehicleInfraction {
                return Application.m_database.vehicleInfractionDao().selectVehicleOfInfraction(idInfraction)
            }
        }.execute().get()
    }

    fun updateInfractionToSend(folio: String) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.infractionDao().updateSendByFolio(folio)
        }
    }

    fun updatePaymentToSend(idInfraction: Long) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.paymentInfringementDao().updateToSend(idInfraction)
        }
    }

    fun getPhotosToSend(): MutableList<InfractionEvidence> {
        return object : AsyncTask<Void, Void, MutableList<InfractionEvidence>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<InfractionEvidence> {
                return Application.m_database.infractionEvidenceDao().selectPhotosToSend()
            }
        }.execute().get()
    }

    fun getFolioOfInfraction(idInfraction: Long): String {
        return object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                return Application.m_database.infractionDao().selectFolioByIdInfraction(idInfraction)
            }
        }.execute().get()
    }

    fun getInfractionById(idInfraction: Long): Infraction {
        return object : AsyncTask<Void, Void, Infraction>() {
            override fun doInBackground(vararg p0: Void?): Infraction {
                return Application.m_database.infractionDao().selectByIdInfraction(idInfraction)
            }
        }.execute().get()
    }

    fun updateImageToSend(idInfraction: Long) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.infractionEvidenceDao().updateSendByIdInfraction(idInfraction)
        }
    }

    fun deleteSendImages() {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database.infractionEvidenceDao().deleteSendImages()
        }
    }

    fun getPaymentsToSend(): MutableList<PaymentInfringement> {
        return object : AsyncTask<Void, Void, MutableList<PaymentInfringement>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<PaymentInfringement> {
                return Application.m_database.paymentInfringementDao().seletPaymentToSend()
            }
        }.execute().get()
    }

    fun getTransactionToSend(idInfraction: Long): PaymentInfringementCard {
        return object : AsyncTask<Void, Void, PaymentInfringementCard>() {
            override fun doInBackground(vararg p0: Void?): PaymentInfringementCard {
                return Application.m_database.paymentInfringementCardDao().selectTransactionsToSend(idInfraction)
            }
        }.execute().get()
    }
}