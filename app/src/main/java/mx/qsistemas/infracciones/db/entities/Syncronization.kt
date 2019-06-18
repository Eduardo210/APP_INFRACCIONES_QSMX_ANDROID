package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "syncronization")
data class Syncronization(@PrimaryKey(autoGenerate = true) val id: Int,
                          @ColumnInfo(name = "APLICACION_ACTIVA") val is_active: Int)