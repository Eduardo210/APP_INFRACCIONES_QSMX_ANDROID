package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.DriverDrivers

@Dao
interface PersonDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<DriverDrivers>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(person: DriverDrivers): Long

    @Query("SELECT DISTINCT p.* from person p INNER JOIN person_infringement pi ON pi.ID_PERSONA = p.id AND pi.ID_INFRACCION = :idInfraction")
    fun selectPersonInfo(idInfraction: Long): DriverDrivers
}