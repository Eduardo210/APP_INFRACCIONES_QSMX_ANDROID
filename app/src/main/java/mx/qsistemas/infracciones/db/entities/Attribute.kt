package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Attribute(@PrimaryKey(autoGenerate = true) val id: Long,
                     @ColumnInfo(name = "ID_MODULO") val id_module: String,
                     @ColumnInfo(name = "ATRIBUTO") val attibute:String,
                     @ColumnInfo(name = "BAN_VISIBLE") val is_active: Boolean,
                     @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)