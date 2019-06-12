package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonAccount(@PrimaryKey(autoGenerate = true) val id:Long,
                         @ColumnInfo(name = "ID_PERSONA") val id_person:Long,
                         @ColumnInfo(name = "BAN_ACTIVA") val is_active: Boolean,
                         @ColumnInfo(name = "USER_NAME") val user_name: String,
                         @ColumnInfo(name = "PASSWORD") val password: String,
                         @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user:Long
                         )