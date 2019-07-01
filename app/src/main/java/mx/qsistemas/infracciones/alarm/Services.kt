package mx.qsistemas.infracciones.alarm

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.location.Location
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.managers.SendInfractionManager
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.InfractionPhotoRequest
import mx.qsistemas.infracciones.net.catalogs.SaveInfractionRequest
import mx.qsistemas.infracciones.net.catalogs.SendInfractionResponse
import mx.qsistemas.infracciones.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.net.ssl.HttpsURLConnection


class ReportsService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        /* Create Infraction Notification Builder */
        val builderInfraction = NotificationCompat.Builder(this, CHANNEL_ID_REPORT).apply {
            setContentTitle(getString(R.string.app_name))
            setContentText("Enviando infracciones...")
            setSmallIcon(R.mipmap.ic_launcher)
            priority = NotificationCompat.PRIORITY_HIGH
        }
        if (Validator.isNetworkEnable(Application.getContext())) {
            /* Get the reports to send */
            val reportsToSend = SendInfractionManager.getInfractionsToSend()
            var infractionsList = mutableListOf<SaveInfractionRequest.InfractionRequest>()
            if (reportsToSend.size > 0) {
                /* Launch Notification */
                val notification = NotificationManagerCompat.from(this).apply {
                    builderInfraction.setProgress(0, 0, true)
                    notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                }
                /* Generate each infraction object to send */
                reportsToSend.forEach {
                    /* Get address of the infraction */
                    val addresInfraction = SendInfractionManager.getInfractionAddress(it.id.toLong())
                    /* Get infraction fractions and motivations list */
                    val motivationList = SendInfractionManager.getInfractionMotivationList(it.id.toLong())
                    val motivationListRequest = mutableListOf<SaveInfractionRequest.InfractionRequest.InfractionFractions_Request>()
                    /* Save the motivation list into the request */
                    motivationList.forEach {
                        motivationListRequest.add(SaveInfractionRequest.InfractionRequest.InfractionFractions_Request(it.id_fraction, it.penalty_points, it.salary, it.motivation))
                    }
                    /* Get offender address */
                    val personAddress = SendInfractionManager.getPersonAddress(it.id.toLong())
                    /* Get person information */
                    val personInfo = SendInfractionManager.getPersonInformation(it.id.toLong())
                    /* Get payment information of the infraction */
                    val paymentInfringement = SendInfractionManager.getPaymentInfrigment(it.id.toLong())
                    /* Get payment transaction information of the infraction */
                    val transactionInfo = SendInfractionManager.getPaymentTransactionInfo(it.id.toLong())
                    /* Get vehicle information of infraction */
                    val vehicleInfraction = SendInfractionManager.getVehileInformation(it.id.toLong())
                    /* Create the infraction subheader */
                    val infractionRequest = SaveInfractionRequest.InfractionRequest(it.is_absent, it.is_paid, it.forwarded_deposit, it.retained_document,
                            addresInfraction.street, addresInfraction.colony, addresInfraction.between_street, Application.prefs?.loadDataInt(R.string.sp_id_state).toString(),
                            Application.prefs?.loadDataInt(R.string.sp_id_township).toString(), addresInfraction.id_country.toString(),
                            0, 0, addresInfraction.and_street, it.registration_date,
                            it.date_capture_line_i, it.date_capture_line_ii, it.date_capture_line_iii, it.folio, it.folio_evidence,
                            motivationListRequest, 0, it.emits_vehicular_plate, it.id_disposition, it.id_status,
                            it.source_infra, it.id_officer, it.sector, it.amount_capture_line_i.toString(), it.amount_capture_line_ii.toString(),
                            it.amount_capture_line_iii.toString(), it.amount.toDouble(), it.capture_line_i, it.capture_line_ii, it.capture_line_iii,
                            it.reason, it.number_sheets_expedient, it.penalty_points, it.minimum_wage, it.minimum_wage_rate.toDouble(), personAddress.street,
                            personAddress.colony, true, personAddress.id_state, personAddress.id_township, "1", "",
                            personAddress.outdoor_number, personAddress.inside_number, it.id_license_type, it.issued_in.toInt(), personInfo.last_name_mother,
                            personInfo.name, it.no_permiso_licencia, personInfo.last_name_father, personInfo.rfc, paymentInfringement.discount,
                            paymentInfringement.id_payment_method, paymentInfringement.observation, paymentInfringement.surcharge,
                            paymentInfringement.subtotal, paymentInfringement.total, transactionInfo.aid, transactionInfo.app_label,
                            transactionInfo.arqc, transactionInfo.afiliacion, transactionInfo.banco_emisor, transactionInfo.emv_data,
                            transactionInfo.entry_type, transactionInfo.importe, transactionInfo.masked_pan, transactionInfo.mensaje,
                            paymentInfringement.folio, transactionInfo.numero_control, transactionInfo.referencia, transactionInfo.serial_paypda,
                            transactionInfo.tsi, transactionInfo.tvr, transactionInfo.tarjetahabiente, transactionInfo.tipo, transactionInfo.tipo_tarjeta,
                            transactionInfo.tipo_transaccion, transactionInfo.trx_date, transactionInfo.trx_nb, transactionInfo.trx_time,
                            transactionInfo.vigencia_tarjeta, it.id_reg_user.toString(),
                            vehicleInfraction.barcode, vehicleInfraction.colour, vehicleInfraction.id_identifier_document,
                            vehicleInfraction.id_brand, vehicleInfraction.model, vehicleInfraction.no_ident_document,
                            vehicleInfraction.sub_brand, vehicleInfraction.circulation_card, vehicleInfraction.type)
                    infractionsList.add(infractionRequest)
                }
                /* Create request header */
                val saveInfractionRequest = SaveInfractionRequest(infractionsList, "CF2E3EF25C90EB567243ADFACD4AA868", "InfraMobile")
                /* Send the infractions list */
                NetworkApi().getNetworkService().sendInfractionToServer(Gson().toJson(saveInfractionRequest)).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.code() == HttpsURLConnection.HTTP_OK) {
                            val result = Gson().fromJson(response.body(), SendInfractionResponse::class.java)
                            if (result.flag) {
                                Log.e(this.javaClass.simpleName, "All items were saved!!!")
                                reportsToSend.forEach {
                                    SendInfractionManager.updateInfractionToSend(it.folio)
                                }
                                // When done, update the notification one more time to remove the progress bar
                                builderInfraction.setContentText(getString(R.string.s_infraction_send))
                                        .setProgress(0, 0, false)
                                notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                            } else {
                                Log.e(this.javaClass.simpleName, "Items that weren't saved: ${result.folios}")
                                reportsToSend.forEach {
                                    if (it.folio !in result.folios) {
                                        SendInfractionManager.updateInfractionToSend(it.folio)
                                    }
                                }
                                // When done, update the notification one more time to remove the progress bar
                                builderInfraction.setContentText(getString(R.string.e_send_infractions_incomplete) + result.folios.size)
                                        .setProgress(0, 0, false)
                                notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                            }
                        } else {
                            // When done, update the notification one more time to remove the progress bar
                            builderInfraction.setContentText(getString(R.string.e_send_infractions))
                                    .setProgress(0, 0, false)
                            notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e(this.javaClass.simpleName, "Send Infractions Failed: ${t.message}")
                        // When done, update the notification one more time to remove the progress bar
                        builderInfraction.setContentText(getString(R.string.e_send_infractions))
                                .setProgress(0, 0, false)
                        notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                    }
                })
            }
            sendPhotos()
        }
        return true
    }

    private fun sendPhotos() {
        /* Create Photos Notification Builder */
        val builderPhotos = NotificationCompat.Builder(this, CHANNEL_ID_IMAGES).apply {
            setContentTitle(getString(R.string.app_name))
            setContentText("Enviando imágenes...")
            setSmallIcon(R.mipmap.ic_launcher)
            priority = NotificationCompat.PRIORITY_HIGH
        }
        /* Send photos of infractions */
        val photos = SendInfractionManager.getPhotosToSend()
        if (photos.size > 0) {
            /* Launch Photos Notification */
            val notificationPhotos = NotificationManagerCompat.from(this).apply {
                builderPhotos.setProgress(0, 0, true)
                notify(NOTIF_SEND_IMAGES, builderPhotos.build())
            }
            val photosToSend = mutableListOf<InfractionPhotoRequest.PhotoData>()
            photos.forEach {
                val vehicleInfraction = SendInfractionManager.getVehileInformation(it.idInfraction.toLong())
                val folioInfraction = SendInfractionManager.getFolioOfInfraction(it.idInfraction.toLong())
                photosToSend.add(InfractionPhotoRequest.PhotoData(4, "${vehicleInfraction.id_identifier_document}_${vehicleInfraction.no_ident_document}_${folioInfraction}_I.bmp", it.evidence1))
                photosToSend.add(InfractionPhotoRequest.PhotoData(4, "${vehicleInfraction.id_identifier_document}_${vehicleInfraction.no_ident_document}_${folioInfraction}_II.bmp", it.evidence2))
            }
            /* Create request header */
            val request = InfractionPhotoRequest(photosToSend, "CF2E3EF25C90EB567243ADFACD4AA868", "Mobile")
            NetworkApi().getNetworkService().sendImagesToServer(Gson().toJson(request)).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.code() == HttpsURLConnection.HTTP_OK) {
                        val result = Gson().fromJson(response.body(), SendInfractionResponse::class.java)
                        if (result.flag) {
                            photos.forEach {
                                SendInfractionManager.updateImageToSend(it.idInfraction.toLong())
                            }
                            // When done, update the notification one more time to remove the progress bar
                            builderPhotos.setContentText(getString(R.string.s_infraction_send))
                                    .setProgress(0, 0, false)
                            notificationPhotos.notify(NOTIF_SEND_IMAGES, builderPhotos.build())
                        } else {
                            val folios = mutableListOf<String>()
                            result.folios.forEach {
                                folios.add(it.split("_")[2])
                            }
                            photos.forEach {
                                val infraction = SendInfractionManager.getInfractionById(it.idInfraction.toLong())
                                if (infraction.folio !in folios) {
                                    SendInfractionManager.updateImageToSend(it.idInfraction.toLong())
                                }
                            }
                            // When done, update the notification one more time to remove the progress bar
                            builderPhotos.setContentText(getString(R.string.e_send_images_incomplete) + result.folios.size)
                                    .setProgress(0, 0, false)
                            notificationPhotos.notify(NOTIF_SEND_IMAGES, builderPhotos.build())
                        }
                    } else {
                        Log.e(this.javaClass.simpleName, "Send Images Fail")
                        // When done, update the notification one more time to remove the progress bar
                        builderPhotos.setContentText(getString(R.string.e_send_infractions))
                                .setProgress(0, 0, false)
                        notificationPhotos.notify(NOTIF_SEND_IMAGES, builderPhotos.build())
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(this.javaClass.simpleName, "Send Images Fail")
                    // When done, update the notification one more time to remove the progress bar
                    builderPhotos.setContentText(getString(R.string.e_send_infractions))
                            .setProgress(0, 0, false)
                    notificationPhotos.notify(NOTIF_SEND_IMAGES, builderPhotos.build())
                }
            })
        }
    }
}

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
                        Application.firestore?.collection(FS_COL_TERMINALS)?.document(imei)?.update("last_geo", GeoPoint(lastLocation!!.latitude, lastLocation!!.longitude))
                        Application.firestore?.collection(FS_COL_TERMINALS)?.document(imei)?.update("time_geo", Date())
                    }
                }
                stopSelf()
            }
        }.start()
        return true
    }
}