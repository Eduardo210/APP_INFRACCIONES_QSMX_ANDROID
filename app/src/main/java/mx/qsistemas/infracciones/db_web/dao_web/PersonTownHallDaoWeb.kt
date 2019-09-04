package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.PersonTownhall

@Dao
interface PersonTownHallDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(personTownHall: PersonTownhall): Long

    @Query("SELECT DISTINCT town.* from infringement_infringements infringements INNER JOIN person_townhall town on infringements.id = town.infringement_id  WHERE infringements.id= :idInfraction")
    fun selectTownPerson(idInfraction: Long): PersonTownhall
}