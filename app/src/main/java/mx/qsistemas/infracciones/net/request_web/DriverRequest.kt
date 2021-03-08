package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class DriverRequest(

        @field:SerializedName("name")
        val name: String? = null,
        @field:SerializedName("paternal")
        val paternal: String? = null,
        @field:SerializedName("maternal")
        val maternal: String? = null

)

data class DriverLicense(
        @field:SerializedName("license_number")
        val licenseNumber: String?=null,
        @field:SerializedName("license_type")
        val licenseType: String?=null,
        @field:SerializedName("state_license")
        val licenseState: String? = null
)

