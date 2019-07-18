package mx.qsistemas.infracciones.net

import android.os.Bundle
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.utils.*

object FirebaseEvents {

    private val personName = "${Application.prefs?.loadData(R.string.sp_person_name, "")} ${Application.prefs?.loadData(R.string.sp_person_f_last_name, "")} ${Application.prefs?.loadData(R.string.sp_person_m_last_name, "")}"
    private val townshipName = Application.prefs?.loadData(R.string.sp_township_name, "")
    private val devicePrefix = Application.prefs?.loadData(R.string.sp_prefix, "")

    fun registerUserProperties() {
        Application.firebaseAnalytics?.setUserProperty(EV_PERSON_NAME, personName)
    }

    fun registerInfractionStarted() {
        val bundle = Bundle()
        bundle.putString(EV_PERSON_NAME, personName)
        bundle.putString(EV_DEVICE_PREFIX, devicePrefix)
        bundle.putString(EV_DEVICE_IMEI, Utils.getImeiDevice(Application.getContext()))
        bundle.putString(EV_TOWNSHIP_NAME, townshipName)
        Application.firebaseAnalytics?.logEvent(EVENT_INFRACTION_STARTED, bundle)
    }

    fun registerInfractionFinished() {
        val bundle = Bundle()
        bundle.putString(EV_PERSON_NAME, personName)
        bundle.putString(EV_DEVICE_PREFIX, devicePrefix)
        bundle.putString(EV_DEVICE_IMEI, Utils.getImeiDevice(Application.getContext()))
        bundle.putString(EV_TOWNSHIP_NAME, townshipName)
        Application.firebaseAnalytics?.logEvent(EVENT_INFRACTION_FINISHED, bundle)
    }
}