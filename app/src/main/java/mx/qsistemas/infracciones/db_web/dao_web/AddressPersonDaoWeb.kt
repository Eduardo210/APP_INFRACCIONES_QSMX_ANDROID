package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db_web.entities.DriverAddressDriver

@Dao
interface AddressPersonDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<DriverAddressDriver>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(address: DriverAddressDriver): Long

 /*   @Query("SELECT a.* FROM address a INNER JOIN address_infringement ai ON ai.ID_DIRECCION = a.ID_DIRECCION AND ai.ID_INFRACCION = :idInfraction INNER JOIN infraction i ON i.ID_INFRACCION = ai.ID_INFRACCION")
    fun selectInfractionAddres(idInfraction: Long): Address

    @Query("SELECT a.* FROM address a INNER JOIN person_infringement pi ON pi.ID_INFRACCION = :idInfraction INNER JOIN address_person ap ON ap.ID_PERSONA = pi.ID_PERSONA AND ap.ID_DIRECCION = a.ID_DIRECCION")
    fun selectPersonAddress(idInfraction: Long): Address*/
}