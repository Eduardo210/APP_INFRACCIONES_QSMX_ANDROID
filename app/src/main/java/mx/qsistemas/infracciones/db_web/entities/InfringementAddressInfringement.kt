package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "infringement_address_infringement")
data class InfringementAddressInfringement(@PrimaryKey(autoGenerate = true) @NotNull val id: Long,
                                           @ColumnInfo(name = "street") val street: String,
                                           @ColumnInfo(name = "street_a") val street_a: String,
                                           @ColumnInfo(name = "street_b") val street_b:String,
                                           @ColumnInfo(name = "city_id") val city_id: String,
                                           @ColumnInfo(name = "colony_id") val colony_id:String,
                                           @ColumnInfo(name = "cp_id") val cp_id: String,
                                           @ColumnInfo(name = "state_id") val state_id: String)
                                           /*@ColumnInfo(name = "infringement_id") val infringement_id: Long)*/