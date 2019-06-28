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
}