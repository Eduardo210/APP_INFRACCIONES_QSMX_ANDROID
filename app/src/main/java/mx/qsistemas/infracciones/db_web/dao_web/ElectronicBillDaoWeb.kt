package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.ElectronicBill

@Dao
interface ElectronicBillDaoWeb {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: ElectronicBill)

    @Query("SELECT bill.* FROM infringement_infringements infra INNER JOIN electronic_bill bill ON infra.id = bill.infringements_id WHERE infra.id = :idInfringement")
    suspend fun selectElectronicBill(idInfringement: Long): ElectronicBill
}