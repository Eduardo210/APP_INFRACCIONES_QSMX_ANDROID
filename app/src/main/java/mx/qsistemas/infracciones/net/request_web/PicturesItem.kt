package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class PicturesItem(

	@field:SerializedName("image")
	val image: String? = null
)