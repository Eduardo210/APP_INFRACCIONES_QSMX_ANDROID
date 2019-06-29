package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.TrafficViolationFraction

@Dao
interface TrafficViolationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trafficViolationFraction: TrafficViolationFraction)

    @Query("SELECT DISTINCT tvf.* FROM traffic_violation_fraction tvf INNER JOIN infraction i ON i.ID_INFRACCION = tvf.ID_INFRACCION AND tvf.ID_INFRACCION = :idInfraction")
    fun selectViolationsByInfraction(idInfraction: Long): MutableList<TrafficViolationFraction>
}