package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "submarking_vehicle")
data class SubmarkingVehicle(@ColumnInfo(name = "ID_SUBMARCA") val id_submarking_vehicle:Int,
                             @ColumnInfo(name = "ID_MARCA_VEHICULO") val submarking_vehicle:String,
                             @ColumnInfo(name = "ID_RGISTRO_USUARIO") val id_reg_user: Long,
                             @ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean)