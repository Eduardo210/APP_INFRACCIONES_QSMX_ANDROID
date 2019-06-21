package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_infringement_card")
data class PaymentInfringementCard(@PrimaryKey(autoGenerate = true)  val id: Int,
                                   @ColumnInfo(name="AID") val aid: String,
                                   @ColumnInfo(name="APP_LABEL") val app_label: String,
                                   @ColumnInfo(name="ARQC") val arqc: String,
                                   @ColumnInfo(name="AUTH_NB") val auth_nb: String,
                                   @ColumnInfo(name="ENTRY_TYPE") val entry_type: String,
                                   @ColumnInfo(name="MASKED_PAN") val masked_pan: String,
                                   @ColumnInfo(name="TRX_DATE") val trx_date: String,
                                   @ColumnInfo(name="TRX_NB") val trx_nb: String,
                                   @ColumnInfo(name="TRX_TIME") val trx_time: String,
                                   @ColumnInfo(name="SERIAL_PAYPDA") val serial_paypda: String,
                                   @ColumnInfo(name="ID_REGISTRO_USUARIO") val id_reg_user: Long,
                                   @ColumnInfo(name="AFILIACION") val afiliacion: String,
                                   @ColumnInfo(name="VIGENCIA_TARJETA") val vigencia_tarjeta: String,
                                   @ColumnInfo(name="MENSAJE") val mensaje: String,
                                   @ColumnInfo(name="TIPO_TARJETA") val tipo_tarjeta: String,
                                   @ColumnInfo(name="TIPO") val tipo: String,
                                   @ColumnInfo(name="BANCO_EMISOR") val banco_emisor: String,
                                   @ColumnInfo(name="REFERENCIA") val referencia: String,
                                   @ColumnInfo(name="IMPORTE") val importe: String,
                                   @ColumnInfo(name="TVR") val tvr: String,
                                   @ColumnInfo(name="TSI") val tsi: String,
                                   @ColumnInfo(name="NUMERO_CONTROL") val numero_control: String,
                                   @ColumnInfo(name="TARJETAHABIENTE") val tarjetahabiente: String,
                                   @ColumnInfo(name="EMV_DATA") val emv_data: String,
                                   @ColumnInfo(name="TIPO_TRANSACCION") val tipo_transaccion: String)