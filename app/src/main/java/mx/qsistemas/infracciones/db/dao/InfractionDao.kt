package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.Infraction

@Dao
interface InfractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<Infraction>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(infraction: Infraction): Long

    @Query("SELECT FOLIO FROM infraction WHERE FOLIO LIKE :prefix ORDER BY ID_INFRACCION DESC LIMIT 1")
    fun selectLastFolio(prefix: String): String

    @Query("SELECT DISTINCT FOLIO FROM infraction WHERE ID_INFRACCION = :idInfraction")
    fun selectFolioByIdInfraction(idInfraction: Long): String

    @Query("SELECT DISTINCT * FROM infraction WHERE ID_INFRACCION = :idInfraction")
    fun selectByIdInfraction(idInfraction: Long): Infraction

    @Query(" SELECT * FROM infraction WHERE SYNC = 0")
    fun selectInfractionsToSend(): MutableList<Infraction>

    @Query("UPDATE infraction SET BAN_PAGADA = 1 WHERE ID_INFRACCION = :idInfraction")
    fun updatePaidById(idInfraction: Long)

    @Query("UPDATE infraction SET SYNC = 1 WHERE FOLIO = :folio")
    fun updateSendByFolio(folio: String)
}