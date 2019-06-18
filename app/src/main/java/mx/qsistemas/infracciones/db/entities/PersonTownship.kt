package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_towship")
data class PersonTownship(@PrimaryKey @ColumnInfo(name = "ID_PERSONA_AYUNTAMIENTO") val id: Int,
                          @ColumnInfo(name = "ID_PERSONA") val id_person: Long,
                          @ColumnInfo(name = "EMPLEADO") val employee: String)