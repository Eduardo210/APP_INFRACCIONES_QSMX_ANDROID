package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db.entities.VehicleInfraction

@Dao
interface VehicleInfractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<VehicleInfraction>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vehicleInfraction: VehicleInfraction): Long
}