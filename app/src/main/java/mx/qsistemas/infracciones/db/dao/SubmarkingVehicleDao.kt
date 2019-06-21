package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.SubmarkingVehicle

@Dao
interface SubmarkingVehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<SubmarkingVehicle>)

    @Query("SELECT * FROM submarking_vehicle WHERE ID_MARCA_VEHICULO = :idBrand ORDER BY SUBMARCA_VEHICULO ASC")
    fun selectSubBrandsByParent(idBrand: Int): MutableList<SubmarkingVehicle>
}