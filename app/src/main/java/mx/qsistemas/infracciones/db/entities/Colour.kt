package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Colour(@PrimaryKey(autoGenerate = true) val id: Int,
                  @ColumnInfo(name = "COLOR") val colour: String,
                  @ColumnInfo(name = "CODE_ASCII") val code_ascii: String,
                  @ColumnInfo(name= "BAN_ACTIVO") val is_active: Boolean,
                  @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: String)