package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.VehicleType

@Dao
interface VehicleTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<VehicleType>)

    @Query("SELECT * FROM vehicle_type ORDER BY TIPO ASC")
    fun selectAll(): MutableList<VehicleType>
}