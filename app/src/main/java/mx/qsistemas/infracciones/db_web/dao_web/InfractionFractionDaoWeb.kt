package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementRelfractionInfringements

@Dao
interface InfractionFractionDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<InfringementRelfractionInfringements>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: InfringementRelfractionInfringements)

    @Query("SELECT * FROM infringementRelInfractionInfringements WHERE infringements_id = :idArticle")//TODO: Preguntar a Maik por la entidad de Artículos
    fun selectByArticle(idArticle: Long): MutableList<InfringementRelfractionInfringements>
}