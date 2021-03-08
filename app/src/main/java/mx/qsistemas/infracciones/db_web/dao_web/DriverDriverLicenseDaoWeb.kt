package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.DriverDriverLicense

@Dao
interface DriverDriverLicenseDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(driverLicense: DriverDriverLicense)

    @Query("SELECT driver_lic.* FROM infringement_infringements infra LEFT JOIN driver_divers driver ON infra.driver_id = driver.id LEFT JOIN driver_driverlicense driver_lic ON driver.id = driver_lic.driver_id WHERE infra.id = :idInfraction")
    suspend fun selectDriverLicense(idInfraction: Long): DriverDriverLicense
}