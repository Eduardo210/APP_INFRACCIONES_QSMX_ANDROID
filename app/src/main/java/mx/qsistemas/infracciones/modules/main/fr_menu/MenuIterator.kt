package mx.qsistemas.infracciones.modules.main.fr_menu

import android.net.Uri
import android.os.Environment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import com.google.firebase.functions.FirebaseFunctionsException
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db_web.managers.PermissionsMgr
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.Cities
import mx.qsistemas.infracciones.net.catalogs.HomeOptions
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

class MenuIterator(val listener: MenuContracts.Presenter) : MenuContracts.Iterator {

    var homeOptionsList: MutableList<HomeOptions> = mutableListOf()

    override fun getAttributes(): MutableList<String> {
        val permissions = mutableListOf<String>()
        PermissionsMgr.getPermissions().forEach { permissions.add(it.codeName) }
        return permissions
    }

    override fun getHomeOptions() {
        Application.firestore.collection(FS_COL_INFRINGEMENT_APP_OPTIONS).whereEqualTo("is_active", true).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
                return@addSnapshotListener
            }
            homeOptionsList = mutableListOf()
            val hasConfigCodi = if (Application.prefsCodi.containsData(R.string.sp_has_config_codi)) Application.prefsCodi.loadDataBoolean(R.string.sp_has_config_codi, false) else false
            val hasBankAccount = if (Application.prefsCodi.containsData(R.string.sp_has_bank_account_codi)) Application.prefsCodi.loadDataBoolean(R.string.sp_has_bank_account_codi, false) else false
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(HomeOptions::class.java)!!
                    data.idReference = document.id
                    /* Agregar a la lista cuando no sea la opción de consultar el estatus de validación de cuenta CoDi */
                    if (data.idReference != HO_VALIDATE_ACCOUNT_CODI) {
                        homeOptionsList.add(data)
                        /* Mostrar la opción si es para consultar el estatus de validación de cuenta beneficiaria solo si ya se ha configurado CoDi */
                    } else if (data.idReference == HO_VALIDATE_ACCOUNT_CODI && hasConfigCodi && !hasBankAccount) {
                        homeOptionsList.add(data)
                    }
                }
                homeOptionsList.sortBy { it.order }
            }
            listener.onHomeOptionsReady(homeOptionsList)
        }
    }

    override fun sendDatabase() {
        val sd = Environment.getExternalStorageDirectory()
        val employeeId = Application.prefs.loadData(R.string.sp_no_employee, "")!!
        val storageReference = Application.firebaseStorage.reference.child("databases/${BuildConfig.APPLICATION_ID}/$employeeId.db")
        if (sd.canWrite()) {
            val dbPath = Application.m_database_web.openHelper.writableDatabase?.path ?: ""
            val dbFile = File(dbPath)
            if (dbFile.exists()) {
                storageReference.putFile(Uri.fromFile(dbFile)).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        listener.onError(it.exception!!.message!!)
                        return@addOnCompleteListener
                    } else {
                        listener.onDatabaseSend()
                    }
                }
            } else {
                listener.onError(Application.getContext().getString(R.string.e_database_not_exists))
            }
        } else {
            listener.onError(Application.getContext().getString(R.string.e_database_not_access))
        }
    }

    override fun checkIfApplicationIsActive() {
        Application.firestore.collection(FS_COL_CITIES).document(Application.prefs.loadData(R.string.sp_id_township, "")!!).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        val document = snapshot.toObject(Cities::class.java)!!
                        if (!document.app_is_active) listener.onApplicationDisable()
                        else listener.onApplicationEnable()
                    }
                }.addOnFailureListener { exception -> FirebaseCrashlytics.getInstance().recordException(exception) }
    }

    override fun validateSession() {
        val cityRef = Application.firestore.collection(FS_COL_CITIES).document(Application.prefs.loadData(R.string.sp_id_township, "")!!)
        val idUser = Application.prefs.loadData(R.string.sp_id_officer)
        val imei = Utils.getImeiDevice(Application.getContext())
        Application.firestore.collection(FS_COL_TERMINALS).whereEqualTo("city", cityRef).whereEqualTo("logged_user", idUser).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && !snapshot.isEmpty) {
                        snapshot.documents.forEach { document ->
                            if (document.id != imei) {
                                listener.onSessionExpired()
                                return@addOnSuccessListener
                            }
                        }
                    }
                }.addOnFailureListener { exception -> FirebaseCrashlytics.getInstance().recordException(exception) }
    }

    override fun reconfigureDevice(psd: String) {
        val logInMap: HashMap<String, Any> = hashMapOf("isEncrypted" to false, "value" to "${Application.prefs.loadData(R.string.sp_user, "")!!}|$psd")
        Application.firebaseFunctions.getHttpsCallable(FF_CIPHER_DATA).call(logInMap).addOnCompleteListener {
            if (!it.isSuccessful) {
                val e = it.exception
                e?.let { FirebaseCrashlytics.getInstance().recordException(e.fillInStackTrace()) }
                if (e is FirebaseFunctionsException)
                    listener.onError(e.details.toString())
                else
                    listener.onError(Application.getContext().getString(R.string.e_other_problem_internet))
                return@addOnCompleteListener
            }
            if (it.exception == null && it.result != null) {
                val cipher = ((it.result?.data) as HashMap<*, *>)["value"].toString()
                val request = LogInRequest(cipher)
                NetworkApi().getNetworkService().login(request).enqueue(object : Callback<LogInResult> {
                    override fun onResponse(call: Call<LogInResult>, response: Response<LogInResult>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK -> {
                                loadNewFirebaseParams()
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

    override fun closeSession() {
        Application.prefs.clearPreference(R.string.sp_id_officer)
        Application.prefs.clearPreference(R.string.sp_person_name)
        Application.prefs.clearPreference(R.string.sp_person_photo_url)
        Application.prefs.clearPreference(R.string.sp_has_session)
        Application.prefs.clearPreference(R.string.sp_user)
        val imei = Utils.getImeiDevice(Application.getContext())
        val map = hashMapOf<String, Any>("logged_user" to FieldValue.delete())
        Application.firestore.collection(FS_COL_TERMINALS).document(imei).update(map)
    }

    private fun loadNewFirebaseParams() {
        Application.firestore.collection(FS_COL_CITIES).document(Application.prefs.loadData(R.string.sp_id_township, "")!!).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val querySnapshot = it.result
                if (querySnapshot != null) {
                    val township = querySnapshot.toObject(Cities::class.java)!!
                    val imei = Utils.getImeiDevice(Application.getContext())
                    Application.firestore.collection(FS_COL_CITIES).document(Application.prefs.loadData(R.string.sp_id_township, "")!!)
                            .update("counter_prefix", township.counter_prefix + 1).addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    val prefix = township.prefix + (township.counter_prefix + 1)
                                    Application.prefs.saveData(R.string.sp_prefix, prefix)
                                    Application.firestore.collection(FS_COL_TERMINALS).document(imei).update("prefix", prefix)
                                    listener.onReconfigureFirestore()
                                } else {
                                    listener.onError(Application.getContext().getString(R.string.e_firestore_not_saved))
                                }
                            }
                }
            } else {
                listener.onError(Application.getContext().getString(R.string.e_firestore_not_available))
            }
        }
    }
}