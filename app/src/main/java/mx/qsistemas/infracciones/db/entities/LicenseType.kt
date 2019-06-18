package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "license_type")
data class LicenseType(@PrimaryKey @ColumnInfo(name = "ID") val id:Int,
                       @ColumnInfo(name = "TIPO_LICENCIA") val license_type:String,
                       //@ColumnInfo(name = "BAN_ACTIVO") val is_active: String,
                       @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)