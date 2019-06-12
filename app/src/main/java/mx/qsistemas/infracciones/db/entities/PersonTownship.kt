package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "person_towship")
data class PersonTownship(@PrimaryKey(autoGenerate = true) val id: Int,
                          @ColumnInfo(name = "ID_PERSONA") val id_person: Long,
                          @ColumnInfo(name = "IFE") val ife: String,
                          @ColumnInfo(name = "CARTILLA_SMN") val smn: String,
                          @ColumnInfo(name = "LICENCIA_CONDUCIR") val license: String,
                          @ColumnInfo(name = "CURP") val curp: String,
                          @ColumnInfo(name = "PASAPORTE") val passport: String,
                          @ColumnInfo(name = "ID_PAIS_NACIMIENTO") val id_country_brith: Int,
                          @ColumnInfo(name = "ID_ESTADO_NACIMIENTO") val id_state_birht: Int,
                          @ColumnInfo(name = "ID_MUNICIPIO_NACIMIENTO") val id_town_birth: Int,
                          @ColumnInfo(name = "ID_NACIONALIDAD") val id_nacionality: Int,
                          @ColumnInfo(name = "ID_ESTADO_CIVIL") val id_marital_status: Int,
                          @ColumnInfo(name = "ID_POSITION") val id_position: Int,
                          @ColumnInfo(name = "ID_TITULO") val id_title: Int,
                          @ColumnInfo(name = "ID_CATEGORIA_PERSONA") val id_category: Int,
                          @ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean,
                          @ColumnInfo(name = "EMPLEADO") val employee: String,
                          @ColumnInfo(name = "FECHA_ALTA") val discharge_date: Date,
                          @ColumnInfo(name = "ISSEMYM") val issemym: String,
                          @ColumnInfo(name = "BAN_LOC") val is_loc: Boolean,
                          @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                          @ColumnInfo(name = "EMAIL") val email: String,
                          @ColumnInfo(name = "NUM_CONVOCATORIA") val num_announcement: String,
                          @ColumnInfo(name = "FOLIO") val folio: String,
                          @ColumnInfo(name = "BAN_CERTIFICACION") val is_certification: Boolean,
                          @ColumnInfo(name = "ID_CATEGORIA_ELEMENTO") val id_element_category: Int,
                          @ColumnInfo(name = "MUNICIPIO_NACIMIENTO") val birth_town: String,
                          @ColumnInfo(name = "SUELDO") val salary: Float,
                          @ColumnInfo(name = "COMPENSACION") val compensation: Float,
                          @ColumnInfo(name = "BAN_ESTATAL") val ban_estatal: Boolean,
                          @ColumnInfo(name = "ID_ESTADO_ADSCRIPCION") val id_state_adscription: Int,
                          @ColumnInfo(name = "ID_MUNICIPIO_ADSCRIPCION") val id_town_adscription: Int
)