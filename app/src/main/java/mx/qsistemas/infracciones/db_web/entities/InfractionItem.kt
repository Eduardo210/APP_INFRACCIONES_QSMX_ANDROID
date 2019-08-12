package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo

data class InfractionItem(@ColumnInfo(name = "id") val id_infraction: Long,
                          @ColumnInfo(name = "folio") val folio: String,
                          @ColumnInfo(name = "num_document") val num_document: String,
                          @ColumnInfo(name = "reason") val reason: String,
                          @ColumnInfo(name = "sync") val sync: Boolean,
                          @ColumnInfo(name = "identifier_document_id") val id_doc_ident: String,
                          @ColumnInfo(name = "sub_brand_id") val sub_brand_id: String,
                          @ColumnInfo(name = "colour_id") val colour_id: String,
                          @ColumnInfo(name = "date") val date: String)