package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authority_issues")
data class AuthorityIssues(@PrimaryKey @ColumnInfo(name = "ID_AUTORIDAD") val id: Int,
                           @ColumnInfo(name="AUTORIDAD") val authority: String,
                           @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long)
                           //@ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean)