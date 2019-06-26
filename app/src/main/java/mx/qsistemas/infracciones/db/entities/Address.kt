package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "address")
data class Address(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID_DIRECCION") @NotNull val id: Int,
                   @ColumnInfo(name = "ID_PAIS") val id_country: Int = 1,
                   @ColumnInfo(name = "ID_ESTADO") val id_state: Int,
                   @ColumnInfo(name = "ID_MUNICIPIO") val id_township: Int,
                   @ColumnInfo(name = "COLONIA") val colony: String,
                   @ColumnInfo(name = "CALLE") val street: String,
                   @ColumnInfo(name = "EXTERIOR") val outdoor_number: String,
                   @ColumnInfo(name = "INTERIOR") val inside_number: String,
                   @ColumnInfo(name = "ID_REGISTRO_USUARIO") val id_reg_user: Long,
                   @ColumnInfo(name = "ENTRE_CALLE") val between_street: String,
                   @ColumnInfo(name = "Y_CALLE") val and_street: String,
                   @ColumnInfo(name = "X") val latitude: Double? = null,
                   @ColumnInfo(name = "Y") val longitude: Double? = null)