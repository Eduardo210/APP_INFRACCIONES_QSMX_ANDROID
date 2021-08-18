package mx.qsistemas.infracciones.net.result_web

import com.google.gson.annotations.SerializedName

class RecurrenceGeneric(@SerializedName("fraction") val fraction: Int,
                        @SerializedName("infringement") val infringement: Int,
                        @SerializedName("folio") val folio: String,
                        @SerializedName("date_infringement") val date : String,
                        @SerializedName("id_identifier_document") val document : Int,
                        @SerializedName("num_doc") val numDoc : String)
