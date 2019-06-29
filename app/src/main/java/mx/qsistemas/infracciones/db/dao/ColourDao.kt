package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.Colour

@Dao
interface ColourDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<Colour>)

    @Query("SELECT * FROM colour WHERE ID_COLOR <> 0 ORDER BY COLOR ASC")
    fun selectAll(): MutableList<Colour>
}