package mx.qsistemas.infracciones.modules.main.fr_menu

import android.net.Uri
import android.os.Environment
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.db.managers.LogInManager
import mx.qsistemas.infracciones.net.catalogs.HomeOptions
import mx.qsistemas.infracciones.utils.FS_COL_HOME_OPTIONS
import mx.qsistemas.infracciones.utils.HO_VALIDATE_ACCOUNT_CODI
import java.io.File

class MenuIterator(val listener: MenuContracts.Presenter) : MenuContracts.Iterator {

    var homeOptionsList: MutableList<HomeOptions> = mutableListOf()

    override fun getAttributes(): MutableList<Long> {
        val idPerson = Application.prefs.loadData(R.string.sp_id_officer)
        return mutableListOf()//return CatalogsAdapterManager.getAttributes(idPerson)
    }

    override fun getHomeOptions() {
        Application.firestore.collection(FS_COL_HOME_OPTIONS).whereEqualTo("is_active", true).addSnapshotListener { snapshot, exception ->
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

    override fun checkIfApplicationIsActive(): Boolean {
        return true//return LogInManager.isApplicationActive()
    }
}