package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colour")
data class Colour(@PrimaryKey @ColumnInfo(name = "ID_COLOR") val id: Int,
                  @ColumnInfo(name = "COLOR") val colour: String,
                  @ColumnInfo(name = "CODE_ASCII") val code_ascii: String,
                  //@ColumnInfo(name= "BAN_ACTIVO") val is_active: Boolean,
                  @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: String)