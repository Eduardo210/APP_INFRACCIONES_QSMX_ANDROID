package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "InfringementRelInfractionInfringements")
class InfringementRelfractionInfringements(@PrimaryKey(autoGenerate = true) val id:Long,
                                           @ColumnInfo(name= "UMA") val uma:String,
                                           @ColumnInfo(name = "penalty_points") val penalty_points: Int,
                                           @ColumnInfo(name = "fraction_id") val fraction_id: Int,
                                           @ColumnInfo(name = "infringements_id") val infringements_id: Int,
                                           @ColumnInfo(name = "reason") val reason: String,
                                           @ColumnInfo(name = "amount") val amount: Float)