package mx.qsistemas.infracciones.modules.login

import android.util.Log
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.request_web.LogInRequest
import mx.qsistemas.infracciones.net.result_web.LogInResult
import mx.qsistemas.infracciones.utils.BBOX_KEY
import mx.qsistemas.infracciones.utils.FF_CIPHER_DATA
import mx.qsistemas.infracciones.utils.FF_ZIP_CODES
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class LogInIterator(private val listener: LogInContracts.Presenter) : LogInContracts.Iterator {
    override fun registerAlarm() {
        Alarms()
    }

    suspend fun getZipCodes(): MutableList<ZipCodes> {
        val zipCodes = mutableListOf<ZipCodes>()
        val firebase = Application.firebaseFunctions?.getHttpsCallable(FF_ZIP_CODES)?.call(null)?.addOnCompleteListener {
            if (!it.isSuccessful) {
                val e = it.exception
                if (e is FirebaseFunctionsException)
                    listener.onError(e.details.toString())
                else
                    listener.onError(Application.getContext().getString(R.string.e_without_internet))
                return@addOnCompleteListener
            }
            if (it.exception == null && it.result != null) {
                // Log.e(this.javaClass.simpleName, "result ${it.result?.data}")
                val zipList = ((it.result?.data) as ArrayList<HashMap<*, *>>)
                zipList.forEach { code ->
                    zipCodes.add(ZipCodes(0, code["key"].toString(), code["value"].toString(), code["reference"].toString(), code["is_active"].toString().toBoolean()))
                    Log.d("FBASE-ZIP", code["value"].toString())
                }
            }
        }
        firebase?.await()
        return zipCodes
    }

    suspend fun insertZipCodes(zipCodes: MutableList<ZipCodes>): Boolean {
        return CatalogsFirebaseManager.saveZipCodes(zipCodes)
    }

    override suspend fun syncCatalogs() {
        /* Sync Catalogs Of Zip Codes */
        var zipCodes: MutableList<ZipCodes>
        GlobalScope.launch(Dispatchers.IO) {
            zipCodes = getZipCodes()
            if (insertZipCodes(zipCodes)) {
                Log.d("FBASE", "Codigos postales insertados de manera correcta. Size: ${zipCodes.size}")
            } else {
                Log.d("FBASE", "Error al insertar codigos postales")
            }
        }
    }

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