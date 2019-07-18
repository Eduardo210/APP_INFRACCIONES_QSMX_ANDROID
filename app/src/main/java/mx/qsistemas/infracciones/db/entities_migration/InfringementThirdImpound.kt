package mx.qsistemas.infracciones.db.entities_migration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "infringement_thirdimpound")
class InfringementThirdImpound(@PrimaryKey(autoGenerate = true) val id: Long,
                               @ColumnInfo(name = "name") val name: String)