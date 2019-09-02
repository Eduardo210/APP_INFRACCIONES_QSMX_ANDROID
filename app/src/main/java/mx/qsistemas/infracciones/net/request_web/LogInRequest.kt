package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

class LogInRequest(@SerializedName("username") val username: String,
                   @SerializedName("password") val bbox: String)

class ValidateTokenRequest(@SerializedName("token") val token: String)