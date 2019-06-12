package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "non_working_day")
data class NonWorkingDay(@PrimaryKey(autoGenerate = true) val id:Long,
                         @ColumnInfo(name = "FECHA") val date: String,
                         @ColumnInfo(name = "DESCRIPCION") val description: String,
                         @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: String,
                         @ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean)