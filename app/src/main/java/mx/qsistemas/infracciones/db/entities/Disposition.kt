package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Disposition (@PrimaryKey(autoGenerate = true) val id: Long,
                        @ColumnInfo(name = "DISPOSICION") val disposition: String,
                        @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                        @ColumnInfo(name = "BAN_ACTIVA") val is_active: Boolean
                        )