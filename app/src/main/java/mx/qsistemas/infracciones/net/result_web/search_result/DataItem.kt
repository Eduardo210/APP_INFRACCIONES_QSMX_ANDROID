package mx.qsistemas.infracciones.net.result_web.search_result

import com.google.gson.annotations.SerializedName

data class DataItem(

        @field:SerializedName("date")
        val date: String? = null,

        @field:SerializedName("folio")
        val folio: String? = null,

        @field:SerializedName("time")
        val time: String? = null,

        @field:SerializedName("fractions")
        val fractions: List<FractionsItem?>? = null,

        @field:SerializedName("token")
        val token: Any? = null,

        @field:SerializedName("status")
        val status: String? = null,

        @field:SerializedName("vehicle")
        val vehicle: Vehicle? = null,

        @field:SerializedName("is_paid")
        val is_paid: Boolean? = null,

        @field:SerializedName("reference")
        val reference: String? = null
)