package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementPicturesInfringement

@Dao
interface InfractionEvidenceDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(infractionEvidence: InfringementPicturesInfringement)

    @Query("SELECT DISTINCT * FROM infringement_pictures_infringement ")
    fun selectPhotosToSend(): MutableList<InfringementPicturesInfringement>
}