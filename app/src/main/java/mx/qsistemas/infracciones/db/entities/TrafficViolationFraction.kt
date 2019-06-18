package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "traffic_violation_fraction")
data class TrafficViolationFraction(@PrimaryKey(autoGenerate = true) val id: Int,
                                    @ColumnInfo(name = "ID_FRACCION") val id_fraction: Int,
                                    @ColumnInfo(name = "SALARIOS") val salary: Int,
                                    @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                                    @ColumnInfo(name = "PUNTOS_SANCION") val penalty_points: Int)