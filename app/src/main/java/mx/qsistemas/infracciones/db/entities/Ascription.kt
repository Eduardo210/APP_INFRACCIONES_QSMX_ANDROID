package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ascription")
data class Ascription(@PrimaryKey @ColumnInfo(name = "ID_ADSCRIPCION") val id: Long,
                      @ColumnInfo(name = "ADSCRIPCION") val ascription: String,
                      @ColumnInfo(name = "BAN_VISIBLE") val is_active: Int
)