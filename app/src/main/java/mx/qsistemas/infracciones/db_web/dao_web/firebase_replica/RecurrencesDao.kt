package mx.qsistemas.infracciones.db_web.dao_web.firebase_replica

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.Recurrences

interface RecurrencesDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(list: MutableList<Recurrences>)
//
//    @Query("SELECT DISTINCT * FROM recurrences where number_document_identify = :Recurrence ")
//    fun selectByRecurrences(Recurrence: String) : MutableList<Recurrences>
//
//    @Query("DELETE FROM recurrences")
//    fun deteteAll()
}