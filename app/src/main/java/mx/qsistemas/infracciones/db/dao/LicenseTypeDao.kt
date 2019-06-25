package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.LicenseType

@Dao
interface LicenseTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<LicenseType>)

    @Query("SELECT * FROM license_type ORDER BY TIPO_LICENCIA ASC")
    fun selectAll(): MutableList<LicenseType>
}