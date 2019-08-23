package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "infringement_payoder")
data class InfringementPayorder(@PrimaryKey(autoGenerate = true) @NotNull val id: Int,
                                @ColumnInfo(name = "amount") val amount: Float,
                                @ColumnInfo(name = "surcharges") val surcharges: Float,
                                @ColumnInfo(name = "discount") val discount: Float,
                                @ColumnInfo(name = "rounding") val rounding: Float,
                                @ColumnInfo(name = "total") val total: Float,
                                @ColumnInfo(name = "payment_date") val payment_date: String,
                                @ColumnInfo(name = "concept") val concept: String,
                                @ColumnInfo(name = "observations") val observations: String,
                                @ColumnInfo(name = "payment_method") val payment_method: String,
                                @ColumnInfo(name = "authorize_no") val authorize_no: Long,
                                @ColumnInfo(name = "infringement_id") val infringement_id: Long,
                                @ColumnInfo(name = "reference") val reference: String,
                                @ColumnInfo(name = "sync") val sync: Boolean)