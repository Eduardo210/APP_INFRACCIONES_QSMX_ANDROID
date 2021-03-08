package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.VehicleVehicles

@Dao
interface VehicleInfractionDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: MutableList<VehicleVehicles>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vehicleInfraction: VehicleVehicles): Long

    @Query("SELECT vehicle.* FROM infringement_infringements infra INNER JOIN vehicle_vehicles vehicle ON infra.vehicle_id = vehicle.id WHERE infra.id =:idInfraction")
    suspend fun selectVehicle(idInfraction: Long): VehicleVehicles
}