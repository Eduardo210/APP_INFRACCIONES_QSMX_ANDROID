package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "non_working_day")
data class NonWorkingDay(@PrimaryKey @ColumnInfo(name = "ID_DIA") val id:Long,
                         @ColumnInfo(name = "FECHA") val date: String)