package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "identifier_document")
data class IdentifierDocument(@PrimaryKey(autoGenerate = true) val id: Long,
                              @ColumnInfo(name = "DOCUMENTO") val document: String,
                              @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                              @ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean)