package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "state")
data class State(@PrimaryKey @ColumnInfo(name = "ID_ESTADO") val id: Int,
                 @ColumnInfo(name = "ID_PAIS") val id_country: Int,
                 @ColumnInfo(name = "ESTADO") val state: String)