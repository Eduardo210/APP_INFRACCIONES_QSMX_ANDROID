package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db.entities.InfractionEvidence

@Dao
interface InfractionEvidenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(infractionEvidence: InfractionEvidence)

    @Query("SELECT DISTINCT ie.* FROM infraction_evidence ie INNER JOIN infraction i ON i.ID_INFRACCION = ie.ID_INFRACCION WHERE i.SYNC = 1 AND ie.SYNC = 0")
    fun selectPhotosToSend(): MutableList<InfractionEvidence>

    @Query("UPDATE infraction_evidence SET SYNC = 1 WHERE ID_INFRACCION = :idInfraction")
    fun updateSendByIdInfraction(idInfraction: Long)
}