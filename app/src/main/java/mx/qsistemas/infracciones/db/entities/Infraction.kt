package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infraction")
data class Infraction(@PrimaryKey @ColumnInfo(name = "ID_INFRACCION") val id: Int,
                      @ColumnInfo(name = "FOLIO") val folio:String,
                      @ColumnInfo(name = "NUM_PERMISO_LICENCIA") val no_permiso_licencia: String,
                      @ColumnInfo(name = "ID_TIPO_LICENCIA") val id_license_type: Int,
                      @ColumnInfo(name = "EXPEDIDA_EN_LICENCIA") val issued_in:String,
                      @ColumnInfo(name = "MOTIVO") val reason:String,
                      @ColumnInfo(name = "BAN_REMITIDO_DEPOSITO") val forwarded_deposit: Int,
                      @ColumnInfo(name = "DOCUMENTO_RETENIDO") val retained_document: String,
                      @ColumnInfo(name = "SALARIOS_MIN") val minimum_wage: Int,
                      @ColumnInfo(name = "IMPORTE") val amount: Float,
                      @ColumnInfo(name = "TAZA_SALARIO_MIN") val minimum_wage_rate: Float,
                      @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                      @ColumnInfo(name = "REGISTRO_FECHA") val registration_date: String,
                      @ColumnInfo(name = "BAN_PAGADA") val is_paid: Int,
                      @ColumnInfo(name = "ID_INFRACCION_FUENTE") val source_infra: Int,
                      @ColumnInfo(name = "ID_SECTOR") val sector: Int,
                      @ColumnInfo(name = "ID_OFICIAL") val id_officer: Int,
                      @ColumnInfo(name = "ID_STATUS") val id_status: Int,
                      @ColumnInfo(name = "NUM_HOJAS_EXPEDIENTE") val number_sheets_expedient: Int,
                      @ColumnInfo(name = "ID_AUTORIDAD_EXPIDE_PLACA") val emits_vehicular_plate: Int,
                      @ColumnInfo(name = "FOLIO_EVIDENCIA") val folio_evidence: String,
                      @ColumnInfo(name = "ID_DISPOSICION") val id_disposition: Int,
                      @ColumnInfo(name = "BAN_INFRACTOR_AUSENTE") val is_absent:Int,
                      @ColumnInfo(name = "PUNTOS_SANCION") val penalty_points: Int,
                      @ColumnInfo(name = "NUM_OFICIO_CONDONA") val official_number_condona:String,
                      @ColumnInfo(name = "FECHA_OFICIO_CONDONA") val date_office_condona:String,
                      @ColumnInfo(name = "LINEA_CAPTURA_I") val capture_line_i:String,
                      @ColumnInfo(name = "LINEA_CAPTURA_II") val capture_line_ii: String,
                      @ColumnInfo(name = "LINEA_CAPTURA_III") val capture_line_iii:String,
                      @ColumnInfo(name = "FEC_LINEA_CAPTURA_I") val date_capture_line_i: String,
                      @ColumnInfo(name = "FEC_LINEA_CAPTURA_II") val date_capture_line_ii: String,
                      @ColumnInfo(name = "FEC_LINEA_CAPTURA_II") val date_capture_line_iii:String,
                      @ColumnInfo(name = "IMPORTE_LINEA_CAPTURA_I") val amount_capture_line_i: Float,
                      @ColumnInfo(name = "IMPORTE_LINEA_CAPTURA_II") val amount_capture_line_ii: Float,
                      @ColumnInfo(name = "IMPORTE_LINEA_CAPTURA_III") val amount_capture_line_iii: Float,
                      @ColumnInfo(name = "ID_TIPO_SERVICIO") val type_service: Int,
                      @ColumnInfo(name = "LINEA_CAPTURA_OXXO_I") val capture_line_oxxo_i:String,
                      @ColumnInfo(name = "FEC_LINEA_CAPTURA_OXXO_I") val date_capture_linea_oxxo_i: String,
                      @ColumnInfo(name = "IMPORTE_LINEA_CAPTURA_OXXO_I") val amount_capture_line_oxxo: Float
)