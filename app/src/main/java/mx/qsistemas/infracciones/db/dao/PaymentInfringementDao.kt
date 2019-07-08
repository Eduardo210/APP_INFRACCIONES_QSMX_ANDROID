package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.PaymentInfringement

@Dao
interface PaymentInfringementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(paymentInfringement: PaymentInfringement): Long

    @Query("SELECT pi.* FROM payment_infringement pi INNER JOIN infraction i ON i.ID_INFRACCION = pi.ID_INFRACCION AND i.ID_INFRACCION = :idInfraction")
    fun selectPaymentOfInfraction(idInfraction: Long): PaymentInfringement

    @Query("SELECT pi.* FROM payment_infringement pi INNER JOIN infraction i ON i.ID_INFRACCION = pi.ID_INFRACCION AND i.SYNC = 1 WHERE pi.SYNC = 0")
    fun seletPaymentToSend(): MutableList<PaymentInfringement>

    @Query("UPDATE payment_infringement SET SYNC = 1 WHERE ID_INFRACCION = :idInfraction")
    fun updateToSend(idInfraction: Long)
}