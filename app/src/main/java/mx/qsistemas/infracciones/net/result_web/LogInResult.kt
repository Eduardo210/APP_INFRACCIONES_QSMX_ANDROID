package mx.qsistemas.infracciones.net.result_web

import com.google.gson.annotations.SerializedName

data class LogInResult(@SerializedName("message") val message: String?,
                       @SerializedName("data") val data: InfoLogin)

data class InfoLogin(@SerializedName("city") val city: String,
                     @SerializedName("town_hall_person_id") val idPerson: Long,
                     @SerializedName("user") val user: String,
                     @SerializedName("person_name") val personName: String,
                     @SerializedName("person_paternal") val personPaternal: String,
                     @SerializedName("person_maternal") val personMaternal: String,
                     @SerializedName("employee") val employee: String,
                     @SerializedName("image") val image: String?,
                     @SerializedName("permissions") val permissions: MutableList<CPermissions>)

data class CPermissions(@SerializedName("name") val name: String,
                       @SerializedName("code_name") val code: String)