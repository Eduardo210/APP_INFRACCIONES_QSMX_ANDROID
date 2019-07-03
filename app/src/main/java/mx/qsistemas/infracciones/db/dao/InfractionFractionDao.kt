package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.InfractionFraction

@Dao
interface InfractionFractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<InfractionFraction>)

    @Query("SELECT DISTINCT infraction_fraction.* FROM infraction_fraction INNER JOIN articles ON articles.ID = ID_ARTICULO AND ID_ARTICULO = :idArticle ORDER BY FRACCION ASC")
    fun selectByArticle(idArticle: Int): MutableList<InfractionFraction>
}