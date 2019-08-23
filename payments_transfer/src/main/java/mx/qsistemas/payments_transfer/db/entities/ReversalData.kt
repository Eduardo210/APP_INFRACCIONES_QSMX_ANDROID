package mx.qsistemas.payments_transfer.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "reversal_data")
data class ReversalData(@PrimaryKey(autoGenerate = true) @NotNull val id: Int,
                        @ColumnInfo(name = "type_tx") var typeTx: String,
                        @ColumnInfo(name = "entry_moe") var entryMode: String,
                        @ColumnInfo(name = "track_1") var track1: String,
                        @ColumnInfo(name = "track_2") var track2: String,
                        @ColumnInfo(name = "emv_tags") var emvTags: String,
                        @ColumnInfo(name = "masked_pan") var maskedPan: String,
                        @ColumnInfo(name = "cardholder") var nombreTarjetahabiente: String,
                        @ColumnInfo(name = "need_sign") var needSign: Boolean,
                        @ColumnInfo(name = "aid") var aid: String = "",
                        @ColumnInfo(name = "arqc") var arqc: String = "",
                        @ColumnInfo(name = "tvr") var tvr: String = "",
                        @ColumnInfo(name = "tsi") var tsi: String = "",
                        @ColumnInfo(name = "apn") var apn: String = "",
                        @ColumnInfo(name = "al") var al: String = "",
                        @ColumnInfo(name = "reference") var reference: String = "",
                        @ColumnInfo(name = "no_control") var noControl: String = "",
                        @ColumnInfo(name = "is_reversal_chip") var reversalChip: Boolean = false,
                        @ColumnInfo(name = "is_complete") val complete: Boolean)