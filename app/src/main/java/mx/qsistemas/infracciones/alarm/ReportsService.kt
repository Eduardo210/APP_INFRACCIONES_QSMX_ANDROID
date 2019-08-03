package mx.qsistemas.infracciones.alarm

import android.app.job.JobParameters
import android.app.job.JobService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.managers.SendInfractionManager
import mx.qsistemas.infracciones.net.RequestNewInfraction.PicturesItem
import mx.qsistemas.infracciones.net.catalogs.UpdatePaymentRequest
import mx.qsistemas.infracciones.utils.CHANNEL_ID_IMAGES
import mx.qsistemas.infracciones.utils.NOTIF_SEND_IMAGES


class ReportsService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        /* Create Infraction Notification Builder */
       /* val builderInfraction = NotificationCompat.Builder(this, CHANNEL_ID_REPORT).apply {
            setContentTitle(getString(R.string.app_name))
            setContentText("Enviando infracciones...")
            setSmallIcon(R.drawable.transparent_launcher)
            color = ContextCompat.getColor(this@ReportsService, R.color.colorPrimary)
            priority = NotificationCompat.PRIORITY_HIGH
        }
        if (Validator.isNetworkEnable(Application.getContext())) {
             //Get the reports to send
            val reportsToSend = SendInfractionManagerWeb.getInfractionsToSend()
            var infractionsList = mutableListOf<RequestInfraction>()
            if (reportsToSend.size > 0) {
                 //Launch Notification
                val notification = NotificationManagerCompat.from(this).apply {
                    builderInfraction.setProgress(0, 0, true)
                    notify(NOTIF_SEND_REPORTS, builderInfraction.build())
                }
                 //Generate each infraction object to send
                reportsToSend.forEach {
                     //Get address of the infraction
                    val addresInfraction = SendInfractionManagerWeb.getInfractionAddress(it.id)
                    // Get infraction fractions and motivations list
                    val motivationList = SendInfractionManagerWeb.getInfractionMotivationList(it.id)
                    val motivationListRequest = mutableListOf<FractionsItem>()
                    // Save the motivation list into the request
                    motivationList.forEach { x ->
                        motivationListRequest.add(FractionsItem(x.reason, x.amount.toString(), x.uma.toString(), x.infringements_id))
                    }
                    // Get offender address
                    val personAddress = SendInfractionManagerWeb.getPersonAddress(it.id)
                    // Get person information
                    val personInfo = SendInfractionManagerWeb.getPersonInformation(it.id)
                    // Get payment information of the infraction
                    val paymentInfringement = SendInfractionManagerWeb.getPaymentInfrigment(it.id.toLong())
                     //Get payment transaction information of the infraction
                    val transactionInfo = SendInfractionManagerWeb.getPaymentTransactionInfo(it.id.toLong())
                     //Get vehicle information of infraction
                    val vehicleInfraction = SendInfractionManagerWeb.getVehileInformation(it.id.toLong())
                     //Create the infraction subheader
                    val infractionRequest = RequestInfraction(it.is_absent, it.is_paid, it.forwarded_deposit, it.retained_document,
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
                 //Create request header
                val saveInfractionRequest = SaveInfractionRequest(infractionsList, "CF2E3EF25C90EB567243ADFACD4AA868", "InfraMobile")
                 //Send the infractions list
                NetworkApi().getNetworkService().sendInfractionToServer(Gson().toJson(saveInfractionRequest)).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.code() == HttpsURLConnection.HTTP_OK) {
                            val result = Gson().fromJson(response.body(), SendInfractionResponse::class.java)
                            if (result.flag) {
                                Log.e(this.javaClass.simpleName, "All items were saved!!!")
                                reportsToSend.forEach {
                                    SendInfractionManagerWeb.updateInfractionToSend(it.folio)
                                    if (it.is_paid == 1) {
                                        SendInfractionManagerWeb.updatePaymentToSend(it.id.toLong())
                                    }
                                }
                                sendPayments()
                                // When done, update the notification one more time to remove the progress bar
                                builderInfraction.setContentText(getString(R.string.s_infraction_send))
                                        .setProgress(0, 0, false)
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
            sendPayments()
            sendPhotos()
        }*/
        return true
    }

    private fun sendPhotos() {
        /* Create Photos Notification Builder */
        val builderPhotos = NotificationCompat.Builder(this, CHANNEL_ID_IMAGES).apply {
            setContentTitle(getString(R.string.app_name))
            setContentText("Enviando imágenes...")
            setSmallIcon(R.drawable.transparent_launcher)
            color = ContextCompat.getColor(this@ReportsService, R.color.colorPrimary)
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
            val photosToSend = mutableListOf<PicturesItem>()
            photos.forEach {
                photosToSend.add(PicturesItem(it.evidence1))
                photosToSend.add(PicturesItem(it.evidence2))
            }
            /* Create request header */
           /* val request = InfractionPhotoRequest(photosToSend, "CF2E3EF25C90EB567243ADFACD4AA868", "Mobile")
            NetworkApi().getNetworkService().sendImagesToServer(Gson().toJson(request)).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.code() == HttpsURLConnection.HTTP_OK) {
                        val result = Gson().fromJson(response.body(), SendInfractionResponse::class.java)
                        if (result.flag) {
                            photos.forEach {
                                SendInfractionManager.updateImageToSend(it.idInfraction.toLong())
                            }
                            // When done, update the notification one more time to remove the progress bar
                            builderPhotos.setContentText(getString(R.string.s_images_send))
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
            })*/
        } else {
            /* If there aren't any images to send, proceed to delete the send images from DB */
            SendInfractionManager.deleteSendImages()
        }
    }

    private fun sendPayments() {
        val idRegUser = Application.prefs?.loadDataInt(R.string.sp_id_township_person)!!.toLong()
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
               /* NetworkApi().getNetworkService().savePayment(it.id_infringement.toLong(), Gson().toJson(request)).enqueue(object : Callback<String> {
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
                })*/
            }
        }
    }
}