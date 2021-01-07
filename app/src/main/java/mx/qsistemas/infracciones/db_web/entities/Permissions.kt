package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "permissions")
data class Permissions(@PrimaryKey(autoGenerate = true) val id: Long,
                       @ColumnInfo(name = "name") val name: String,
                       @ColumnInfo(name = "code_name") val codeName: String)