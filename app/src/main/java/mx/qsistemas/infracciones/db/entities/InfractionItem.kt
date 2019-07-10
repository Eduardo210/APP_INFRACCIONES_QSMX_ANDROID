package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo

data class InfractionItem(@ColumnInfo(name ="ID_INFRACCION") val id_infraction: Long,
                          @ColumnInfo(name="ID_DOCUMENTO_IDENTIFICADOR") val id_doc_ident:Int,
                          @ColumnInfo(name="TITLE_VEHICLE") val title_vehicle: String,
                          @ColumnInfo(name="FOLIO") val folio: String,
                          @ColumnInfo(name="MOTIVO") val motivation: String,
                          @ColumnInfo(name="NUM_DOCUMENTO_IDENTIFICADOR") val identifier_document:String,
                          @ColumnInfo(name="REGISTRO_FECHA") val date_infra:String,
                          @ColumnInfo(name="BAN_PAGADA") val it_is_paid: Int,
                          @ColumnInfo(name="SYNC") val send: Boolean)