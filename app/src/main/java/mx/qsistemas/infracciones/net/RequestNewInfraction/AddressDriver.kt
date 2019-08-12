package mx.qsistemas.infracciones.net.RequestNewInfraction

import com.google.gson.annotations.SerializedName

data class AddressDriver(

	@field:SerializedName("colony")
	val colony: String? = null,

	@field:SerializedName("internal_num")
	val internalNum: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("exterior_num")
	val exteriorNum: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("cp")
	val cp: String? = null
)