package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infraction_fraction")
data class InfractionFraction(@PrimaryKey @ColumnInfo(name = "ID") val id: Long,
                              @ColumnInfo(name = "ID_ARTICULO") val id_articulo: Int,
                              @ColumnInfo(name = "FRACCION") val fraccion:String,
                              @ColumnInfo(name = "DESCRIPCION") val description: String,
                              @ColumnInfo(name = "ALIAS_TICKET") val alias_ticket: String,
                              @ColumnInfo(name = "SALARIOS_MINIMO") val minimum_wages: Int,
                              @ColumnInfo(name = "ID_REGISTRO_USUARIO") val reg_user: Long,
                              //@ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean,
                              @ColumnInfo(name = "PUNTOS_SANCION") val penalty_points: Int)