package mx.qsistemas.infracciones.db_web.dao_web.firebase_replica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes

@Dao
interface ZipCodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: MutableList<ZipCodes>)

    @Query("DELETE FROM zip_codes")
    fun deleteAll()
}