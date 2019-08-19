package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_vehicles")
data class VehicleVehicles(@PrimaryKey(autoGenerate = true) val id: Long,
                           @ColumnInfo(name = "year") val year: String,
                           //@ColumnInfo(name = "origin") val origin: String,
                           //@ColumnInfo(name = "circulation_card") val circulation_card: String,
                           @ColumnInfo(name = "colour_id") val colour_id: String,
                           @ColumnInfo(name = "is_new_color") val isNewColor: Boolean,
                          /* @ColumnInfo(name = "plate") val plate: String,
                           @ColumnInfo(name = "niv") val niv: String,*/
                           @ColumnInfo(name = "class_type_id") val class_type_id: String,
                           @ColumnInfo(name = "brand_reference") val brand_reference: String,
                           @ColumnInfo(name = "sub_brand_id") val sub_brand_id: String,
                           @ColumnInfo(name = "is_new_sub_brand") val isNewSubBrand: Boolean,
                           @ColumnInfo(name = "identifier_document_id") val identifier_document_id: String,
                           @ColumnInfo(name = "issued_in_id") val issued_in_id: String,
                           @ColumnInfo(name = "num_document") val num_document: String,
                           @ColumnInfo(name = "document_type") val document_type: String)