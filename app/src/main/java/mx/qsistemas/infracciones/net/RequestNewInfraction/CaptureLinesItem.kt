package mx.qsistemas.infracciones.net.RequestNewInfraction

import com.google.gson.annotations.SerializedName

data class CaptureLinesItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("key")
	val key: String? = null,

	@field:SerializedName("order")
	val order: Int? = null
)