package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "town_sepo_mex")
data class TownSepoMex(@PrimaryKey @ColumnInfo(name = "ID_MUNICIPIO") val id_town:Int,
                       @ColumnInfo(name = "ID_ESTADO") val id_state:Int,
                       @ColumnInfo(name = "MUNICIPIO") val town: String,
                       //@ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean,
                       @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)