package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "electronic_bill")
data class ElectronicBill(@PrimaryKey(autoGenerate = true) val id:Long,
                          @ColumnInfo(name = "name") val name: String,
                          @ColumnInfo(name= "paternal") val paternal: String,
                          @ColumnInfo(name="maternal") val maternal: String,
                          @ColumnInfo(name="business_name") val business_name: String,
                          @ColumnInfo(name="rfc") val rfc: String,
                          @ColumnInfo(name="email") val email: String)