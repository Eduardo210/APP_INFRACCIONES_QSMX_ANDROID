package mx.qsistemas.infracciones.alarm

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.utils.*
import java.util.*

class GeosService : JobService() {

    private var lastLocation: Location? = null
    private var locationCallback: LocationCallback
    private var tiempoTranscurrido = 0L
    private val fusedLocationClient = lazy { LocationServices.getFusedLocationProviderClient(Application.getContext()) }
    private val locationRequest = lazy {
        LocationRequest.create()?.apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    init {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    lastLocation = location
                }
            }
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onStartJob(params: JobParameters?): Boolean {
        val imei = Utils.getImeiDevice(Application.getContext())
        fusedLocationClient.value.requestLocationUpdates(locationRequest.value,
                locationCallback, null /* Looper */)
        Thread {
            run {
                try {
                    while (tiempoTranscurrido < TIME_OUT_OF_GEO) {
                        Thread.sleep(200)
                        tiempoTranscurrido += 200
                        if (lastLocation != null && lastLocation!!.accuracy <= MAX_ACCURACY) {
                            tiempoTranscurrido = TIME_OUT_OF_GEO
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    fusedLocationClient.value.removeLocationUpdates(locationCallback)
                    if (lastLocation != null && Validator.isNetworkEnable(Application.getContext())) {
                        Application.firestore.collection(FS_COL_TERMINALS).document(imei).update("last_geo", GeoPoint(lastLocation!!.latitude, lastLocation!!.longitude))
                        Application.firestore.collection(FS_COL_TERMINALS).document(imei).update("time_geo", Date())
                    }
                }
                stopSelf()
            }
        }.start()
        return true
    }
}