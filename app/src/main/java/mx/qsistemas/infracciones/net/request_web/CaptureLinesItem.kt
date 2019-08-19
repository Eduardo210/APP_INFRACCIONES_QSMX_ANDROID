package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class CaptureLinesItem(

	@field:SerializedName("amount")
	val amount: Float? = null,

	@field:SerializedName("key")
	val key: String? = null,

	@field:SerializedName("order")
	val order: Int? = null
)