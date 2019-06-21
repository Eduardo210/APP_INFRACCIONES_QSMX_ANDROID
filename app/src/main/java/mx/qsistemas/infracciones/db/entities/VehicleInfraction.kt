package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_infraction")
data class VehicleInfraction(@PrimaryKey @ColumnInfo(name = "ID_VEHICULO") val id:Int,
                             @ColumnInfo(name = "ID_MARCA") val id_brand: Int,
                             @ColumnInfo(name = "SUBMARCA") val sub_brand: String,
                             @ColumnInfo(name = "TIPO") val type: String,
                             @ColumnInfo(name = "COLOR") val colour: String,
                             @ColumnInfo(name = "MODELO") val model: String,
                             @ColumnInfo(name = "ID_DOCUMENTO_IDENTIFICADOR") val id_identifier_document: Int,
                             @ColumnInfo(name = "NUM_DOCUMENTO_IDENTIFICADOR") val no_ident_document: String,
                             @ColumnInfo(name = "DOCUMENTO_EXPEDIDO_EN") val document_issued_in: String,
                             @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                             @ColumnInfo(name = "BARCODE_PLACA") val barcode:String,
                             @ColumnInfo(name = "TARJETA_CIRCULACION") val circulation_card: String)