package mx.qsistemas.infracciones.net.result_web.search_result

import com.google.gson.annotations.SerializedName

data class SearchResult(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("previous")
	val previous: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("num_pages")
	val numPages: Int? = null,

	@field:SerializedName("count")
	val count: Int? = null
)