package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_brand")
data class VehicleBrand(@PrimaryKey @ColumnInfo(name = "ID_MARCA_VEHICULO") val id: Int,
                        @ColumnInfo(name = "MARCA_VEHICULO") val vehicle_brand: String,
                        //@ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean,
                        @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)