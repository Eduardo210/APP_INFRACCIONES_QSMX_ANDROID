package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementPicturesInfringement

@Dao
interface PictureInfractionDaoWeb {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(pictures: MutableList<InfringementPicturesInfringement>)

    @Query("SELECT pic.* FROM infringement_pictures_infringement pic INNER JOIN infringement_infringements i ON i.id = pic.infringements_id AND i.id = :idInfraction")
    fun selectPicturesToSend(idInfraction: Long): MutableList<InfringementPicturesInfringement>
}