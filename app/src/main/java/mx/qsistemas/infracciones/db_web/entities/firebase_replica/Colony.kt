package mx.qsistemas.infracciones.db_web.entities.firebase_replica

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colony")
class Colony(@PrimaryKey(autoGenerate = true) val id: Int,
             @ColumnInfo(name = "key") val key: String,
             @ColumnInfo(name = "value") val value: String,
             @ColumnInfo(name = "reference") val reference: String,
             @ColumnInfo(name = "enable") val enable: Boolean)