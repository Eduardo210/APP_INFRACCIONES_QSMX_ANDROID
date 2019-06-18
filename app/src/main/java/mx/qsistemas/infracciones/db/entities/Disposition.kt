package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disposition")
data class Disposition (@PrimaryKey @ColumnInfo(name = "ID_DISPOSICION") val id: Int,
                        @ColumnInfo(name = "DISPOSICION") val disposition: String,
                        @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long
                        //@ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean
                        )