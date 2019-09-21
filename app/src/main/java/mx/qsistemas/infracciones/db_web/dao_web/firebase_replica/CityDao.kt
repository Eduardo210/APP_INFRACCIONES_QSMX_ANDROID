package mx.qsistemas.infracciones.db_web.dao_web.firebase_replica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.City

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: MutableList<City>)

    @Query("SELECT DISTINCT * FROM city WHERE reference LIKE :reference ORDER BY value ASC")
    fun selectByStateReference(reference: String): MutableList<City>

    @Query("DELETE FROM city")
    fun deleteAll()
}