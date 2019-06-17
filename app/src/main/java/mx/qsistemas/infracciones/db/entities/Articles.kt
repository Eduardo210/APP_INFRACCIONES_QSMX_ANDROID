package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="articles" )
data class Articles(@PrimaryKey @ColumnInfo(name = "ID") val id:Long,
                    @ColumnInfo(name = "ARTICULO") val article:String,
                    @ColumnInfo(name = "DESCRIPCION") val description: String,
                    @ColumnInfo(name = "BAB_VISIBLE") val is_active: Int,
                    @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)