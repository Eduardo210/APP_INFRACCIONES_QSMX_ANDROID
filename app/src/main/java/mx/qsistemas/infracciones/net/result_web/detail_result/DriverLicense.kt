package mx.qsistemas.infracciones.net.result_web.detail_result

import com.google.gson.annotations.SerializedName

data class DriverLicense(

	@field:SerializedName("license_type")
	val licenseType: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("license_number")
	val licenseNumber: String? = null
)