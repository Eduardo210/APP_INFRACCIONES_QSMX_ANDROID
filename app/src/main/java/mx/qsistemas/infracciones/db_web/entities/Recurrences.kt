package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurrences")
data class Recurrences(@PrimaryKey(autoGenerate = true) val idRecurrences: Int,
                       @ColumnInfo(name = "id_infraccion") val name: String,
                       @ColumnInfo(name = "folio") val folio: String,
                       @ColumnInfo(name = "id_fraction") val idFraction: String,
                       @ColumnInfo(name = "id_document_identifyr") val idDocumentIdentify: String,
                       @ColumnInfo(name = "number_document_identify") val numberDocumentIdentity : String,
                       @ColumnInfo(name = "infraction_date")  val infractioNumber : String)
