    package mx.qsistemas.infracciones.modules.login

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.SetOptions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.City
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.db_web.managers.PermissionsMgr
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.Cities
import mx.qsistemas.infracciones.net.request_web.LogInRequest
import mx.qsistemas.infracciones.net.result_web.LogInResult
import mx.qsistemas.infracciones.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.HttpURLConnection
import java.util.*
import kotlin.collections.HashMap

class LogInIterator(private val listener: LogInContracts.Presenter) : LogInContracts.Iterator {
    override fun registerAlarm() {
        Alarms()
    }

    override suspend fun syncCatalogs() {
        /* Sync Catalogs Of Zip Codes */
        var zipCodes: MutableList<ZipCodes>
        var colonies: MutableList<Colony>
        var cities: MutableList<City>
        GlobalScope.launch(Dispatchers.IO) {
            zipCodes = getZipCodes()
            if (insertZipCodes(zipCodes)) {
                colonies = getColonies()
                if (insertColonies(colonies)) {
                    cities = getCities()
                    if (insertCities(cities)) {
                        listener.onCatalogsDownloaded()
                    } else {
                        listener.onError("Error descargando municipios")
                    }
                } else {
                    listener.onError("Error descargando colonias")
                }
            } else {
                listener.onError("Error descargando códigos postales")
            }
        }
    }

    override fun login(userName: String, psd: String) {
        val logInMap: HashMap<String, Any> = hashMapOf("isEncrypted" to false, "value" to "$userName|$psd")
        Application.firebaseFunctions.getHttpsCallable(FF_CIPHER_DATA).call(logInMap).addOnCompleteListener {
            if (!it.isSuccessful) {
                val e = it.exception
                e?.let {  Log.e(Application.TAG, e.toString()) }
                if (e is FirebaseFunctionsException)
                    listener.onError(e.details.toString())
                else
                    listener.onError(Application.getContext().getString(R.string.e_other_problem_internet))
                return@addOnCompleteListener
            }
            if (it.exception == null && it.result != null) {
                val cipher = ((it.result?.data) as HashMap<*, *>)["value"].toString()
                Application.prefs.saveData(R.string.sp_session_token, cipher)
                val request = LogInRequest(cipher)
                NetworkApi().getNetworkService().login(request).enqueue(object : Callback<LogInResult> {
                    override fun onResponse(call: Call<LogInResult>, response: Response<LogInResult>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK -> {
                                if (response.body()?.data != null) {
                                    PermissionsMgr.insertPermissions(response.body()?.data?.permissions!!)
                                }
                                Application.prefs.saveData(R.string.sp_id_officer, response.body()?.data?.idPerson ?: 0)
                                Application.prefs.saveData(R.string.sp_person_name, response.body()?.data?.personName ?: "")
                                Application.prefs.saveData(R.string.sp_person_paternal, response.body()?.data?.personPaternal ?: "")
                                Application.prefs.saveData(R.string.sp_person_maternal, response.body()?.data?.personMaternal ?: "")
                                Application.prefs.saveData(R.string.sp_person_photo_url, NetworkApi.API_URL + response.body()?.data?.image)
                                Application.prefs.saveData(R.string.sp_no_employee, response.body()?.data?.employee ?: "")
                                Application.prefs.saveData(R.string.sp_user, userName)
                                Application.prefs.saveDataBool(R.string.sp_has_session, true)
                                val map = hashMapOf<String, Any>("logged_user" to response.body()?.data?.idPerson!!)
                                val imei = Utils.getImeiDevice(Application.getContext())
                                Application.firestore.collection(FS_COL_TERMINALS).document(imei).set(map, SetOptions.merge())
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

    private suspend fun getZipCodes(): MutableList<ZipCodes> {
        val zipCodes = mutableListOf<ZipCodes>()
        val firebase = Application.firebaseFunctions.getHttpsCallable(FF_ZIP_CODES).call(null).addOnCompleteListener {
            if (!it.isSuccessful) {
                val e = it.exception
                if (e is FirebaseFunctionsException)
                    listener.onError(e.details.toString())
                else
                    listener.onError("Error descargando códigos postales")
                return@addOnCompleteListener
            }
            if (it.exception == null && it.result != null) {
                val zipList = ((it.result?.data) as ArrayList<HashMap<*, *>>)
                zipList.forEach { code ->
                    zipCodes.add(ZipCodes(0, code["key"].toString(), code["value"].toString(), code["reference"].toString().split("/").last(), code["is_active"].toString().toBoolean()))
                }
            }
        }
        firebase.await()
        return zipCodes
    }

    private suspend fun getColonies(): MutableList<Colony> {
        val colonies = mutableListOf<Colony>()
        val reference = Application.firebaseStorage.reference.child("documents/colonias.json")
        val jsonFile = File.createTempFile("tempColonies", "json")
        val firestore = reference.getFile(jsonFile).addOnSuccessListener {
            val jsonString = Utils.parseJsonFromFile(jsonFile)
            val readColonies = Gson().fromJson(jsonString, Array<ColonyJson>::class.java).toMutableList()
            readColonies.forEach {
                colonies.add(Colony(0, it.key, it.value, it.reference, it.isActive))
            }
        }.addOnFailureListener {
            listener.onError("Error descargando colonias")
        }
        firestore.await()
        return colonies
    }

    private suspend fun getCities(): MutableList<City> {
        val cities = mutableListOf<City>()
        val firestore = Application.firestore.collection(FS_COL_CITIES).get().addOnSuccessListener {
            if (it != null && !it.isEmpty) {
                it.documents.forEach { doc ->
                    val data = doc.toObject(Cities::class.java)!!
                    cities.add(City(0, doc.id, data.value, data.reference?.id
                            ?: "", data.is_active))
                }
            }
        }.addOnFailureListener {
            listener.onError(it.message
                    ?: Application.getContext().getString(R.string.e_firestore_not_available))
        }
        firestore.await()
        return cities
    }

    suspend fun insertCities(cities: MutableList<City>): Boolean {
        return CatalogsFirebaseManager.saveCities(cities)
    }

    suspend fun insertZipCodes(zipCodes: MutableList<ZipCodes>): Boolean {
        return CatalogsFirebaseManager.saveZipCodes(zipCodes)
    }

    suspend fun insertColonies(colonies: MutableList<Colony>): Boolean {
        return CatalogsFirebaseManager.saveColonies(colonies)
    }

    class ColonyJson(@SerializedName("key") val key: String, @SerializedName("value") val value: String,
                     @SerializedName("reference") val reference: String, @SerializedName("is_active") val isActive: Boolean)
}