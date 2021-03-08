package mx.qsistemas.infracciones.net.request_web

import com.google.gson.annotations.SerializedName

data class VehicleInfraction(
        @field:SerializedName("sub_brand")
        val subBrand: String?=null,
        @field:SerializedName("identifier_document")
        val identifierDocument: String?=null,
        @field:SerializedName("num_doc")
        val num_document: String?=null,
        @field:SerializedName("issued_in")
        val issuedIn: String?=null,
        @field:SerializedName("colour")
        val color: String?=null,
        @field:SerializedName("class_type")
        val classType: String?=null,
        @field:SerializedName("year")
        val year: String?=null
)
