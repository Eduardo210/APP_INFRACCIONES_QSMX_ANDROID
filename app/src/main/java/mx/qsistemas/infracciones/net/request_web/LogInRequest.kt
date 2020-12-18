package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

class LogInRequest(@SerializedName("str_data") val strData: String)

class ValidateTokenRequest(@SerializedName("token") val token: String)