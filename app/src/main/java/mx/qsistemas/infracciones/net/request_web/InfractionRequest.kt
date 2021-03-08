package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class InfractionRequest(

        @field:SerializedName("folio")
        val folio: String? = null,
        @field:SerializedName("town_hall")
        val townHall: Long? = null,
        @field:SerializedName("date")
        val date: String? = null,
        @field:SerializedName("time")
        val time: String? = null,
        @field:SerializedName("insured_document")
        val insuredDocument: String? = null,
        @field:SerializedName("is_impound")
        val isImpound: Boolean? = null,
        @field:SerializedName("third_impound")
        val thirdImpund: String? = null,
        @field:SerializedName("address")
        val address: AddressInfraction?=null,
        @field:SerializedName("vehicle")
        val vehicle: VehicleInfraction?=null,
        @field:SerializedName("fractions")
        val fractions: MutableList<FractionsItem>,
        @field:SerializedName("is_absent")
        val isAbsent: Boolean? = null,
        @field:SerializedName("driver")
        val driver: DriverRequest? = null,
        @field:SerializedName("address_driver")
        val addressDriver: AddressDriver?=null,
        @field:SerializedName("driver_license")
        val driverLicense: DriverLicense?=null,
        @field:SerializedName("capture_lines")
        val captureLines: MutableList<CaptureLinesItem>? = null,
        @field:SerializedName("payment_card")
        val paymentCard: Boolean?=null,
        val payer: PayerInfraction?=null,
        val payerCard: PayerCard?=null
)