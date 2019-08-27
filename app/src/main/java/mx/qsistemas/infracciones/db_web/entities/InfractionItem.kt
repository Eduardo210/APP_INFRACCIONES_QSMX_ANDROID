package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo

data class InfractionItem(@ColumnInfo(name = "id") val id_infraction: Long,
                          @ColumnInfo(name = "folio") val folio: String,
                          @ColumnInfo(name = "date") val date: String,
                          @ColumnInfo(name = "num_document") val num_document: String,
                          @ColumnInfo(name = "reason") val reason: String,
                          @ColumnInfo(name = "sync") val sync: Boolean,
                          @ColumnInfo(name = "sub_brand") val sub_brand: String,
                          @ColumnInfo(name = "brand") val brand: String,
                          @ColumnInfo(name = "colour") val colour: String)