package mx.qsistemas.infracciones.net.RequestNewInfraction

import com.google.gson.annotations.SerializedName

data class FractionsItem(

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("uma")
	val uma: String? = null,

	@field:SerializedName("fraction")
	val fraction: String? = null
)