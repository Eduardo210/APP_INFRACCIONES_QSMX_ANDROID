package mx.qsistemas.infracciones.db.entities_migration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*

@Entity(tableName = "infringement_infringements")
data class infringementInfringements(@PrimaryKey(autoGenerate = true) val id: Long,
                                     @ColumnInfo(name = "folio") val folio: String,
                                     @ColumnInfo(name = "is_impound") val is_impound: Boolean,
                                     @ColumnInfo(name = "type_Service") val type_service: String,
                                     @ColumnInfo(name = "uma_rate") val uma_rate: Int,
                                     @ColumnInfo(name = "is_paid") val is_rate: Boolean,
                                     @ColumnInfo(name = "status") val status: String,
                                     @ColumnInfo(name = "is_absent") val is_absent: Boolean,
                                     @ColumnInfo(name = "insured_document_id") val insured_document_id: Int,
                                     @ColumnInfo(name = "third_impound_id") val third_impound_id: Long,
                                     @ColumnInfo(name = "town_hall_id") val town_hall_id: Long,
                                     @ColumnInfo(name = "vehicle_id") val vehicle_id: Long,
                                     @ColumnInfo(name = "date") val date: Date,
                                     @ColumnInfo(name = "num_document_condonation") val time: Time,
                                     @ColumnInfo(name = "is_insured") val is_insured:Boolean,
                                     @ColumnInfo(name = "driver_license_id") val driver_license_id: Long,
                                     @ColumnInfo(name = "amount") val amount: Float,
                                     @ColumnInfo(name = "driver_id") val driver_id: Long,
                                     @ColumnInfo(name = "total_uma") val total_uma: Float,
                                     @ColumnInfo(name = "is_offline") val is_offline: Boolean)
