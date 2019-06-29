package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mx.qsistemas.infracciones.db.entities.PaymentInfringement

@Dao
interface PaymentInfringementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(paymentInfringement: PaymentInfringement): Long
}