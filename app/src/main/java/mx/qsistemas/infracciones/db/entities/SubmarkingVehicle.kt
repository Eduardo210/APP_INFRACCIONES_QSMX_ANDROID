package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "submarking_vehicle")
data class SubmarkingVehicle(@PrimaryKey @ColumnInfo(name = "ID_SUBMARCA") val id_submarking_vehicle: Int,
                             @ColumnInfo(name = "ID_MARCA_VEHICULO") val id_brand: Int,
                             @ColumnInfo(name = "SUBMARCA_VEHICULO") val subbrand: String)