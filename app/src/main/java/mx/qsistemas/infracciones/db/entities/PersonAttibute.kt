package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_attribute")
data class PersonAttibute(@PrimaryKey(autoGenerate = true) val id: Int,
                          @ColumnInfo(name = "ID_PERSONA_ATRIBUTO") val id_persona_atributo: Int,
                          @ColumnInfo(name = "ID_ATRIBUTO") val id_atribute: Int,
                          @ColumnInfo(name = "ID_PERSONA") val id_person: Long)
