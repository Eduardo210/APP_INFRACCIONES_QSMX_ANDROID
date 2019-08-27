package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_townhall")
data class PersonTownhall(@PrimaryKey(autoGenerate = false) val idPersona: Long,
                          @ColumnInfo(name = "name") val name: String,
                          @ColumnInfo(name= "paternal") val paternal: String,
                          @ColumnInfo(name= "maternal") val maternal: String,
                          @ColumnInfo(name = "infringement_id") val infringement_id: Long)
                          //@ColumnInfo(name = "dis"))