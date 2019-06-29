package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.VehicleInfraction

@Dao
interface VehicleInfractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<VehicleInfraction>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vehicleInfraction: VehicleInfraction): Long

    @Query("SELECT DISTINCT vi.* FROM vehicle_infraction vi INNER JOIN infraction i ON i.ID_INFRACCION = vi.ID_INFRACCION AND i.ID_INFRACCION = :idInfraction")
    fun selectVehicleOfInfraction(idInfraction: Long): VehicleInfraction
}