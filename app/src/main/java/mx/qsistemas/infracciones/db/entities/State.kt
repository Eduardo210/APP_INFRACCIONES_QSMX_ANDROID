package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "state")
data class State(@PrimaryKey(autoGenerate = true) val id: Int,
                 @ColumnInfo(name = "ID_PAIS") val id_country: Int,
                 @ColumnInfo(name = "ESTADO") val state: String,
                 @ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean,
                 @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long
)