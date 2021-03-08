package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.DriverDrivers

@Dao
interface PersonDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: MutableList<DriverDrivers>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: DriverDrivers): Long

    @Query("SELECT DISTINCT drivers.* from infringement_infringements infringements INNER JOIN driver_divers drivers on infringements.driver_id = drivers.id  WHERE infringements.id= :idInfraction")
    suspend fun selectPersonInfo(idInfraction: Long): DriverDrivers
}