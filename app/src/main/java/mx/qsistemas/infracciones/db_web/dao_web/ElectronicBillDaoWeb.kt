package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.ElectronicBill

@Dao
interface ElectronicBillDaoWeb {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bill: ElectronicBill)

    @Query("SELECT DISTINCT * FROM electronic_bill WHERE infringements_id = :idInfraction")
    fun select(idInfraction: Long): ElectronicBill
}