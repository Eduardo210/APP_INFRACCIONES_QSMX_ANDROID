package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infringement_capturelines")
data class InfringementCapturelines(@PrimaryKey(autoGenerate = true) val id: Long,
                                    @ColumnInfo(name = "key") val key: String,
                                    @ColumnInfo(name = "date") val date: String,
                                    @ColumnInfo(name = "amount") val amount: Float,
                                    @ColumnInfo(name = "type_line") val type_line: String,
                                    @ColumnInfo(name = "order") val order: Int,
                                    @ColumnInfo(name = "infringements_id") val infringements_id: String
)