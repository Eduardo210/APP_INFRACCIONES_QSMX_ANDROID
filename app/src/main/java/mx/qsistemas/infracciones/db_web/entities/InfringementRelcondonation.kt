package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "InfringementRelcondonation")
data class InfringementRelcondonation(@PrimaryKey(autoGenerate = true) val id: Long,
                                      @ColumnInfo(name = "num_document") val num_document: String,
                                      @ColumnInfo(name = "date") val date: Date,
                                      @ColumnInfo(name = "condonation_id") val condonation_id: Long,
                                      @ColumnInfo(name = "infringement_id") val infringement_id: Long)