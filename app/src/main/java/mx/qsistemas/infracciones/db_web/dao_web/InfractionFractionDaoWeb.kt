package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementRelfractionInfringements

@Dao
interface InfractionFractionDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: MutableList<InfringementRelfractionInfringements>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: InfringementRelfractionInfringements)

    @Query("SELECT * FROM infringement_relInfraction_infringements WHERE infringements_id = :idInfraction")
    suspend fun selectFractions(idInfraction: Long): MutableList<InfringementRelfractionInfringements>
}