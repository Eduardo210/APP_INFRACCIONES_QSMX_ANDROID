package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attribute")
data class Attribute(@PrimaryKey @ColumnInfo(name = "ID_ATRIBUTO") val id: Long,
                     @ColumnInfo(name = "ID_MODULO") val id_module: Int,
                     @ColumnInfo(name = "ATRIBUTO") val attibute:String,
                     //@ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean,
                     @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)