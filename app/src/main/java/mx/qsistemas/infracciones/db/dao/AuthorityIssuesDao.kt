package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.AuthorityIssues

@Dao
interface AuthorityIssuesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<AuthorityIssues>)

    @Query("SELECT * FROM authority_issues ORDER BY AUTORIDAD ASC")
    fun selectAll(): MutableList<AuthorityIssues>
}