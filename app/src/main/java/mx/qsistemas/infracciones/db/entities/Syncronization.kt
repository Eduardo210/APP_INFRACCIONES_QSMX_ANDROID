package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "syncronization")
data class Syncronization(@PrimaryKey @ColumnInfo(name = "IPA_INFRACCION_CATALOGOS") val id:Int,
                          @ColumnInfo(name = "TEMPLATES_IDENT") val templates_ident:Int,
                          @ColumnInfo(name = "APLICACION_ACTIVA") val is_active: Int,
                          @ColumnInfo(name = "ULTIMA_SINCRONIZACION_VEHICULOS_ROBADOS") val last_sync_date: String)