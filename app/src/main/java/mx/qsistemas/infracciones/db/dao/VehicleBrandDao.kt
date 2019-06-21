package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.VehicleBrand

@Dao
interface VehicleBrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<VehicleBrand>)

    @Query("SELECT * FROM vehicle_brand ORDER BY MARCA_VEHICULO ASC")
    fun selectAll(): MutableList<VehicleBrand>
}