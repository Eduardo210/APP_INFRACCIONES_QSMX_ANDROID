package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "infringement_relInfraction_infringements")
class InfringementRelfractionInfringements(@PrimaryKey(autoGenerate = true) val id:Long,
                                           @ColumnInfo(name= "UMA") val uma:Int,
                                          /* @ColumnInfo(name = "penalty_points") val penalty_points: Int,*/
                                           @ColumnInfo(name = "fraction_id") val fraction_id: String,
                                           @ColumnInfo(name = "infringements_id") val infringements_id: Long,
                                           @ColumnInfo(name = "reason") val reason: String,
                                           @ColumnInfo(name = "amount") val amount: Float)