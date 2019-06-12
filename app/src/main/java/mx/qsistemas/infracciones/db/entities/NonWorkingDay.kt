package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class NonWorkingDay(@PrimaryKey(autoGenerate = true) val id:Long,
                         @ColumnInfo(name = "FECHA") val date: Date,
                         @ColumnInfo(name = "DESCRIPCION") val description: String,
                         @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: String,
                         @ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean)