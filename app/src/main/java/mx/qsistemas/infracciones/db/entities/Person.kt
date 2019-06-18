package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
data class Person(@PrimaryKey(autoGenerate = true) val id: Int,
                  @ColumnInfo(name = "NOMBRE") val name: String,
                  @ColumnInfo(name = "A_PATERNO") val last_name_father: String,
                  @ColumnInfo(name = "A_MATERNO") val last_name_mother: String)