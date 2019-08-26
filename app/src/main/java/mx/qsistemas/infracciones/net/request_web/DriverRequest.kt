package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class DriverRequest(
        @field:SerializedName("token")
        val tokenInfraction: String,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("rfc")
        val rfc: String? = null,

        @field:SerializedName("paternal")
        val paternal: String? = null,

        @field:SerializedName("maternal")
        val maternal: String? = null
)