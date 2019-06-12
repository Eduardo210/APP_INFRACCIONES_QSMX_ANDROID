package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="articles" )
data class Articles(@PrimaryKey(autoGenerate = true) val id:Long,
                    @ColumnInfo(name = "ARTICULO") val article:String,
                    @ColumnInfo(name = "DESCRIPCION") val description: String,
                    @ColumnInfo(name = "BAN_VISIBLE") val is_active: Boolean,
                    @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)