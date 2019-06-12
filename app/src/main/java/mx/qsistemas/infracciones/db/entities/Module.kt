package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module")
data class Module(@PrimaryKey(autoGenerate = true) val id:Int,
                  @ColumnInfo(name = "MODULO") val module: String,
                  @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                  @ColumnInfo(name = "BAN_ACTIVO") val is_active:Boolean)