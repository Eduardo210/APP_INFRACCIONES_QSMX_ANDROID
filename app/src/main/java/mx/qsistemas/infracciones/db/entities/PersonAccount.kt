package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persona_account")
data class PersonAccount(@PrimaryKey @ColumnInfo(name = "ID_CUENTA_USUARIO") val id:Long,
                         @ColumnInfo(name = "ID_PERSONA") val id_person:Long,
                         @ColumnInfo(name = "BAN_ACTIVA") val is_active: Int,
                         @ColumnInfo(name = "USER_NAME") val user_name: String,
                         @ColumnInfo(name = "PASSWORD") val password: String
                         )