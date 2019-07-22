package mx.qsistemas.infracciones.db_web.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infringement_pictures_infringement")
data class InfringementPicturesInfringement(@PrimaryKey(autoGenerate = true) val id: Long,
                                            @ColumnInfo(name = "image") val image: String,
                                            @ColumnInfo(name = "token") val token: String,
                                            @ColumnInfo(name = "infringements_id") val infringements_id: Int
)