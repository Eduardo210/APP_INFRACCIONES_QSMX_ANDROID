package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
        @SerializedName("discount") val discount: Float,
        @SerializedName("folio_payment") val folio_payment: String,
        @SerializedName("observations") val observations: String,
        @SerializedName("payment_date") val payment_date: String,
        @SerializedName("payment_method") val payment_method: String = "CARD",
        @SerializedName("rounding") val rounding: Float,
        @SerializedName("subtotal") val subtotal: Float,
        @SerializedName("surcharges") val surcharges: Float,
        @SerializedName("token") val token: String,
        @SerializedName("total") val total: Float,
        @SerializedName("num_authorization") val num_authorization: String)