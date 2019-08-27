package mx.qsistemas.infracciones.net.result_web.detail_result

import com.google.gson.annotations.SerializedName

data class Driver(

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rfc")
	val rfc: String? = null,

	@field:SerializedName("paternal")
	val paternal: String? = null,

	@field:SerializedName("maternal")
	val maternal: String? = null
)