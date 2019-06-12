package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ascription")
data class Ascription(@PrimaryKey(autoGenerate = true) val id: Long,
                      @ColumnInfo(name= "BAN_VISIBLE") val is_active:Boolean,
                      @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                      @ColumnInfo(name="ID_SUBDIRECCION") val id_subdirection: Int,
                      @ColumnInfo(name = "BAN_ESTATAL") val ban_estatal: Boolean,
                      @ColumnInfo(name = "ID_ESTADO") val id_estado: Int,
                      @ColumnInfo(name = "ID_MUNICIPIO") val id_municipio: Int
                      )