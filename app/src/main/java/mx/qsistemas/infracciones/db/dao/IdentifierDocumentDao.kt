package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.IdentifierDocument

@Dao
interface IdentifierDocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<IdentifierDocument>)

    @Query("SELECT ID, DOCUMENTO FROM identifier_document")
    fun getAll() :MutableList<IdentifierDocument>

}