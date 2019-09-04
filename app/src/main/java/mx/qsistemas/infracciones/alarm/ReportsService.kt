package mx.qsistemas.infracciones.alarm

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db_web.managers.SendInfractionManagerWeb
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.request_web.*
import mx.qsistemas.infracciones.net.result_web.GenericResult
import mx.qsistemas.infracciones.net.result_web.InfractionResult
import mx.qsistemas.infracciones.utils.CHANNEL_ID_REPORT
import mx.qsistemas.infracciones.utils.NOTIF_SEND_REPORTS
import mx.qsistemas.infracciones.utils.Validator
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
                    // Get infraction fractions and motivations list
                    val requestMotivations = mutableListOf<FractionsItem>()
                    SendInfractionManagerWeb.getInfractionMotivationList(it.id).forEach { x ->
                        requestMotivations.add(FractionsItem(x.reason, x.amount.toString(), x.uma.toString(), x.fraction_id))
                    }
                    // Get offender license
                    val personLicense = SendInfractionManagerWeb.getPersonLicense(it.id)
                    // Get offender address
                    val personAddress = SendInfractionManagerWeb.getPersonAddress(it.driver_id)
                    val requestAddressDriver = AddressDriver(personAddress.colony_id, personAddress.internal_num, personAddress.city_id, personAddress.street, personAddress.exterior_num, personAddress.state_id, personAddress.cp_id)
                    // Get person information
                    val personInfo = SendInfractionManagerWeb.getPersonInformation(it.id)
                    val requestPerson = DriverRequest("", personInfo.name, personInfo.rfc, personInfo.paternal, personInfo.maternal)
                    //Get vehicle information of infraction
                    val vehicleInfraction = SendInfractionManagerWeb.getVehicleInformation(it.id)
                    // Get the capture lines
                    val requestCaptureLines = mutableListOf<CaptureLinesItem>()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    SendInfractionManagerWeb.getCaptureLines(it.id).forEach { line ->
                        requestCaptureLines.add(CaptureLinesItem("%.2f".format(line.amount).toFloat(), line.key, line.order, line.discount, dateFormat.format(SimpleDateFormat("dd/MM/yyyy").parse(line.date))))
                    }
                    //Create the infraction subheader
                    val infractionRequest = InfractionRequest(it.date, addresInfraction.colony_id, vehicleInfraction.colour_id, addresInfraction.city_id,
                            if (vehicleInfraction.year == "-") null else vehicleInfraction.year.toInt(), vehicleInfraction.identifier_document_id,
                            addresInfraction.latitude.toString(), it.town_hall_id, requestAddressDriver, requestPictures, vehicleInfraction.isNewColor,
                            vehicleInfraction.class_type_id, vehicleInfraction.num_document, addresInfraction.street, vehicleInfraction.sub_brand_id,
                            personLicense.state_license_id, vehicleInfraction.isNewSubBrand, vehicleInfraction.brand_reference, addresInfraction.longitude.toString(),
                            addresInfraction.street_a, "", personLicense.license_type_id, addresInfraction.street_b,
                            it.is_absent, it.is_impound, addresInfraction.cp_id, personLicense.license_number, vehicleInfraction.issued_in_id,
                            requestPerson, requestCaptureLines, it.insured_document_id, it.folio, it.time, requestMotivations, "ACTIVO", it.third_impound_id)
                    //Send the infractions list
                    NetworkApi().getNetworkService().sendInfractionToServer("Bearer ${Application.prefs?.loadData(R.string.sp_access_token, "")!!}", infractionRequest).enqueue(object : Callback<InfractionResult> {
                        override fun onResponse(call: Call<InfractionResult>, response: Response<InfractionResult>) {
                            if (response.code() == HttpsURLConnection.HTTP_OK) {
                                val result = response.body()
                                if (result?.status == "success") {
                                    SendInfractionManagerWeb.updateInfractionSend(result?.infringement.token, result?.infringement.folio)
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
                            Log.e(this.javaClass.simpleName, "Send Infractions Failed: ${t.message}")
                            // When done, update the notification one more time to remove the progress bar
                            builderInfraction.setContentText(getString(R.string.e_send_infractions))
                                    .setProgress(0, 0, false)
                            notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                        }
                    })
                }
            } else {
                sendPayments()
            }
        }
        return true
    }

    private fun sendPayments() {
        val payments = SendInfractionManagerWeb.getPayments()
        payments.forEach {
            val request = PaymentRequest("%.2f".format(it.discount).toFloat(), it.folio_payment, it.observations, it.payment_date, it.payment_method,
                    "%.2f".format(it.rounding).toFloat(), "%.2f".format(it.amount).toFloat(), "%.2f".format(it.surcharges).toFloat(),
                    it.infringement_id_server, "%.2f".format(it.total).toFloat(), it.authorize_no.toString())
            NetworkApi().getNetworkService().savePaymentToServer("Bearer ${Application.prefs?.loadData(R.string.sp_access_token, "")!!}",
                    it.id.toLong(), request).enqueue(object : Callback<GenericResult> {
                override fun onResponse(call: Call<GenericResult>, response: Response<GenericResult>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.body()?.status == "success") {
                            SendInfractionManagerWeb.updatePaymentSend(call.request().headers()["idPayment"]!!.toLong())
                        }
                    }
                }

                override fun onFailure(call: Call<GenericResult>, t: Throwable) {
                }
            })
        }
    }
}