package mx.qsistemas.infracciones.net.result_web.search_result

import com.google.gson.annotations.SerializedName

data class Vehicle(

	@field:SerializedName("colour")
	val colour: String? = null,

	@field:SerializedName("num_document")
	val numDocument: String? = null,

	@field:SerializedName("sub_brand")
	val subBrand: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null
)