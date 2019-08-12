package mx.qsistemas.infracciones.net.RequestNewInfraction

import com.google.gson.annotations.SerializedName

data class PicturesItem(

	@field:SerializedName("image")
	val image: String? = null
)