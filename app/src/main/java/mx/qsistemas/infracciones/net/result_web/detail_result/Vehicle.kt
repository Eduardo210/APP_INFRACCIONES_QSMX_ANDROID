package mx.qsistemas.infracciones.net.result_web.detail_result

import com.google.gson.annotations.SerializedName

data class Vehicle(

	@field:SerializedName("class_type")
	val classType: String? = null,

	@field:SerializedName("issued_in")
	val issuedIn: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("year")
	val year: String? = null,

	@field:SerializedName("num_document")
	val numDocument: String? = null,

	@field:SerializedName("identifier_document")
	val identifierDocument: String? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null
)