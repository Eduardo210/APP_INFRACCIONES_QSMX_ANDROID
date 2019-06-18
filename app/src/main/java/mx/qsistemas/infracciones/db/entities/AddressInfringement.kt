package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "address_infringement", primaryKeys = ["ID_DIRECCION","ID_INFRACCION"])
data class AddressInfringement(@ColumnInfo(name = "ID_DIRECCION") val id_address: Int,
                               @ColumnInfo(name = "ID_INFRACCION") val id_infrengement: Int)