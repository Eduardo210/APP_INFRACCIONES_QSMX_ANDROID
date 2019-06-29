package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.Person

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<Person>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(person: Person): Long

    @Query("SELECT DISTINCT p.* from person p INNER JOIN person_infringement pi ON pi.ID_PERSONA = p.id AND pi.ID_INFRACCION = :idInfraction")
    fun selectPersonInfo(idInfraction: Long): Person
}