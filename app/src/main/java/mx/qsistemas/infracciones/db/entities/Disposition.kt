package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disposition")
data class Disposition(@PrimaryKey @ColumnInfo(name = "ID_DISPOSICION") val id: Int,
                       @ColumnInfo(name = "DISPOSICION") val disposition: String)