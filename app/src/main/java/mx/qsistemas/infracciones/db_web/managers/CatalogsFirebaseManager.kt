package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.City
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.utils.Validator


@SuppressLint("StaticFieldLeak")
object CatalogsFirebaseManager {
    suspend fun getValue(reference: String, typeCollection: String, field: String): String {
        val value: String
        val document: Task<DocumentSnapshot>? = if (Validator.isNetworkEnable(Application.getContext())) {
            Application.firestore.collection(typeCollection).document(reference).get(Source.SERVER)
        } else {
            Application.firestore.collection(typeCollection).document(reference).get(Source.CACHE)
        }
        value = document?.await()?.get(field).toString()
        return value
    }

    suspend fun saveCities(cityList: MutableList<City>): Boolean {
        val insert = CoroutineScope(Dispatchers.IO).launch {
            Application.m_database_web.cityDao().deleteAll()
            Application.m_database_web.cityDao().insert(cityList)
        }
        insert.join()
        return true
    }

    suspend fun saveZipCodes(zipCodeList: MutableList<ZipCodes>): Boolean {
        val insert = CoroutineScope(Dispatchers.IO).launch {
            Application.m_database_web.zipCodeDao().deleteAll()
            Application.m_database_web.zipCodeDao().insert(zipCodeList)
        }
        insert.join()
        return true
    }

    suspend fun saveColonies(colonyList: MutableList<Colony>): Boolean {
        val insert = CoroutineScope(Dispatchers.IO).launch {
            Application.m_database_web.colonyDao().deleteAll()
            Application.m_database_web.colonyDao().insert(colonyList)
        }
        insert.join()
        return true
    }

    fun getZipCodesByCityId(idCity: String): MutableList<ZipCodes> {
        return object : AsyncTask<Void, Void, MutableList<ZipCodes>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<ZipCodes> {
                return Application.m_database_web.zipCodeDao().selectByCityId(idCity)
            }
        }.execute().get()
    }

    fun getColoniesByZipCode(zipCode: Int): MutableList<Colony> {
        return object : AsyncTask<Void, Void, MutableList<Colony>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<Colony> {
                return Application.m_database_web.colonyDao().selectByZipCode(zipCode)
            }
        }.execute().get()
    }

    fun getCitiesByStateReference(reference: String): MutableList<City> {
        return object : AsyncTask<Void, Void, MutableList<City>>() {
            override fun doInBackground(vararg p0: Void?): MutableList<City> {
                return Application.m_database_web.cityDao().selectByStateReference(reference)
            }
        }.execute().get()
    }
}