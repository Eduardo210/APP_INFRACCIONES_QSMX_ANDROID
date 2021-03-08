package mx.qsistemas.infracciones.net.request_web

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class AddressInfraction(
        @field:SerializedName("state")
        val state: String? = null,
        @field:SerializedName("city")
        val city: String? = null,
        @field:SerializedName("cp")
        val cp: String? = null,
        @field:SerializedName("colony")
        val colony: String? = null,
        @field:SerializedName("street")
        val street: String? = null,
        @field:SerializedName("street_a")
        val streetA: String? = null,
        @field:SerializedName("street_b")
        val streetB: String? = null,
        @field:SerializedName("latitude")
        val latitud: Double? = null,
        @field:SerializedName("longitude")
        val longitud: Double? = null


)