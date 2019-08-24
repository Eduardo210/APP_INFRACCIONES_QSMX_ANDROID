package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementCapturelines

@Dao
interface CaptureLineDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(captureLine: InfringementCapturelines)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<InfringementCapturelines>)

    @Query("SELECT * FROM infringement_capturelines WHERE infringements_id =:idInfraction")
    fun selectCaptureLine(idInfraction: Long): MutableList<InfringementCapturelines>
}