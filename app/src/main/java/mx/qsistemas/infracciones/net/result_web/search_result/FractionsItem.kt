package mx.qsistemas.infracciones.net.result_web.search_result

import com.google.gson.annotations.SerializedName

data class FractionsItem(

        @field:SerializedName("fraction")
        val fraction: String? = null,

        @field:SerializedName("article")
        val article: String? = null,
        @field:SerializedName("motivation")
        val reason: String? = null,


        )