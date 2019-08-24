package mx.qsistemas.payments_transfer.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.payments_transfer.db.entities.ReversalData

@Dao
interface ReversalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveData(data: ReversalData)

    @Query("SELECT * FROM reversal_data WHERE is_complete = 0")
    fun selectNotComplete(): MutableList<ReversalData>

    @Query("UPDATE reversal_data SET is_complete = 1 WHERE no_control = :noControl")
    fun updateData(noControl: String)
}