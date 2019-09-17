package mx.qsistemas.infracciones.db_web.dao_web.firebase_replica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.State

@Dao
interface StateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: MutableList<State>)

    @Query("DELETE FROM state")
    fun deleteAll()
}