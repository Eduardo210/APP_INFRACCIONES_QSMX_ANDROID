package mx.qsistemas.infracciones.modules.login

import android.util.Log
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.City
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.State
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.GenericSubCatalog
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.net.request_web.LogInRequest
import mx.qsistemas.infracciones.net.result_web.LogInResult
import mx.qsistemas.infracciones.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class LogInIterator(private val listener: LogInContracts.Presenter) : LogInContracts.Iterator {

    override fun registerAlarm() {
        Alarms()
    }

    /*override fun syncCatalogs() = runBlocking {
        launch(Dispatchers.IO) {
            *//* Sync Catalogs Of Zip Codes *//*
            Application.firestore?.collection(FS_COL_ZIP_CODES)?.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                val zipCodesList = mutableListOf<ZipCodes>()
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericSubCatalog::class.java)!!
                        Log.d("Firebase", data.toString())
                        zipCodesList.add(ZipCodes(0, document.id, document.id, data.reference?.id
                                ?: "", data.is_active))
                    }
                    CatalogsFirebaseManager.saveZipCodes(zipCodesList)
                    *//* Sync Catalogs Of Colonies *//*
                    Application.firestore?.collection(FS_COL_COLONIES)?.addSnapshotListener { snapshot2, exception2 ->
                        if (exception2 != null) {
                            return@addSnapshotListener
                        }
                        val coloniesList = mutableListOf<Colony>()
                        if (snapshot2 != null && !snapshot2.isEmpty) {
                            for (document in snapshot2.documents) {
                                val data = document.toObject(GenericSubCatalog::class.java)!!
                                Log.d("Firebase", data.toString())
                                coloniesList.add(Colony(0, document.id, data.value, data.reference?.id
                                        ?: "", data.is_active))
                            }
                            CatalogsFirebaseManager.saveColonies(coloniesList)
                            *//* Sync Catalogs of States *//*
                            Application.firestore?.collection(FS_COL_STATES)?.addSnapshotListener { snapshot3, exception3 ->
                                if (exception3 != null) {
                                    return@addSnapshotListener
                                }
                                val statesList = mutableListOf<State>()
                                if (snapshot3 != null && !snapshot3.isEmpty) {
                                    for (document in snapshot3.documents) {
                                        val data = document.toObject(GenericCatalog::class.java)!!
                                        Log.d("Firebase", data.toString())
                                        statesList.add(State(0, document.id, data.value, data.is_active))
                                    }
                                    CatalogsFirebaseManager.saveStates(statesList)
                                    *//* Sync Catalogs Of Cities *//*
                                    Application.firestore?.collection(FS_COL_CITIES)?.addSnapshotListener { snapshot4, exception4 ->
                                        if (exception4 != null) {
                                            return@addSnapshotListener
                                        }
                                        val citiesList = mutableListOf<City>()
                                        if (snapshot4 != null && !snapshot4.isEmpty) {
                                            for (document in snapshot4.documents) {
                                                val data = document.toObject(Townships::class.java)!!
                                                Log.d("Firebase", data.toString())
                                                citiesList.add(City(0, document.id, data.value, data.reference?.id
                                                        ?: "", data.is_active))
                                            }
                                            CatalogsFirebaseManager.saveCities(citiesList)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/

    override fun login(userName: String, psd: String) {
        val request = hashMapOf("key" to BBOX_KEY, "value" to psd)
        Application.firebaseFunctions?.getHttpsCallable(FF_CIPHER_DATA)?.call(request)?.addOnCompleteListener {
            if (!it.isSuccessful) {
                val e = it.exception
                if (e is FirebaseFunctionsException)
                    listener.onError(e.details.toString())
                else
                    listener.onError(Application.getContext().getString(R.string.e_without_internet))
                return@addOnCompleteListener
            }
            if (it.exception == null && it.result != null) {
                Log.e(this.javaClass.simpleName, "result ${it.result?.data}")
                val cipher = ((it.result?.data) as HashMap<*, *>)["encrypted"].toString()
                val request = LogInRequest(userName.trim(), cipher)
                NetworkApi().getNetworkService().login(request).enqueue(object : Callback<LogInResult> {
                    override fun onResponse(call: Call<LogInResult>, response: Response<LogInResult>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK -> {
                                Application.prefs?.saveData(R.string.sp_access_token, response.body()?.access_token
                                        ?: "")
                                Application.prefs?.saveDataInt(R.string.sp_id_officer, response.body()?.idPerson
                                        ?: 0)
                                Application.prefs?.saveData(R.string.sp_person_name, response.body()?.nameOfficer
                                        ?: "")
                                Application.prefs?.saveData(R.string.sp_person_f_last_name, response.body()?.lastNameOfficer
                                        ?: "")
                                Application.prefs?.saveData(R.string.sp_person_m_last_name, response.body()?.secLastNameOfficer
                                        ?: "")
                                Application.prefs?.saveData(R.string.sp_person_photo_url, response.body()?.urlPhoto
                                        ?: "")
                                Application.prefs?.saveDataBool(R.string.sp_has_session, true)
                                listener.onLoginSuccessful()
                            }
                            HttpURLConnection.HTTP_UNAUTHORIZED -> listener.onError(Application.getContext().getString(R.string.e_user_pss_incorrect))
                            else -> listener.onError(Application.getContext().getString(R.string.e_other_problem_internet))
                        }
                    }

                    override fun onFailure(call: Call<LogInResult>, t: Throwable) {
                        listener.onError(t.message
                                ?: Application.getContext().getString(R.string.e_other_problem_internet))
                    }
                })
            }
        }
    }
}