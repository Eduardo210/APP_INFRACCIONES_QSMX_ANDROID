package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementAddressInfringement

@Dao
interface AddressInfringementDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(addressInfringement: InfringementAddressInfringement): Long

    @Query("SELECT * FROM infringement_address_infringement WHERE infringement_id =:idInfraction")
    fun selectInfractionAddres(idInfraction: Long): InfringementAddressInfringement

}