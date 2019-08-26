package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.InfringementPayorder
import mx.qsistemas.infracciones.db_web.entities.InfringementPayorderToSend

@Dao
interface InfractionPayorderDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(payorder: InfringementPayorder)

    @Query("SELECT DISTINCT inf.folio as folio_payment, pay.id, pay.amount, pay.surcharges, pay.discount, pay.rounding, pay.total, pay.payment_date, pay.concept, pay.observations, pay.payment_method, pay.authorize_no, inf.token_server AS infringement_id_server, pay.reference, pay.sync FROM infringement_payoder pay INNER JOIN infringement_infringements inf ON inf.id = pay.infringement_id WHERE pay.sync = 0")
    fun selectToSend(): MutableList<InfringementPayorderToSend>

    @Query("UPDATE infringement_payoder SET sync = 1 WHERE id = :idPayment")
    fun update(idPayment: Long)
}