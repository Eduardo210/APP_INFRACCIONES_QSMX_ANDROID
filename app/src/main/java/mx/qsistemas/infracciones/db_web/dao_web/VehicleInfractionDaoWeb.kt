package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db_web.entities.VehicleVehicles

@Dao
interface VehicleInfractionDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<VehicleVehicles>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vehicleInfraction: VehicleVehicles): Long

   /* @Query("SELECT DISTINCT vi.* FROM vehicle_infraction vi INNER JOIN infraction i ON i.ID_INFRACCION = vi.ID_INFRACCION AND i.ID_INFRACCION = :idInfraction")
    fun selectVehicleOfInfraction(idInfraction: Long): VehicleInfraction*/
}