package mx.qsistemas.infracciones.alarm

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.Application.Companion.TAG
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db_web.managers.SendInfractionManagerWeb
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.request_web.*
import mx.qsistemas.infracciones.net.result_web.GenericResult
import mx.qsistemas.infracciones.net.result_web.InfractionResult
import mx.qsistemas.infracciones.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import javax.net.ssl.HttpsURLConnection


class ReportsService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        sendReports()
        sendPhotosInEvidence()
        return true
    }

    private fun sendReports() {
        /* Create Infraction Notification Builder */
        val builderInfraction = NotificationCompat.Builder(this, CHANNEL_ID_REPORT).apply {
            setContentTitle(getString(R.string.app_name))
            setContentText("Enviando infracciones...")
            setSmallIcon(R.drawable.transparent_launcher)
            color = ContextCompat.getColor(this@ReportsService, R.color.colorPrimary)
            priority = NotificationCompat.PRIORITY_HIGH
        }

        if (Validator.isNetworkEnable(Application.getContext())) {
            //Get the reports to send
            val reportsToSend = SendInfractionManagerWeb.getInfractionsToSend()
            var reportsSend = 0
            if (reportsToSend.size > 0) {
                //Launch Notification
                val notification = NotificationManagerCompat.from(this).apply {
                    builderInfraction.setProgress(reportsToSend.size, reportsSend, true)
                    notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                }
                //Generate each infraction object to send
                reportsToSend.forEachIndexed { index, it ->
                    // Get the images of the infraction
                    val requestPictures = mutableListOf<PicturesItem>()
                    SendInfractionManagerWeb.getInfractionPictures(it.id).forEach { pic -> requestPictures.add(PicturesItem(pic.image)) }
                    //Get address of the infraction
                    val addresInfraction = SendInfractionManagerWeb.getInfractionAddress(it.id)
                    val requestAddresInfraction = AddressInfraction(
                            addresInfraction.state_id,
                            addresInfraction.city_id,
                            null,
                            addresInfraction.colony,
                            addresInfraction.street,
                            addresInfraction.street_a,
                            addresInfraction.street_b,
                            addresInfraction.latitude,
                            addresInfraction.longitude)
                    // Get infraction fractions and motivations list
                    val requestMotivations = mutableListOf<FractionsItem>()
                    SendInfractionManagerWeb.getInfractionMotivationList(it.id).forEach { x ->
                        requestMotivations.add(FractionsItem(x.reason, x.fraction_id, x.uma.toString(), x.amount.toString()))
                    }
                    // Get offender license
                    val personLicense = SendInfractionManagerWeb.getPersonLicense(it.id)
                    var requestPersonLicense = DriverLicense()
                    if (personLicense != null) {
                        requestPersonLicense = DriverLicense(
                                personLicense.license_number,
                                personLicense.license_type_id,
                                personLicense.state_license_id)
                    }
                    // Get offender address
                    val personAddress = SendInfractionManagerWeb.getPersonAddress(it.driver_id)
                    var requestAddressDriver = AddressDriver()
                    if (personAddress != null) {
                        requestAddressDriver = AddressDriver(personAddress.city_id, personAddress.colony, personAddress.street, personAddress.exterior_num, personAddress.internal_num)
                    }
                    // Get person information
                    val personInfo = SendInfractionManagerWeb.getPersonInformation(it.id)
                    var requestPerson = DriverRequest()
                    if (personInfo != null) {
                        requestPerson = DriverRequest(personInfo.name, personInfo.paternal, personInfo.maternal)
                    }

                    // Get payer information
                    val payerInfo = SendInfractionManagerWeb.getPayerInformation(it.id)
                    var requestPayerInfo = PayerInfraction()
                    if (payerInfo != null) {
                        requestPayerInfo = PayerInfraction(
                                payerInfo.name,
                                payerInfo.paternal,
                                payerInfo.maternal,
                                payerInfo.rfc,
                                payerInfo.business_name,
                                payerInfo.email)
                    }
                    //Get vehicle information of infraction
                    val vehicleInfraction = SendInfractionManagerWeb.getVehicleInformation(it.id)
                    var requestVehicleInfraction = VehicleInfraction()
                    if (vehicleInfraction != null) {
                        requestVehicleInfraction = VehicleInfraction(
                                vehicleInfraction.sub_brand_id,
                                vehicleInfraction.identifier_document_id,
                                vehicleInfraction.num_document,
                                vehicleInfraction.issued_in_id,
                                vehicleInfraction.colour_id,
                                vehicleInfraction.class_type_id,
                                vehicleInfraction.year
                        )
                    }
                    // Get the capture lines
                    val requestCaptureLines = mutableListOf<CaptureLinesItem>()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    SendInfractionManagerWeb.getCaptureLines(it.id).forEach { line ->
                        requestCaptureLines.add(CaptureLinesItem(line.key, "%.2f".format(line.amount).toFloat(), line.order, dateFormat.format(SimpleDateFormat("dd/MM/yyyy").parse(line.date)), line.discount))
                    }
                    //Create the infraction subheader
                    val infractionRequest = InfractionRequest(
                            it.folio,
                            it.town_hall_id,
                            it.date,
                            it.time,
                            it.insured_document_id,
                            it.is_impound,
                            it.third_impound_id,
                            requestAddresInfraction,
                            requestVehicleInfraction,
                            requestMotivations,
                            it.is_absent,
                            requestPerson,
                            requestAddressDriver,
                            requestPersonLicense,
                            requestCaptureLines,
                            it.is_paid,
                            requestPayerInfo)
                    //Send the infractions list
                    val token = Application.prefs.loadData(R.string.sp_session_token, "")

                    NetworkApi().getNetworkService().sendInfractionToServer("$token", infractionRequest).enqueue(object : Callback<InfractionResult> {
                        override fun onResponse(call: Call<InfractionResult>, response: Response<InfractionResult>) {
                            if (response.code() == HttpsURLConnection.HTTP_OK || response.code() == HttpsURLConnection.HTTP_CREATED) {
                                if (response.body() != null) {
                                    val result = response.body()!!
                                    //if (result?.status == "success") {
                                    SendInfractionManagerWeb.updateInfractionSend(result.data.token, result.data.folio)
                                    reportsSend++
                                    sendPayments()
                                    // When done, update the notification one more time to remove the progress bar
                                    if (reportsSend == index + 1) {
                                        builderInfraction.setContentText(getString(R.string.s_infraction_send) + reportsSend)
                                                .setProgress(0, 0, false)
                                    } else {
                                        builderInfraction.setContentText(getString(R.string.s_infraction_send) + reportsSend)
                                                .setProgress(reportsToSend.size, reportsSend, true)
                                    }
                                    notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())

                                } else {
                                    // When done, update the notification one more time to remove the progress bar
                                    builderInfraction.setContentText(getString(R.string.s_infraction_send) + reportsSend)
                                            .setProgress(reportsToSend.size, reportsSend, false)
                                    notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                                }
                            } else {
                                // When done, update the notification one more time to remove the progress bar
                                builderInfraction.setContentText(getString(R.string.e_send_infractions_incomplete) + (reportsToSend.size - reportsSend))
                                        .setProgress(0, 0, false)
                                notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                            }
                        }

                        override fun onFailure(call: Call<InfractionResult>, t: Throwable) {
                            Log.e(TAG, "Send Infractions Failed: ${t.message}")
                            // When done, update the notification one more time to remove the progress bar
                            builderInfraction.setContentText(getString(R.string.e_connect_server))
                                    .setProgress(0, 0, false)
                            notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                        }
                    })
                }
            } else {
                sendPayments()
            }
        }
    }

    private fun sendPayments() {
        val payments = SendInfractionManagerWeb.getPayments()
        payments.forEach { payToSend ->
            //Obtenemos el token de la infracción
            val infraction = SendInfractionManagerWeb.getInfractionsToken(payToSend.infringement_id)
            //Llenamos la info del pago
            val payerCard = PayerCardInfo(
                    "%.2f".format(payToSend.amount),
                    payToSend.tx_nb,
                    payToSend.membership,
                    payToSend.card_holder,
                    payToSend.type,
                    payToSend.tx_date,
                    payToSend.app_label,
                    payToSend.bank,
                    payToSend.mobile_series,
                    payToSend.tx_time,
                    payToSend.control_number,
                    payToSend.entry_type,
                    payToSend.bank_reference,
                    payToSend.authorize_no.toString())


            //Llenamos la info para los datos de facturación
            val bill = SendInfractionManagerWeb.getPayerInformation(payToSend.infringement_id)
            var payer = Payer()
            if (bill != null) {
                payer = Payer(
                        bill.business_name,
                        bill.name,
                        bill.rfc,
                        bill.email,
                        bill.paternal,
                        bill.maternal)
            }
            val request = SendPaymentRequest(
                    infraction.token_server,
                    payer.name,
                    payer.paternal,
                    payer.maternal,
                    payer.rfc,
                    payer.businessName,
                    payer.email,
                    payerCard)
            val token = Application.prefs.loadData(R.string.sp_session_token, "")!!
            //TODO: DEcirle a Eric que genere una respuesta chida
            NetworkApi().getNetworkService().savePaymentToServer(token,
                    payToSend.id.toLong(), request).enqueue(object : Callback<GenericResult> {
                override fun onResponse(call: Call<GenericResult>, response: Response<GenericResult>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.body()?.error == "") {
                            SendInfractionManagerWeb.updatePaymentSend(call.request().headers()["idPayment"]!!.toLong())
                        } else {

                        }
                    }
                }

                override fun onFailure(call: Call<GenericResult>, t: Throwable) {
                    Log.d("PAYMENTS", t.message + "")
                }
            })
        }
    }

    private fun sendPhotosInEvidence() {
        var photosToSendCount = 0
        val builderImages = NotificationCompat.Builder(this, CHANNEL_ID_IMAGES).apply {
            setContentTitle(getString(R.string.app_name))
            setContentText("Enviando fotos...")
            setSmallIcon(R.drawable.transparent_launcher)
            color = ContextCompat.getColor(this@ReportsService, R.color.colorPrimary)
            priority = NotificationCompat.PRIORITY_HIGH
        }

        val storageRef = Application.firebaseStorage.reference
        val photosToSend = SendInfractionManagerWeb.getAllPhotos()
        if (photosToSend.size > 0) {
            val notification = NotificationManagerCompat.from(this).apply {
                builderImages.setProgress(photosToSend.size, photosToSendCount, true)
                notify(NOTIF_SEND_IMAGES, builderImages.build())
            }
            //traffic_violation/token_municipio/evidence/photos/documentoIdentificador_folio_I.jpg
            photosToSend.forEach { photo ->
                val township =
                        Application.prefs.loadData(R.string.sp_id_township, "")!!
                val pathToFirestore = "traffic_violation/$township/evidence/photos/${photo.name}"
                val riversRef =
                        storageRef.child(pathToFirestore)

                val photoUri =
                        Utils.getImageUriFromB64(Application.getContext(), photo.image, photo.name)
                val uploadTask = riversRef.putFile(photoUri)

                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress =
                            (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    //println("Upload is $progress% done")
                }.addOnPausedListener {
                }.addOnFailureListener {
                    builderImages.setContentText(getString(R.string.e_send_images_incomplete + (photosToSend.size - photosToSendCount)))
                            .setProgress(0, 0, false)
                    notification.notify(NOTIF_SEND_IMAGES, builderImages.build())

                }.addOnSuccessListener {
                    photosToSendCount++
                    val result = photosToSend.size - photosToSendCount
                    if (result != 0) {
                        SendInfractionManagerWeb.deletePhotos(photo.infringements_id)
                        builderImages.setContentText(getString(R.string.s_images_send) + ": " + photosToSendCount)
                                .setProgress(photosToSend.size, photosToSendCount, true)
                        notification.notify(NOTIF_SEND_IMAGES, builderImages.build())
                    } else {
                        SendInfractionManagerWeb.deletePhotos(photo.infringements_id)
                        builderImages.setContentText(getString(R.string.s_images_send) + ": " + photosToSendCount)
                                .setProgress(0, 0, false)
                        notification.notify(NOTIF_SEND_IMAGES, builderImages.build())
                    }

                }
            }
        }

    }

}