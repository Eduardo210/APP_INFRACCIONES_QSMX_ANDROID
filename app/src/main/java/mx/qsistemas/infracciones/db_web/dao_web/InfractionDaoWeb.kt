package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import mx.qsistemas.infracciones.db_web.entities.InfractionItem
import mx.qsistemas.infracciones.db_web.entities.InfringementInfringements

@Dao
interface InfractionDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<InfringementInfringements>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(infraction: InfringementInfringements): Long

    @Query(" SELECT * FROM infringement_infringements WHERE SYNC = 0")
    suspend fun selectInfractionsToSend(): MutableList<InfringementInfringements>

    @Query("SELECT * FROM infringement_infringements WHERE id = :idInfraction")
    fun selectInfraction(idInfraction: Long): InfringementInfringements

    @Query("SELECT folio FROM infringement_infringements WHERE folio LIKE :prefix ORDER BY id DESC LIMIT 1")
    fun selectLastFolio(prefix: String): String

    @RawQuery
    fun searchResumeInfraction(query: SupportSQLiteQuery): MutableList<InfractionItem>
    /*@Query("SELECT DISTINCT FOLIO FROM infraction WHERE ID_INFRACCION = :idInfraction")
    fun selectFolioByIdInfraction(idInfraction: Long): String

    @Query("SELECT DISTINCT * FROM infraction WHERE ID_INFRACCION = :idInfraction")
    fun selectByIdInfraction(idInfraction: Long): Infraction



    @Query("UPDATE infraction SET BAN_PAGADA = 1 WHERE ID_INFRACCION = :idInfraction")
    fun updatePaidById(idInfraction: Long)*/

    @Query("UPDATE infringement_infringements SET sync = 1, token_server = :tokenServer WHERE folio = :folio")
    fun updateSendByFolio(tokenServer: String, folio: String)

    /*@Query("SELECT infra.REGISTRO_FECHA, infra.FOLIO, infra.BAN_INFRACTOR_AUSENTE, per.NOMBRE, per.A_PATERNO, per.A_MATERNO, per.RFC, ad.EXTERIOR, ad.INTERIOR, ad.CALLE CALLE_PERSON,ad.COLONIA COL_PERSON, ve_infra.TARJETA_CIRCULACION, ve_infra.TIPO, infra.EXPEDIDA_EN_LICENCIA, brand.MARCA_VEHICULO, ve_infra.SUBMARCA, ve_infra.COLOR, ve_infra.MODELO, ident_doc.DOCUMENTO, auth_is.AUTORIDAD, ve_infra.DOCUMENTO_EXPEDIDO_EN, ad_infra.CALLE CALLE_INFRA, ad_infra.ENTRE_CALLE ENTRE_CALLE_INFRA, ad_infra.Y_CALLE Y_CALLE_INFRA, ad_infra.COLONIA COL_INFRA, infra.DOCUMENTO_RETENIDO, dis.DISPOSICION, dis.ID_DISPOSICION ,(p_oficial.NOMBRE ||' '|| p_oficial.A_PATERNO ||' '|| p_oficial.A_MATERNO) OFICIAL, p_town.EMPLEADO, infra.FEC_LINEA_CAPTURA_II, infra.LINEA_CAPTURA_II, infra.IMPORTE_LINEA_CAPTURA_II, infra.FEC_LINEA_CAPTURA_III, infra.LINEA_CAPTURA_III, infra.IMPORTE_LINEA_CAPTURA_III, st_infractor.ESTADO ESTADO_INFRACTOR, st_infraction.ESTADO ESTADO_INFRACCION, ve_infra.NUM_DOCUMENTO_IDENTIFICADOR, authority.AUTORIDAD, town_infra.MUNICIPIO TOWN_INFRA FROM infraction infra LEFT JOIN person_infringement per_in on infra.ID_INFRACCION = per_in.ID_INFRACCION LEFT JOIN person per on per_in.ID_PERSONA = per.ID LEFT JOIN address_person ad_person on per.ID = ad_person.ID_PERSONA LEFT JOIN address ad on ad_person.ID_DIRECCION = ad.ID_DIRECCION LEFT JOIN vehicle_infraction ve_infra on infra.ID_INFRACCION = ve_infra.ID_INFRACCION LEFT JOIN vehicle_brand brand on ve_infra.ID_MARCA = brand.ID_MARCA_VEHICULO LEFT JOIN identifier_document ident_doc on ve_infra.ID_DOCUMENTO_IDENTIFICADOR = ident_doc.ID LEFT JOIN authority_issues auth_is on infra.ID_AUTORIDAD_EXPIDE_PLACA = auth_is.ID_AUTORIDAD LEFT JOIN address_infringement add_infr on infra.ID_INFRACCION = add_infr.ID_INFRACCION LEFT JOIN address ad_infra on add_infr.ID_DIRECCION = ad_infra.ID_DIRECCION LEFT JOIN disposition dis on infra.ID_DISPOSICION = dis.ID_DISPOSICION LEFT JOIN person_towship p_town on infra.ID_OFICIAL = p_town.ID_PERSONA_AYUNTAMIENTO LEFT JOIN person p_oficial on p_town.ID_PERSONA = p_oficial.id LEFT JOIN state st_infractor ON ad.ID_ESTADO = st_infractor.ID_ESTADO LEFT JOIN state st_infraction ON ad_infra.ID_ESTADO = st_infraction.ID_ESTADO LEFT JOIN authority_issues authority ON infra.ID_AUTORIDAD_EXPIDE_PLACA = authority.ID_AUTORIDAD LEFT JOIN town_sepo_mex town_infra ON ad_infra.ID_MUNICIPIO = town_infra.ID_MUNICIPIO WHERE infra.ID_INFRACCION  =:idInfraction")
    fun getAllDataInraction(idInfraction: Long): InfractionLocal

    @Query("SELECT art.ARTICULO article, infra_fra.FRACCION fraction, infra_fra.SALARIOS_MINIMO umas, infra_fra.PUNTOS_SANCION points, viol_frac.MOTIVACION motivation FROM traffic_violation_fraction viol_frac INNER JOIN infraction_fraction infra_fra on viol_frac.ID_FRACCION = infra_fra.ID INNER JOIN articles art ON infra_fra.ID_ARTICULO = art.ID WHERE viol_frac .ID_INFRACCION = :idInfraction")
    fun getInfractionFraction(idInfraction: Long): MutableList<SingletonTicket.ArticleFraction>*/


}