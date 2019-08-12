package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db_web.entities.DriverDriverLicense

@Dao
interface DriverDriverLicenseDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(driverLicense: DriverDriverLicense)

}