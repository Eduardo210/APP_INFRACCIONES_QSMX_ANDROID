package mx.qsistemas.infracciones.net.result_web

import com.google.gson.annotations.SerializedName

class GenericResult(@SerializedName("status") val status: String,
                    @SerializedName("errors") val error: String)