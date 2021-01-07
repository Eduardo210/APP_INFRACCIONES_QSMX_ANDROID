package mx.qsistemas.infracciones.modules.main.fr_menu

import android.net.Uri
import android.os.Environment
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.Application.Companion.TAG
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.net.catalogs.Cities
import mx.qsistemas.infracciones.net.catalogs.HomeOptions
import mx.qsistemas.infracciones.utils.*
import java.io.File

class MenuIterator(val listener: MenuContracts.Presenter) : MenuContracts.Iterator {

    var homeOptionsList: MutableList<HomeOptions> = mutableListOf()

    override fun getAttributes(): MutableList<Long> {
        val idPerson = Application.prefs.loadData(R.string.sp_id_officer)
        return mutableListOf()//return CatalogsAdapterManager.getAttributes(idPerson)
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
            val dbPath = Application.m_database.openHelper.writableDatabase?.path ?: ""
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
        val cityRef = Application.firestore.collection(FS_COL_CITIES).document(Application.prefs.loadData(R.string.sp_id_township,"")!!)
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
                }.addOnFailureListener {  exception -> FirebaseCrashlytics.getInstance().recordException(exception) }
    }

    override fun closeSession() {
        Application.prefs.clearPreference(R.string.sp_id_officer)
        Application.prefs.clearPreference(R.string.sp_person_name)
        Application.prefs.clearPreference(R.string.sp_person_photo_url)
        Application.prefs.clearPreference(R.string.sp_has_session)
        val imei = Utils.getImeiDevice(Application.getContext())
        val map = hashMapOf<String, Any>("logged_user" to FieldValue.delete())
        Application.firestore.collection(FS_COL_TERMINALS).document(imei).update(map)
    }
}