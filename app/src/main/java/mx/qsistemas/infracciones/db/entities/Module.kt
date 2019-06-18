package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module")
data class Module(@PrimaryKey @ColumnInfo(name = "ID_MODULO") val id: Int,
                  @ColumnInfo(name = "MODULO") val module: String)