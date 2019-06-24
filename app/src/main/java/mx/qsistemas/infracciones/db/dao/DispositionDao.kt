package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.Disposition

@Dao
interface DispositionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<Disposition>)

    @Query("SELECT * FROM disposition ORDER BY DISPOSICION ASC")
    fun selectAll(): MutableList<Disposition>
}