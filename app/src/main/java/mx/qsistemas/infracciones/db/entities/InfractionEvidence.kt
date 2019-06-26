package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infraction_evidence")
data class InfractionEvidence(@PrimaryKey(autoGenerate = true) val id: Int,
                              @ColumnInfo(name = "ID_INFRACCION") val idInfraction: Int,
                              @ColumnInfo(name = "EVIDENCIA_1") val evidence1: String,
                              @ColumnInfo(name = "EVIDENCIA_2") val evidence2: String)