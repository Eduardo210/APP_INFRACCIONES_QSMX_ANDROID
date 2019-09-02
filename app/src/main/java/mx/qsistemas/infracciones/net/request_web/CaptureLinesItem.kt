package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class CaptureLinesItem(
        @field:SerializedName("amount")
        val amount: Float? = null,

        @field:SerializedName("key")
        val key: String? = null,

        @field:SerializedName("order")
        val order: Int? = null,

        @field:SerializedName("discount_label")
        val discountLabel: String? = null,

        @field:SerializedName("date")
        val date: String? = null
)