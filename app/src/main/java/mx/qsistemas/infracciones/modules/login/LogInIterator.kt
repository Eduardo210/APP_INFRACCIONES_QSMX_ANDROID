package mx.qsistemas.infracciones.modules.login

import android.util.Log
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.City
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.CipherData
import mx.qsistemas.infracciones.net.catalogs.CipherDataResult
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.net.request_web.LogInRequest
import mx.qsistemas.infracciones.net.result_web.LogInResult
import mx.qsistemas.infracciones.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.HttpURLConnection
import java.util.*

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
        val requestCipher = CipherData(false, "$userName|$psd")
        val networkApi = NetworkApi()
        networkApi.getHonosService().cipherData(requestCipher).enqueue(object : Callback<CipherDataResult> {
            override fun onResponse(call: Call<CipherDataResult>, response: Response<CipherDataResult>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val result = response.body()!!.data
                    val account = LogInRequest(result.value)
                    networkApi.getNetworkService().login(account).enqueue(object : Callback<LogInResult> {
                        override fun onFailure(call: Call<LogInResult>, t: Throwable) {
                            presenter.onError(t.message.toString())
                        }

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
                                    Application.prefs?.saveData(R.string.sp_person_photo_url, response.body()?.data?.image
                                            ?: "")
                                    Application.prefs?.saveDataBool(R.string.sp_has_session, true)
                                    listener.onLoginSuccessful()
                                }
                                HttpURLConnection.HTTP_UNAUTHORIZED -> listener.onError(Application.getContext().getString(R.string.e_user_pss_incorrect))
                                else -> listener.onError(Application.getContext().getString(R.string.e_other_problem_internet))
                            }
                        }
                    })
                } else {
                    listener.onError(Application.getContext().getString(R.string.e_other_problem_internet))
                }
            }

            override fun onFailure(call: Call<CipherDataResult>, t: Throwable) {
                listener.onError(t.message.toString())
            }
        })


        val request = hashMapOf("key" to BBOX_KEY, "value" to psd, "isEncrypted" to false)
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
                val cipher = ((it.result?.data) as HashMap<*, *>)["value"].toString()
                val request = LogInRequest(cipher)
                val gsonConverter = GsonConverterFactory.create()
                NetworkApi().getNetworkService(gsonConverter).login(request).enqueue(object : Callback<LogInResult> {
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

    private suspend fun getZipCodes(): MutableList<ZipCodes> {
        val zipCodes = mutableListOf<ZipCodes>()
        val firebase = Application.firebaseFunctions?.getHttpsCallable(FF_ZIP_CODES)?.call(null)?.addOnCompleteListener {
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
                    zipCodes.add(ZipCodes(0, code["key"].toString(), code["value"].toString(), code["reference"].toString(), code["is_active"].toString().toBoolean()))
                }
            }
        }
        firebase?.await()
        return zipCodes
    }

    private suspend fun getColonies(): MutableList<Colony> {
        val colonies = mutableListOf<Colony>()
        val reference = Application.firebaseStorage?.reference?.child("documents/colonias.json")
        val jsonFile = File.createTempFile("tempColonies", "json")
        val firestore = reference?.getFile(jsonFile)?.addOnSuccessListener {
            val jsonString = Utils.parseJsonFromFile(jsonFile)
            val readColonies = Gson().fromJson(jsonString, Array<ColonyJson>::class.java).toMutableList()
            readColonies.forEach {
                colonies.add(Colony(0, it.key, it.value, it.reference, it.isActive))
            }
        }?.addOnFailureListener {
            listener.onError("Error descargando colonias")
        }
        firestore?.await()
        return colonies
    }

    private suspend fun getCities(): MutableList<City> {
        val cities = mutableListOf<City>()
        val firestore = Application.firestore?.collection(FS_COL_CITIES)?.get()?.addOnSuccessListener {
            if (it != null && !it.isEmpty) {
                it.documents.forEach { doc ->
                    val data = doc.toObject(Townships::class.java)!!
                    cities.add(City(0, doc.id, data.value, data.reference?.id
                            ?: "", data.is_active))
                }
            }
        }?.addOnFailureListener {
            listener.onError(it.message
                    ?: Application.getContext().getString(R.string.e_firestore_not_available))
        }
        firestore?.await()
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