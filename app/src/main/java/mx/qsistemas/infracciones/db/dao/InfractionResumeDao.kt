package mx.qsistemas.infracciones.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface InfractionResumeDao {
    /*@Query("SELECT \n" +
            "\t(MARCA.MARCA_VEHICULO +' '+ SUBMARCA.SUBMARCA_VEHICULO +' '+ VEHICULO.COLOR) AS TITLE_VEHICLE\n" +
            "\t,INFRA.FOLIO AS FOLIO\n" +
            "\t,(ARTICULO.ARTICULO+'-'+FRACCION.FRACCION+': '+ARTICULO.DESCRIPCION+' '+FRACCION.DESCRIPCION) AS DESCRIPTION\n" +
            "\t,VEHICULO.NUM_DOCUMENTO_IDENTIFICADOR AS DOC_IDENT\n" +
            "\t,INFRA.REGISTRO_FECHA AS DATE_INFRA\n" +
            "FROM infraction INFRA\n" +
            "INNER JOIN infra_infra_vehicle INFRA_VEH ON INFRA.ID_INFRACCION = INFRA_VEH.ID_INFRACCION\n" +
            "INNER JOIN infra_infra_vehicle VEHICULO ON INFRA_VEH.ID_VEHICULO = VEHICULO.ID_VEHICULO\n" +
            "INNER JOIN vehicle_brand MARCA ON VEHICULO.ID_MARCA =MARCA.ID_MARCA_VEHICULO\n" +
            "INNER JOIN submarking_vehicle SUBMARCA ON MARCA.ID_MARCA_VEHICULO = SUBMARCA.ID_MARCA_VEHICULO\n" +
            "INNER JOIN traffic_violation_fraction INFRA_FRA ON INFRA.ID_INFRACCION = INFRA_FRA.ID_INFRACCION\n" +
            "INNER JOIN infraction_fraction FRACCION ON INFRA_FRA.ID_FRACCION = FRACCION.ID\n" +
            "INNER JOIN articles ARTICULO ON FRACCION.ID_ARTICULO = ARTICULO.ID")
    fun getAllInfractions(): MutableList<ResumeInfraItem>*/
}