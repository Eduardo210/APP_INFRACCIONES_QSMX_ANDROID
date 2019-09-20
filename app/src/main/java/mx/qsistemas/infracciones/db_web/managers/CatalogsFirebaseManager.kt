package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
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
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.State
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.utils.Validator
import java.util.concurrent.Executors


@SuppressLint("StaticFieldLeak")
object CatalogsFirebaseManager {
    suspend fun getValue(reference: String, typeCollection: String, field: String): String {
        val value: String
        val document: Task<DocumentSnapshot>? = if (Validator.isNetworkEnable(Application.getContext())) {
            Application.firestore?.collection(typeCollection)?.document(reference)?.get(Source.SERVER)
        } else {
            Application.firestore?.collection(typeCollection)?.document(reference)?.get(Source.CACHE)
        }
        value = document?.await()?.get(field).toString()
        return value
    }

    fun saveStates(statesList: MutableList<State>) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database_web?.stateDao()?.deleteAll()
            Application.m_database_web?.stateDao()?.insert(statesList)
        }
    }

    fun saveCities(cityList: MutableList<City>) {
        Executors.newSingleThreadExecutor().execute {
            Application.m_database_web?.cityDao()?.deleteAll()
            Application.m_database_web?.cityDao()?.insert(cityList)
        }
    }

    suspend fun saveZipCodes(zipCodeList: MutableList<ZipCodes>): Boolean {
        val insert=  CoroutineScope(Dispatchers.IO).launch{
            Application.m_database_web?.zipCodeDao()?.deleteAll()
            Application.m_database_web?.zipCodeDao()?.insert(zipCodeList)
        }
        insert.join()
        return true
    }

    fun deleteColonies(){
        Executors.newSingleThreadExecutor().execute {
            Application.m_database_web?.colonyDao()?.deleteAll()
        }
    }

    suspend fun saveColonies(colonyList: MutableList<Colony>):Boolean {
        val insert = CoroutineScope(Dispatchers.IO).launch {
            Application.m_database_web?.colonyDao()?.insert(colonyList)
        }
        insert.join()
        return true
    }
}