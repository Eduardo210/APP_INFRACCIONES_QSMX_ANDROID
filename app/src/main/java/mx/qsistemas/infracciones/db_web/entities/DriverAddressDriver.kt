package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_address_driver")
data class DriverAddressDriver(@PrimaryKey(autoGenerate = true) val id: Long,
                               @ColumnInfo(name = "street") val street: String,
                               @ColumnInfo(name = "exterior_num") val exterior_num: String,
                               @ColumnInfo(name = "internal_num") val internal_num: String,
                               @ColumnInfo(name = "city_id") val city_id:String,
                               @ColumnInfo(name = "city") val city:String,
                               @ColumnInfo(name = "colony_id") val colony_id:String,
                               @ColumnInfo(name = "colony") val colony:String,
                               @ColumnInfo(name = "cp_id") val cp_id: String,
                               @ColumnInfo(name = "cp") val cp: String,
                               @ColumnInfo(name = "driver_id") val driver_id:String,
                               @ColumnInfo(name = "state_id") val state_id: String,
                               @ColumnInfo(name = "state") val state: String
                               )