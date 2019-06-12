package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authority_issues")
data class AuthorityIssues(@PrimaryKey(autoGenerate = true) val id: Long,
                           @ColumnInfo(name="AUTORIDAD") val authority: String,
                           @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: String,
                           @ColumnInfo(name = "BAN_ACTIVO") val is_active: Boolean)