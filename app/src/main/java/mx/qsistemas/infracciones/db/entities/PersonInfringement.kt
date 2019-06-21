package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "person_infringement", primaryKeys = ["ID_INFRACCION", "ID_PERSONA"])
data class PersonInfringement(@ColumnInfo(name="ID_INFRACCION") val id_infringement: Int,
                              @ColumnInfo(name = "ID_PERSONA") val id_person: Long)
