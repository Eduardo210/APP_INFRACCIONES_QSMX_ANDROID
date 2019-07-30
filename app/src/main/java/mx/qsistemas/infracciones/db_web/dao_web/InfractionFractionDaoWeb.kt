package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db_web.entities.InfringementRelfractionInfringements

@Dao
interface InfractionFractionDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<InfringementRelfractionInfringements>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: InfringementRelfractionInfringements)

   /* @Query("SELECT DISTINCT infraction_fraction.* FROM infraction_fraction INNER JOIN articles ON articles.ID = ID_ARTICULO AND ID_ARTICULO = :idArticle ORDER BY FRACCION ASC")
    fun selectByArticle(idArticle: Int): MutableList<InfringementRelfractionInfringements>*/
}