package mx.qsistemas.infracciones.net.result_web.search_result

import com.google.gson.annotations.SerializedName

data class FractionsItem(

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("id_fraction")
	val idFraction: String? = null,

	@field:SerializedName("uma")
	val uma: String? = null,

	@field:SerializedName("num_fraction")
	val numFraction: String? = null,

	@field:SerializedName("article")
	val article: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)