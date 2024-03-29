package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_divers")
data class DriverDrivers(@PrimaryKey(autoGenerate = true) val id: Long,
                         @ColumnInfo(name = "name") val name: String,
                         @ColumnInfo(name = "paternal") val paternal:String,
                         @ColumnInfo(name = "maternal") val maternal: String,
                         @ColumnInfo(name= "rfc") val rfc: String)