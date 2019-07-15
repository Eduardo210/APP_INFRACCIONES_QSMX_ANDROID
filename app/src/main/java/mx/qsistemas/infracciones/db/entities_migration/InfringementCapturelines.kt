package mx.qsistemas.infracciones.db.entities_migration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InfringementCapturelines")
data class InfringementCapturelines(@PrimaryKey(autoGenerate = true) val id: Long,
                                    @ColumnInfo(name = "key") val key: String,
                                    @ColumnInfo(name = "date") val date: String,
                                    @ColumnInfo(name = "amount") val amount: Float,
                                    @ColumnInfo(name = "type_line") val type_line: String,
                                    @ColumnInfo(name = "order") val order: String,
                                    @ColumnInfo(name = "infringements_id") val infringements_id: Long
)