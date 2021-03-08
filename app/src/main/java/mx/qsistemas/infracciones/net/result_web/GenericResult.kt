package mx.qsistemas.infracciones.net.result_web

import com.google.gson.annotations.SerializedName

class GenericResult(@SerializedName("folio") val folio: String,
                    @SerializedName("message") val error: String)