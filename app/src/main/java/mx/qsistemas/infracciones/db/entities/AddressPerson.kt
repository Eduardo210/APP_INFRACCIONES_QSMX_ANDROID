package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "address_person", primaryKeys = ["ID_DIRECCION", "ID_PERSONA"])
data class AddressPerson(@ColumnInfo(name = "ID_DIRECCION") val id_address: Int,
                         @ColumnInfo(name = "ID_PERSONA") val id_person: Long)