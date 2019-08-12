package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "infringement_thirdimpound")
class InfringementThirdImpound(@PrimaryKey(autoGenerate = true) val id: Long,
                               @ColumnInfo(name="token") val token: String,
                               @ColumnInfo(name = "name") val name: String)