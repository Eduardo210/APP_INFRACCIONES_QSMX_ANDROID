package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db_web.entities.PersonTownhall

@Dao
interface PersonTownHallDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(personTownHall: PersonTownhall): Long
}