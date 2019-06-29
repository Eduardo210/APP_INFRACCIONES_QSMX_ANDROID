package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infraction")
data class Infraction(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID_INFRACCION") val id: Int,
                      @ColumnInfo(name = "FOLIO") val folio: String,
                      @ColumnInfo(name = "NUM_PERMISO_LICENCIA") val no_permiso_licencia: String, // Paso 3
                      @ColumnInfo(name = "ID_TIPO_LICENCIA") val id_license_type: Int, // Paso 3
                      @ColumnInfo(name = "EXPEDIDA_EN_LICENCIA") val issued_in: String, // Paso 1
                      @ColumnInfo(name = "MOTIVO") val reason: String,
                      @ColumnInfo(name = "BAN_REMITIDO_DEPOSITO") val forwarded_deposit: Int, // Paso 2
                      @ColumnInfo(name = "DOCUMENTO_RETENIDO") val retained_document: String,  // Paso 2
                      @ColumnInfo(name = "SALARIOS_MIN") val minimum_wage: Int, // Total de UMA's calculadas
                      @ColumnInfo(name = "IMPORTE") val amount: Float,  // UMA x Total calculadas
                      @ColumnInfo(name = "TAZA_SALARIO_MIN") val minimum_wage_rate: Float, // UMA's
                      @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long, // Id Persona Ayuntamiento
                      @ColumnInfo(name = "REGISTRO_FECHA") val registration_date: String, // Fecha/Hora infracción
                      @ColumnInfo(name = "BAN_PAGADA") val is_paid: Int, // 1 = Si 0 = No
                      @ColumnInfo(name = "ID_INFRACCION_FUENTE") val source_infra: Int = 1,
                      @ColumnInfo(name = "ID_SECTOR") val sector: Int = 4,
                      @ColumnInfo(name = "ID_OFICIAL") val id_officer: Int,  // Id Persona Ayuntamiento
                      @ColumnInfo(name = "ID_STATUS") val id_status: Int = 1,
                      @ColumnInfo(name = "NUM_HOJAS_EXPEDIENTE") val number_sheets_expedient: Int,
                      @ColumnInfo(name = "ID_AUTORIDAD_EXPIDE_PLACA") val emits_vehicular_plate: Int, // Tipo de documento
                      @ColumnInfo(name = "FOLIO_EVIDENCIA") val folio_evidence: String = "",
                      @ColumnInfo(name = "ID_DISPOSICION") val id_disposition: Int,
                      @ColumnInfo(name = "BAN_INFRACTOR_AUSENTE") val is_absent: Int,
                      @ColumnInfo(name = "PUNTOS_SANCION") val penalty_points: Int = 0,  // Suma de puntos x fracción
                      @ColumnInfo(name = "NUM_OFICIO_CONDONA") val official_number_condona: String = "",
                      @ColumnInfo(name = "FECHA_OFICIO_CONDONA") val date_office_condona: String = "",
                      @ColumnInfo(name = "LINEA_CAPTURA_I") val capture_line_i: String,
                      @ColumnInfo(name = "LINEA_CAPTURA_II") val capture_line_ii: String,
                      @ColumnInfo(name = "LINEA_CAPTURA_III") val capture_line_iii: String,
                      @ColumnInfo(name = "FEC_LINEA_CAPTURA_I") val date_capture_line_i: String,
                      @ColumnInfo(name = "FEC_LINEA_CAPTURA_II") val date_capture_line_ii: String,
                      @ColumnInfo(name = "FEC_LINEA_CAPTURA_III") val date_capture_line_iii: String,
                      @ColumnInfo(name = "IMPORTE_LINEA_CAPTURA_I") val amount_capture_line_i: Float,
                      @ColumnInfo(name = "IMPORTE_LINEA_CAPTURA_II") val amount_capture_line_ii: Float,
                      @ColumnInfo(name = "IMPORTE_LINEA_CAPTURA_III") val amount_capture_line_iii: Float,
                      @ColumnInfo(name = "ID_TIPO_SERVICIO") val type_service: Int,
                      @ColumnInfo(name = "LINEA_CAPTURA_OXXO_I") val capture_line_oxxo_i: String,
                      @ColumnInfo(name = "FEC_LINEA_CAPTURA_OXXO_I") val date_capture_linea_oxxo_i: String,
                      @ColumnInfo(name = "IMPORTE_LINEA_CAPTURA_OXXO_I") val amount_capture_line_oxxo: Float,
                      @ColumnInfo(name = "SYNC") val sync: Boolean // Flag envío a server
)