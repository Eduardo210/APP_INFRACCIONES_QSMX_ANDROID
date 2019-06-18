package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class Address(@PrimaryKey(autoGenerate = true) val id:Int,
                   @ColumnInfo(name = "ID_PAIS") val id_country: Int,
                   @ColumnInfo(name = "ID_ESTADO") val id_state: Int,
                   @ColumnInfo(name = "ID_MUNICIPIO") val id_township: Int,
                   @ColumnInfo(name = "ID_COLONIA") val id_colony: Int,
                   @ColumnInfo(name = "ID_CALLE") val street: String,
                   @ColumnInfo(name = "EXTERIOR") val outdoor_number: String,
                   @ColumnInfo(name = "INTERIOR") val interior_number: String,
                   @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                   @ColumnInfo(name = "REGISTRO_FECHA") val registration_date: String,
                   @ColumnInfo(name = "ID_ENTRE_CALLE") val id_between_street: Long,
                   @ColumnInfo(name = "ID_Y_CALLE") val id_and_street: Long,
                   @ColumnInfo(name = "X") val longitude: Long,
                   @ColumnInfo(name = "Y") val latitutde: Long,
                   @ColumnInfo(name = "ID_MUNICIPIO_SEPOMEX") val id_township_sepomex: Long)