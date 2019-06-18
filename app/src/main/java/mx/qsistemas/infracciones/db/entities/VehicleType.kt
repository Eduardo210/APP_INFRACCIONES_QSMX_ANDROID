package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_type")
data class VehicleType(@PrimaryKey @ColumnInfo(name = "ID_TIPO") val id: Long,
                       @ColumnInfo(name = "TIPO") val type_string: String)