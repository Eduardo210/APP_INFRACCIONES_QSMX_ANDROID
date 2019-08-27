package mx.qsistemas.infracciones.net.result_web.detail_result

import com.google.gson.annotations.SerializedName

data class Address(

	@field:SerializedName("colony")
	val colony: String? = null,

	@field:SerializedName("internal_num")
	val internalNum: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("exterior_num")
	val exteriorNum: String? = null,

	@field:SerializedName("state")
	val state: String? = null
)