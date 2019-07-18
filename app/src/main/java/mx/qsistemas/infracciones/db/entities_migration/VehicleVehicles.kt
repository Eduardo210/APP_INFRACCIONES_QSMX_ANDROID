package mx.qsistemas.infracciones.db.entities_migration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_vehicles")
data class VehicleVehicles(@PrimaryKey(autoGenerate = true) val id: Long,
                           @ColumnInfo(name = "year") val year: String,
                           @ColumnInfo(name = "origin") val origin: String,
                           @ColumnInfo(name = "colour_id") val colour_id: Long,
                           @ColumnInfo(name = "plate") val plate: String,
                           @ColumnInfo(name = "niv") val niv: String,
                           @ColumnInfo(name = "circulation_card") val circulation_card: String,
                           @ColumnInfo(name = "class_type_id") val class_type_id: Long,
                           @ColumnInfo(name = "sub_brand_id") val sub_brand_id: Long,
                           @ColumnInfo(name = "identifier_document_id") val identifier_document_id: Long,
                           @ColumnInfo(name = "issued_in_id") val issued_in_id: Long,
                           @ColumnInfo(name = "num_document") val num_document: String)