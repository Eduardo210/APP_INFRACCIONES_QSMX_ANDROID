package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_townhall")
data class PersonTownhall(@PrimaryKey(autoGenerate = true) val id: Long,
                          @ColumnInfo(name = "employee") val employee: String,
                          @ColumnInfo(name = "is_active") val is_active: Boolean)
                          //@ColumnInfo(name = "dis"))