package mx.qsistemas.infracciones.net.result_web.detail_result

import com.google.gson.annotations.SerializedName

data class CaptureLinesItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("key")
	val key: String? = null,

	@field:SerializedName("order")
	val order: Int? = null
)