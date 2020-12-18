package mx.qsistemas.infracciones.net.catalogs

import com.google.gson.annotations.SerializedName

class GenerateStreamToken(val api_key: String, val session_id: String, val stream_token: String)

data class CipherData(@SerializedName("isEncrypted") val isEncrypted: Boolean,
                      @SerializedName("value") val value: String)

data class CipherDataResult(@SerializedName("data") val data: Result) {
    data class Result(@SerializedName("value") val value: String)
}