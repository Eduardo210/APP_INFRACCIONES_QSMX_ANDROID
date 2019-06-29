package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.Articles

@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<Articles>)

    @Query("SELECT * FROM articles ORDER BY ARTICULO DESC")
    fun selectAll(): MutableList<Articles>
}