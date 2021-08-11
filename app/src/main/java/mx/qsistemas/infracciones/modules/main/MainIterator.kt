package mx.qsistemas.infracciones.modules.main

import com.google.firebase.firestore.FieldValue
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.utils.FS_COL_TERMINALS
import mx.qsistemas.infracciones.utils.Utils


class MainIterator(private val presenter: MainContracts.Presenter) : MainContracts.Iterator {

    fun closeSession() {
        Application.prefs.clearPreference(R.string.sp_id_officer)
        Application.prefs.clearPreference(R.string.sp_person_name)
        Application.prefs.clearPreference(R.string.sp_person_photo_url)
        Application.prefs.clearPreference(R.string.sp_has_session)
        Application.prefs.clearPreference(R.string.sp_user)
        val imei = Utils.getImeiDevice(Application.getContext())
        val map = hashMapOf<String, Any>("logged_user" to FieldValue.delete())
        Application.firestore.collection(FS_COL_TERMINALS).document(imei).update(map)
    }

}