package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db.entities.TrafficViolationFraction

@Dao
interface TrafficViolationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trafficViolationFraction: TrafficViolationFraction)
}