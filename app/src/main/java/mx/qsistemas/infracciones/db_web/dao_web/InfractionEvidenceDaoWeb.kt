package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementPicturesInfringement

@Dao
interface InfractionEvidenceDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(infractionEvidence: InfringementPicturesInfringement)

    @Query("SELECT DISTINCT * FROM infringement_pictures_infringement ")
    suspend fun selectPhotosToSend(): MutableList<InfringementPicturesInfringement>

    @Query("UPDATE infringement_pictures_infringement SET sync=1 WHERE infringements_id=:idInfraction")
    suspend fun updateSentPhotos(idInfraction: Long)

    @Query("DELETE FROM infringement_pictures_infringement WHERE infringements_id =:idInfraction")
    suspend fun deleteSentPhotos(idInfraction: Long)
}