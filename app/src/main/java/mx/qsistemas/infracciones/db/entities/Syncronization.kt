package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "syncronization")
data class Syncronization(@PrimaryKey(autoGenerate = true) val id:Int,
                          @ColumnInfo(name = "TEMPLATES_IDENT") val templates_ident:String,
                          @ColumnInfo(name = "APLICACION_ACTIVA") val is_active: Boolean,
                          @ColumnInfo(name = "ULTIMA_SINCRONIZACION_VEHICULOS_ROBADOS") val last_sync_date: Date)