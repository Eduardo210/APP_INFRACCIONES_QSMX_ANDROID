package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infringement_infringements")
data class InfringementInfringements(@PrimaryKey(autoGenerate = true) val id: Long,
                                     @ColumnInfo(name = "folio") val folio: String,
                                     /*@ColumnInfo(name = "is_impound") val is_impound: Boolean,*/
                                     /*@ColumnInfo(name = "type_Service") val type_service: String,*/
                                     @ColumnInfo(name = "uma_rate") val uma_rate: Int,
                                     @ColumnInfo(name = "is_paid") val is_paid: Boolean,
                                     @ColumnInfo(name = "status") val status: String ="active",
                                     @ColumnInfo(name = "is_absent") val is_absent: Boolean,
                                     @ColumnInfo(name = "insured_document_id") val insured_document_id: String,
                                     @ColumnInfo(name = "third_impound_id") val third_impound_id: String,
                                     @ColumnInfo(name = "town_hall_id") val town_hall_id: Long,
                                     @ColumnInfo(name = "vehicle_id") val vehicle_id: Long,
                                     @ColumnInfo(name = "date") val date: String,
                                     @ColumnInfo(name = "time") val time:String,
                                     @ColumnInfo(name = "num_document_condonation") val condonation: String,
                                     @ColumnInfo(name = "is_insured") val is_insured:Boolean,
                                     @ColumnInfo(name = "sync") val sync: Boolean,
                                     @ColumnInfo(name = "driver_license_id") val driver_license_id: String,
                                     @ColumnInfo(name = "amount") val amount: Float,
                                     @ColumnInfo(name = "driver_id") val driver_id: Long,
                                     @ColumnInfo(name = "total_uma") val total_uma: Float)

