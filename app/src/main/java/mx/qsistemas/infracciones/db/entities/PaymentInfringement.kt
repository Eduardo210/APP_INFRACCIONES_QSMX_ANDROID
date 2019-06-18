package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_infringement")
data class PaymentInfringement(@PrimaryKey(autoGenerate = true) val id: Int,
                               @ColumnInfo(name = "ID_INFRACCION") val id_infringement: Int,
                               @ColumnInfo(name = "ID_FORMA_PAGO") val id_payment_method: Int,
                               @ColumnInfo(name = "SUBTOTAL") val subtotal: Float,
                               @ColumnInfo(name = "DESCUENTO") val discount: Float,
                               @ColumnInfo(name = "TOTAL") val total: Float,
                               @ColumnInfo(name = "FOLIO") val folio: String,
                               @ColumnInfo(name = "OBSERVACION") val observation: String,
                               @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                               @ColumnInfo(name = "RECARGOS") val surcharge: Float
                               )