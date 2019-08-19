package mx.qsistemas.infracciones.db.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.*
import java.util.concurrent.Executors

@SuppressLint("StaticFieldLeak")
object SaveInfractionManager {

    /*fun getConfig(): Config {
        return object : AsyncTask<Void, Void, Config>() {
            override fun doInBackground(vararg p0: Void?): Config {
                return Application.m_database?.configDao()?.selectFirstConfig()!!
            }
        }.execute().get()
    }



    fun getLastFolioSaved(prefix: String): String {
        return object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                return Application.m_database?.infractionDao()?.selectLastFolio(prefix)
                        ?: "$prefix-0".replace("%", "")
            }
        }.execute().get()
    }*/
    fun getNonWorkingDays(): MutableList<NonWorkingDay> {
        return object : AsyncTask<Void, Void, MutableList<NonWorkingDay>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<NonWorkingDay> {
                return Application.m_database?.nonWorkingDayDao()?.selectFutureDays()!!
            }
        }.execute().get()
    }

    fun insertInfraction(infraction: Infraction): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database?.infractionDao()?.insert(infraction)!!
            }
        }.execute().get()
    }

    fun getSubBrandExist(subBrand: String, idBrand: Int): SubmarkingVehicle {
        return object : AsyncTask<Void, Void, SubmarkingVehicle>() {
            override fun doInBackground(vararg p0: Void?): SubmarkingVehicle {
                return Application.m_database?.submarkingVehicleDao()?.selectSubBrandByText(subBrand, idBrand)
                        ?: SubmarkingVehicle(0, 0, "")
            }
        }.execute().get()
    }

    fun saveSubBrandVehicle(subBrand: SubmarkingVehicle): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database?.submarkingVehicleDao()?.insertNewSubBrand(subBrand)!!
            }
        }.execute().get()
    }

    fun saveVehicleInfraction(vehicleInfraction: VehicleInfraction): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database?.vehicleInfractionDao()?.insert(vehicleInfraction)!!
            }
        }.execute().get()
    }

    fun savePersonInformation(person: Person): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database?.personDao()?.insert(person)!!
            }
        }.execute().get()
    }

    fun savePersonInfractionRelation(personInfringement: PersonInfringement) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.personInfringementDao()?.insert(personInfringement)
        }
    }

    fun saveAddress(address: Address): Long {
        return object : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                return Application.m_database?.addressDao()?.insert(address)!!
            }
        }.execute().get()
    }

    fun savePersonAddressRelation(addressPerson: AddressPerson) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.addressPersonDao()?.insert(addressPerson)
        }
    }

    fun saveInfractionAddressRelation(addressInfringement: AddressInfringement) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.addressInfringementDao()?.insert(addressInfringement)
        }
    }

    fun saveTrafficViolation(trafficViolationFraction: TrafficViolationFraction) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.trafficViolationDao()?.insert(trafficViolationFraction)
        }
    }

    fun saveInfractionEvidence(infracionEvidence: InfractionEvidence) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.infractionEvidenceDao()?.insert(infracionEvidence)
        }
    }

    fun savePaymentInfringement(paymentInfringement: PaymentInfringement) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.paymentInfringementDao()?.insert(paymentInfringement)
        }
    }

    fun savePaymentInfringementCard(paymentInfringementCard: PaymentInfringementCard) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.paymentInfringementCardDao()?.insert(paymentInfringementCard)
        }
    }

    fun updateInfrationToPaid(idInfraction: Long) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database?.infractionDao()?.updatePaidById(idInfraction)
        }
    }
}