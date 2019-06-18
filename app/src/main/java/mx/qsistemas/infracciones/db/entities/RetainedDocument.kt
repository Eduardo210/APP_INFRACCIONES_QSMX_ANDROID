package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retained_document")
data class RetainedDocument(@PrimaryKey @ColumnInfo(name = "ID_DOCUMENTO") val id: Int,
                            @ColumnInfo(name = "DOCUMENTO") val document: String)