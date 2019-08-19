package mx.qsistemas.infracciones.net.result_web

import com.google.gson.annotations.SerializedName

data class LogInResult(
        @SerializedName("access_token")
        val access_token: String,
        @SerializedName("refresh_token")
        val refresh_token: String,
        @SerializedName("id_person")
        val idPerson: Int,
        @SerializedName("name_officer")
        val nameOfficer: String,
        @SerializedName("last_name_officer")
        val lastNameOfficer: String,
        @SerializedName("sec_last_name_officer")
        val secLastNameOfficer: String,
        @SerializedName("url_photo")
        val urlPhoto: String
)