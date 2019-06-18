package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.PersonAttibute

@Dao
interface PersonAttributeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<PersonAttibute>)

    @Query("DELETE FROM person_attribute")
    fun deleteAll()
}