package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db.entities.PaymentInfringementCard

@Dao
interface PaymentInfringementCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(paymentInfringementCard: PaymentInfringementCard): Long
}