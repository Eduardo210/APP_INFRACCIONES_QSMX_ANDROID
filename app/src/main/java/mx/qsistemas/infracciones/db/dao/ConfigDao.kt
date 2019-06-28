package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.Config

@Dao
interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<Config>)

    @Query("SELECT * FROM config ORDER BY id ASC LIMIT 1")
    fun selectFirstConfig(): Config

    @Query("DELETE FROM config")
    fun deleteAll()
}