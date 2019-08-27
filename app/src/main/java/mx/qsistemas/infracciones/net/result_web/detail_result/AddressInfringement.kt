package mx.qsistemas.infracciones.net.result_web.detail_result

import com.google.gson.annotations.SerializedName

data class AddressInfringement(

	@field:SerializedName("colony")
	val colony: String? = null,

	@field:SerializedName("street_a")
	val streetA: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("street_b")
	val streetB: String? = null,

	@field:SerializedName("cp")
	val cp: String? = null
)