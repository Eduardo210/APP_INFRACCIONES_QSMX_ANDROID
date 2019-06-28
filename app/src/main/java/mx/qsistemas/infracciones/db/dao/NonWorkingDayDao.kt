package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.NonWorkingDay

@Dao
interface NonWorkingDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<NonWorkingDay>)

    @Query("SELECT * FROM non_working_day WHERE date(FECHA) >= date('now','localtime')")
    fun selectFutureDays(): MutableList<NonWorkingDay>
}