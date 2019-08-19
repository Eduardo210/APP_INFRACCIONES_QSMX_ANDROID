package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_driverlicense")
data class DriverDriverLicense(@PrimaryKey(autoGenerate = true) val id: Long?,
                               @ColumnInfo(name = "license_number") val license_number: String?,
                               @ColumnInfo(name = "driver_id") val driver_id: Long?,
                               @ColumnInfo(name = "license_type_id") val license_type_id: String?,
                               @ColumnInfo(name = "state_license_id") val state_license_id: String?)