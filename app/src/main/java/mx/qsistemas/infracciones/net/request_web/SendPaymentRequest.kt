package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class SendPaymentRequest(

        @field:SerializedName("token")
        val token: String? = null,
        @field:SerializedName("name")
        val name: String? = null,
        @field:SerializedName("paternal")
        val paternal: String? = null,
        @field:SerializedName("maternal")
        val maternal: String? = null,
        @field:SerializedName("rfc")
        val rfc: String? = null,
        @field:SerializedName("business_name")
        val businessName: String? = null,
        @field:SerializedName("email")
        val email: String? = null,
        @field:SerializedName("payer_card")
        val payerCard: PayerCardInfo? = null,


        )

data class PayerCardInfo(

        @field:SerializedName("amount")
        val amount: String? = null,

        @field:SerializedName("trx_nb")
        val trxNb: String? = null,

        @field:SerializedName("membership")
        val membership: String? = null,

        @field:SerializedName("cardholder")
        val cardholder: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("trx_date")
        val trxDate: String? = null,

        @field:SerializedName("app_label")
        val appLabel: String? = null,

        @field:SerializedName("bank")
        val bank: String? = null,

        @field:SerializedName("mobile_series")
        val mobileSeries: String? = null,

        @field:SerializedName("trx_time")
        val trxTime: String? = null,

        @field:SerializedName("control_number")
        val controlNumber: String? = null,

        @field:SerializedName("entry_type")
        val entryType: String? = null,

        @field:SerializedName("bank_reference")
        val bankReference: String? = null,

        @field:SerializedName("authorization_number")
        val authorizationNumber: String? = null
)

data class Payer(@field:SerializedName("business_name")
                 val businessName: String? = null,
                 @field:SerializedName("name")
                 val name: String? = null,
                 @field:SerializedName("rfc")
                 val rfc: String? = null,
                 @field:SerializedName("email")
                 val email: String? = null,
                 @field:SerializedName("paternal")
                 val paternal: String? = null,
                 @field:SerializedName("maternal")
                 val maternal: String? = null)