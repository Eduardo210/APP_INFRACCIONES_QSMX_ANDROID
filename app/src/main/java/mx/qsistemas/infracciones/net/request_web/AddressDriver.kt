package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class AddressDriver(
        @field:SerializedName("city")
        val city: String? = null,
        @field:SerializedName("colony")
        val colony: String? = null,
        @field:SerializedName("street")
        val street: String? = null,
        @field:SerializedName("exterior_num")
        val exteriorNum: String? = null,
        @field:SerializedName("internal_num")
        val internalNum: String? = null
)