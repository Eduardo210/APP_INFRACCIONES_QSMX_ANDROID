package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class FractionsItem(

	@field:SerializedName("motivation")
	val motivation: String? = null,
	@field:SerializedName("fraction")
	val fraction: String? = null,
	@field:SerializedName("uma")
	val uma: String? = null,
	@field:SerializedName("amount")
	val amount: String? = null,

)