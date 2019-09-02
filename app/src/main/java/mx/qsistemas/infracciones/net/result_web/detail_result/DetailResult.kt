package mx.qsistemas.infracciones.net.result_web.detail_result

import com.google.gson.annotations.SerializedName

data class DetailResult(

	@field:SerializedName("address_infringement")
	val addressInfringement: AddressInfringement? = null,

	@field:SerializedName("driver")
	val driver: Driver? = null,

	@field:SerializedName("capture_lines")
	val captureLines: List<CaptureLinesItem?>? = null,

	@field:SerializedName("insured_document")
	val insuredDocument: String? = null,

	@field:SerializedName("is_absent")
	val isAbsent: Boolean? = null,

	@field:SerializedName("fractions")
	val fractions: List<FractionsItem?>? = null,

	@field:SerializedName("driver_license")
	val driverLicense: DriverLicense? = null,

	@field:SerializedName("town_hall")
	val townHall: String? = null,

	@field:SerializedName("is_insured")
	val isInsured: Boolean? = null,

	@field:SerializedName("vehicle")
	val vehicle: Vehicle? = null
)