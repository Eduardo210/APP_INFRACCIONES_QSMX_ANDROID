package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.PaymentInfringementCard

@Dao
interface PaymentInfringementCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(paymentInfringementCard: PaymentInfringementCard): Long

    @Query("SELECT pic.* FROM payment_infringement_card pic INNER JOIN payment_infringement pi ON pi.ID_INFRACCION = pic.ID_INFRACCION INNER JOIN infraction i ON i.ID_INFRACCION = pic.ID_INFRACCION AND i.ID_INFRACCION = :idInfraction")
    fun selectTransactionInfo(idInfraction: Long): PaymentInfringementCard
}