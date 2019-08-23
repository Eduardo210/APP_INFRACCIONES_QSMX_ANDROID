package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class PaymentRequest(@SerializedName("discount") val discount: Double,
                          @SerializedName("folio_payment") val folio_payment: String,
                          @SerializedName("observations") val observations: String,
                          @SerializedName("payment_date") val payment_date: String,
                          @SerializedName("payment_method") val payment_method: String = "CARD",
                          @SerializedName("rounding") val rounding: Double,
                          @SerializedName("subtotal") val subtotal: Int,
                          @SerializedName("surcharges") val surcharges: Int,
                          @SerializedName("token") val token: String,
                          @SerializedName("total") val total: Int,
                          @SerializedName("num_authorization") val num_authorization: String)