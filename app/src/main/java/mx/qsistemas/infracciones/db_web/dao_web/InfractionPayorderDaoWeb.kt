package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementPayorder
import mx.qsistemas.infracciones.db_web.entities.InfringementPayorderToSend

@Dao
interface InfractionPayorderDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payorder: InfringementPayorder)

     /*@Query("SELECT DISTINCT pay.infringement_id, " +
             "pay.id, " +
             "pay.app_label," +
             "pay.membership, " +
             "pay.bank, " +
             "pay.entry_type, " +
             "pay.amount, " +
             "pay.authorize_no, " +
             "pay.control_number," +
             "pay.bank_reference, " +
             "pay.mobile_series," +
             "pay.cardholder," +
             "pay.type," +
             "pay.tx_nb," +
             "pay.tx_date," +
             "pay.tx_time, " +
             "pay.sync " +
             "FROM infringement_payoder pay " +
             "WHERE pay.sync = 0")*/
    @Query("SELECT * FROM infringement_payoder WHERE sync = 0")
    suspend fun selectToSend(): MutableList<InfringementPayorder>
    /*@Query("SELECT * FROM infringement_payoder WHERE sync = 0")
    suspend fun selectToSend(): MutableList<InfringementPayorder>*/

    @Query("UPDATE infringement_payoder SET sync = 1 WHERE id = :idPayment")
    suspend fun update(idPayment: Long)

    @Query("SELECT payorder.* FROM infringement_infringements infra INNER JOIN infringement_payoder payorder ON infra.id = payorder.infringement_id WHERE infra.id = :idInfringement")
    suspend fun selectPayOrder(idInfringement: Long): InfringementPayorder
}