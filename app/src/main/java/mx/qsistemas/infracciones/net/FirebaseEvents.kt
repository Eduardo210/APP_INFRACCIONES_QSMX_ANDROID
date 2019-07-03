package mx.qsistemas.infracciones.net

import android.os.Bundle
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.utils.*

object FirebaseEvents {

    private val personName = "${Application.prefs?.loadData(R.string.sp_person_name, "")} ${Application.prefs?.loadData(R.string.sp_person_f_last_name, "")} ${Application.prefs?.loadData(R.string.sp_person_m_last_name, "")}"
    private val devicePrefix = Application.prefs?.loadData(R.string.sp_prefix, "")

    fun registerInfractionCancelled() {
        val bundle = Bundle()
        bundle.putString(EV_PERSON_NAME, personName)
        bundle.putString(EV_DEVICE_PREFIX, devicePrefix)
        bundle.putString(EV_DEVICE_IMEI, Utils.getImeiDevice(Application.getContext()))
        Application.firebaseAnalytics?.logEvent(EVENT_INFRACTION_CANCELLED, bundle)
    }
}