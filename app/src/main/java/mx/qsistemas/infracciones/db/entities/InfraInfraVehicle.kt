package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "infra_infra_vehicle", primaryKeys = ["ID_INFRACCION"])
data class InfraInfraVehicle (@ColumnInfo(name = "ID_INFRACCION") val id_infra: Int,
                              @ColumnInfo(name = "ID_VEHICLE") val id_vehicle: Int)
