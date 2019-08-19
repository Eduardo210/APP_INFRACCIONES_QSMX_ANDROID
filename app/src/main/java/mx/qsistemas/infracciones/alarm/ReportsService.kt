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
import mx.qsistemas.infracciones.utils.CHANNEL_ID_REPORT
import mx.qsistemas.infracciones.utils.NOTIF_SEND_REPORTS
import mx.qsistemas.infracciones.utils.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
                    val requestPerson = Driver(personInfo.name, personInfo.rfc, personInfo.paternal, personInfo.maternal)
                    //Get vehicle information of infraction
                    val vehicleInfraction = SendInfractionManagerWeb.getVehicleInformation(it.id)
                    // Get the capture lines
                    val requestCaptureLines = mutableListOf<CaptureLinesItem>()
                    SendInfractionManagerWeb.getCaptureLines(it.id).forEach { line ->
                        requestCaptureLines.add(CaptureLinesItem(line.amount, line.key, line.order))
                    }
                    //Create the infraction subheader
                    val infractionRequest = RequestInfraction(it.date, addresInfraction.colony_id, vehicleInfraction.colour_id, addresInfraction.city_id,
                            if (vehicleInfraction.year == "-") null else vehicleInfraction.year.toInt(), vehicleInfraction.identifier_document_id,
                            addresInfraction.latitude.toString(), it.town_hall_id, requestAddressDriver, requestPictures, vehicleInfraction.isNewColor,
                            vehicleInfraction.class_type_id, vehicleInfraction.num_document, addresInfraction.street, vehicleInfraction.sub_brand_id,
                            personLicense.state_license_id, vehicleInfraction.isNewSubBrand, vehicleInfraction.brand_reference, addresInfraction.longitude.toString(),
                            addresInfraction.street_a, "", personLicense.license_type_id, addresInfraction.street_b,
                            it.is_absent, false, addresInfraction.cp_id, personLicense.license_number, vehicleInfraction.issued_in_id,
                            requestPerson, requestCaptureLines, it.insured_document_id, it.folio, it.time, requestMotivations, "ACTIVO")
                    //Send the infractions list
                    NetworkApi().getNetworkService().sendInfractionToServer(infractionRequest).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.code() == HttpsURLConnection.HTTP_OK) {
                                /*val result = Gson().fromJson(response.body(), SendInfractionResponse::class.java)
                                if (result.flag) {
                                    *//*Log.e(this.javaClass.simpleName, "All items were saved!!!")
                                    reportsToSend.forEach {
                                        SendInfractionManagerWeb.updateInfractionToSend(it.folio)
                                        if (it.is_paid == 1) {
                                            SendInfractionManagerWeb.updatePaymentToSend(it.id.toLong())
                                        }
                                    }
                                    sendPayments()*//*
                                    // When done, update the notification one more time to remove the progress bar
                                    if (reportsSend == index + 1) {
                                        builderInfraction.setContentText(getString(R.string.s_infraction_send))
                                                .setProgress(0, 0, false)
                                    } else {
                                        builderInfraction.setContentText(getString(R.string.s_infraction_send))
                                                .setProgress(reportsToSend.size, reportsSend, true)
                                    }
                                    notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                                } else {
                                    Log.e(this.javaClass.simpleName, "Items that weren't saved: ${result.folios}")
                                    reportsToSend.forEach {
                                        if (it.folio !in result.folios) {
                                            SendInfractionManagerWeb.updateInfractionToSend(it.folio)
                                            if (it.is_paid == 1) {
                                                SendInfractionManagerWeb.updatePaymentToSend(it.id.toLong())
                                            }
                                        }
                                    }
                                    // When done, update the notification one more time to remove the progress bar
                                    builderInfraction.setContentText(getString(R.string.s_infraction_send))
                                            .setProgress(reportsToSend.size, reportsSend, false)
                                    notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                                }*/
                            } else {
                                // When done, update the notification one more time to remove the progress bar
                                builderInfraction.setContentText(getString(R.string.e_send_infractions_incomplete))
                                        .setProgress(reportsToSend.size, reportsSend, false)
                                notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e(this.javaClass.simpleName, "Send Infractions Failed: ${t.message}")
                            // When done, update the notification one more time to remove the progress bar
                            builderInfraction.setContentText(getString(R.string.e_send_infractions))
                                    .setProgress(reportsToSend.size, reportsSend, false)
                            notification.notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                        }
                    })
                }
            }
            sendPayments()
        }
        return true
    }

    private fun sendPayments() {
        /* val idRegUser = 0//Application.prefs?.loadDataInt(R.string.sp_id_township_person)!!.toLong()
       val idPerson = Application.prefs?.loadDataInt(R.string.sp_id_person)!!.toLong()
       val payments = SendInfractionManager.getPaymentsToSend()
       if (payments.size > 0) {
           payments.forEach {
               val transaction = SendInfractionManager.getTransactionToSend(it.id_infringement.toLong())
               val folio = SendInfractionManager.getFolioOfInfraction(it.id_infringement.toLong())
               val paymentCardData = UpdatePaymentRequest.UpdatePaymentCardData(transaction.aid, transaction.app_label, transaction.arqc, transaction.auth_nb,
                       transaction.entry_type, transaction.masked_pan, transaction.trx_date, transaction.trx_nb, transaction.trx_time, transaction.serial_paypda, idRegUser.toString(),
                       transaction.afiliacion, transaction.vigencia_tarjeta, transaction.mensaje, transaction.tipo_tarjeta, transaction.tipo,
                       transaction.banco_emisor, transaction.referencia, it.total.toString(), transaction.tvr, transaction.tsi, transaction.numero_control,
                       transaction.tarjetahabiente, transaction.emv_data, transaction.tipo_transaccion)
               val paymentData = UpdatePaymentRequest.UpdatePaymentData(it.id_payment_method, it.subtotal.toString(), it.discount.toString(), it.total.toString(),
                       transaction.auth_nb, it.observation, idPerson)
               val request = UpdatePaymentRequest("", folio, "InfraMobile", "CF2E3EF25C90EB567243ADFACD4AA868", paymentCardData,
                       paymentData)
              NetworkApi().getNetworkService().savePayment(it.id_infringement.toLong(), Gson().toJson(request)).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            val data = Gson().fromJson(response.body(), ServiceResponse::class.java)
                            if (data.flag) {
                                val idInfraction = call.request().header("id_infraction")!!.toLong()
                                SendInfractionManager.updatePaymentToSend(idInfraction)
                            }
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                    }
                })
           }
       }*/
    }
}