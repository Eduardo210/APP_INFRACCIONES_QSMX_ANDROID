package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colour")
data class Colour(@PrimaryKey @ColumnInfo(name = "ID_COLOR") val id: Int,
                  @ColumnInfo(name = "COLOR") val colour: String)