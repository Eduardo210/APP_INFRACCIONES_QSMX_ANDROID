package mx.qsistemas.infracciones.net.result_web

import com.google.gson.annotations.SerializedName

data class InfractionResult(@SerializedName("data") val data: Infringement,
                            @SerializedName("status") val status: String) {

    data class Infringement(@SerializedName("folio") val folio: String,
                            @SerializedName("token") val token: String)
}