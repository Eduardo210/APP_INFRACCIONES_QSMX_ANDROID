package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class RequestInfraction(

        @field:SerializedName("date")
        val date: String? = null,

        @field:SerializedName("colony")
        val colony: String? = null,

        @field:SerializedName("color")
        val color: String? = null,

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("year")
        val year: Int? = null,

        @field:SerializedName("identifier_document")
        val identifierDocument: String? = null,

        @field:SerializedName("latitude")
        val latitude: String? = null,

        @field:SerializedName("town_hall")
        val townHall: Long? = null,

        @field:SerializedName("address_driver")
        val addressDriver: AddressDriver? = null,

        @field:SerializedName("pictures")
        val pictures: MutableList<PicturesItem>? = null,

        @field:SerializedName("is_new_color")
        val isNewColor: Boolean? = null,

        @field:SerializedName("class_type")
        val classType: String? = null,

        @field:SerializedName("num_document")
        val numDocument: String? = null,

        @field:SerializedName("street")
        val street: String? = null,

        @field:SerializedName("model")
        val model: String? = null,

        @field:SerializedName("state_license")
        val stateLicense: String? = null,

        @field:SerializedName("is_new_model")
        val isNewModel: Boolean? = null,

        @field:SerializedName("brand")
        val brand: String? = null,

        @field:SerializedName("longitude")
        val longitude: String? = null,

        @field:SerializedName("street_a")
        val streetA: String? = null,

        @field:SerializedName("num_document_condonation")
        val numDocumentCondonation: String = "",

        @field:SerializedName("license_type")
        val licenseType: String? = null,

        @field:SerializedName("street_b")
        val streetB: String? = null,

        @field:SerializedName("is_absent")
        val isAbsent: Boolean? = null,

        @field:SerializedName("is_impound")
        val isImpound: Boolean? = null,

        @field:SerializedName("cp")
        val cp: String? = null,

        @field:SerializedName("license_number")
        val licenseNumber: String? = null,

        @field:SerializedName("issued_in")
        val issuedIn: String? = null,

        @field:SerializedName("driver")
        val driver: Driver? = null,

        @field:SerializedName("capture_lines")
        val captureLines: List<CaptureLinesItem?>? = null,

        @field:SerializedName("insured_document")
        val insuredDocument: Int? = null,

        @field:SerializedName("folio")
        val folio: String? = null,

        @field:SerializedName("time")
        val time: String? = null,

        @field:SerializedName("fractions")
        val fractions: List<FractionsItem?>? = null,

        @field:SerializedName("status")
        val status: String = "ACTIVO"
)