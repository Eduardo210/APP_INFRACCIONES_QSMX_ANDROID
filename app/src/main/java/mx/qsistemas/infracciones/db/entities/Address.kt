package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "address")
data class Address(@PrimaryKey @ColumnInfo(name = "ID_DIRECCION") @NotNull val id: Int,
                   @ColumnInfo(name = "ID_PAIS") val id_country: Int,
                   @ColumnInfo(name = "ID_ESTADO") val id_state: Int,
                   @ColumnInfo(name = "ID_MUNICIPIO") val id_township: Int,
                   @ColumnInfo(name = "ID_COLONIA") val id_colony: Int,
                   @ColumnInfo(name = "ID_CALLE") val id_street: Int,
                   @ColumnInfo(name = "EXTERIOR") val outdoor_number: String,
                   @ColumnInfo(name = "INTERIOR") val inside_number: String,
                   @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                   @ColumnInfo(name = "ID_ENTRE_CALLE") val id_between_street: Int,
                   @ColumnInfo(name = "ID_Y_CALLE") val id_and_street: Int,
                   @ColumnInfo(name = "X") val latitude: Float?=null,
                   @ColumnInfo(name = "Y") val longitude: Float?=null)