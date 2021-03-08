package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "infringement_payoder")
data class InfringementPayorder(
        @PrimaryKey(autoGenerate = true) @NotNull val id: Int,
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
        @ColumnInfo(name = "bank_reference") val bank_reference: String,
        @ColumnInfo(name = "sync") val sync: Boolean,
        @ColumnInfo(name = "token_server") val token_server: String,
        @ColumnInfo(name = "app_label") val app_label: String,
        @ColumnInfo(name = "membership") val membership: String,
        @ColumnInfo(name = "bank") val bank: String,
        @ColumnInfo(name = "entry_type") val entry_type: String,
        @ColumnInfo(name = "control_number") val control_number: String,
        @ColumnInfo(name = "mobile_series") val mobile_series: String,
        @ColumnInfo(name = "cardholder") val card_holder: String,
        @ColumnInfo(name = "type") val type: String,
        @ColumnInfo(name = "tx_nb") val tx_nb: String,
        @ColumnInfo(name = "tx_date") val tx_date: String,
        @ColumnInfo(name = "tx_time") val tx_time: String)
/*data class InfringementPayorder(@PrimaryKey(autoGenerate = true) @NotNull val id: Int,
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
                                @ColumnInfo(name = "sync") val sync: Boolean,
                                @ColumnInfo(name = "token_server") val token_server: String)*/

data class InfringementPayorderToSend(
        @ColumnInfo(name = "folio")
        val folio: String,
        @ColumnInfo(name = "id")
        val id: Int,
        @ColumnInfo(name = "appLabel")
        val appLabel: String,
        @ColumnInfo(name= "membership")
        val membership: String,
        @ColumnInfo(name ="bank")
        val bank: String,
        @ColumnInfo(name ="entryType")
        val entryType: String,
        @ColumnInfo(name = "amount")
        val amount: Float,
        @ColumnInfo(name = "authorize_no")
        val authorize_no: Long,
        @ColumnInfo(name="controlNumber")
        val controlNumber: String,
        @ColumnInfo(name = "bankReference")
        val bankReference: String,
        @ColumnInfo(name = "mobileSeries")
        val mobileSeries: String,
        @ColumnInfo(name = "cardHolder")
        val cardHolder: String,
        @ColumnInfo(name = "type")
        val type: String,
        @ColumnInfo(name = "tx_nb")
        val tx_nb: String,
        @ColumnInfo(name = "tx_date")
        val tx_date: String,
        @ColumnInfo(name = "tx_time")
        val tx_time: String,
        @ColumnInfo(name = "token_server")
        val token_server: String,
        @ColumnInfo(name ="sync")
        val sync: Boolean,
        @ColumnInfo(name = "id_infraction")
        val id_infraction: Long)