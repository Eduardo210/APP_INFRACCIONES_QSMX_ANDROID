package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonAttibute(@PrimaryKey(autoGenerate = true) val id:Long,
                          @ColumnInfo(name = "ID_PERSONA") val id_person: Int,
                          @ColumnInfo(name = "ID_ATRIBUTO") val id_atribute: Int,
                          @ColumnInfo(name = "BAN_ACTIVA") val is_active: Boolean,
                          @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)
