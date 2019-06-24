package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.RetainedDocument

@Dao
interface RetainedDocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<RetainedDocument>)

    @Query("SELECT * FROM retained_document ORDER BY DOCUMENTO ASC")
    fun selectAll(): MutableList<RetainedDocument>
}