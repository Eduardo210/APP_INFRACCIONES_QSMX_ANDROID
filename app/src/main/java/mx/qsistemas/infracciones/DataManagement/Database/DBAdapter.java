package mx.qsistemas.infracciones.DataManagement.Database;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Models.ArtFraccion;
import mx.qsistemas.infracciones.DataManagement.Models.Catalogs;
import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;
import mx.qsistemas.infracciones.DataManagement.Models.Persona;
import mx.qsistemas.infracciones.DataManagement.Models.SearchTerms;
import mx.qsistemas.infracciones.DataManagement.Models.UserLogin;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;

/**
 * Developed by ingmtz on 10/17/16.
 */

class DBAdapter {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    DBAdapter(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    boolean getUserPermissions(String idPersonaAyuntamiento, Context context) {
        Cursor cursor = null;
        try {
            PreferenceHelper pHelper = new PreferenceHelper(context);
            pHelper.putCanConsult("0");
            pHelper.putCanInsert("0");
            pHelper.putCanEdit("0");
            pHelper.putCanPay("0");
            pHelper.putCanSync("0");
            pHelper.putHasAll("0");
            //cursor = database.rawQuery(Const.Database.SQLQueries.GET_ATRIBUTOS + idPersonaAyuntamiento +"';", null);
            cursor = database.query(Const.Database.Tables.TABLE_ATRIBS, new String[]{"SIIPTA_PERSONA_ATRIBUTO.ID_ATRIBUTO"}, "ID_PERSONA = ?", new String[]{idPersonaAyuntamiento}, null, null, null);
            if (cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++){
                    cursor.moveToPosition(i);
                    switch (cursor.getString(0)){
                        case "71":
                            pHelper.putCanConsult("1");
                            break;
                        case "72":
                            pHelper.putCanInsert("1");
                            break;
                        case "73":
                            pHelper.putCanEdit("1");
                            break;
                        case "75":
                            pHelper.putCanPay("1");
                            break;
                        case "91":
                            pHelper.putCanSync("1");
                            break;
                        case "0":
                            pHelper.putCanConsult("1");
                            pHelper.putCanInsert("1");
                            pHelper.putCanEdit("1");
                            pHelper.putCanPay("1");
                            pHelper.putCanSync("1");
                            pHelper.putHasAll("1");
                            break;
                    }
                }

            }
            cursor.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            if (cursor != null){
                cursor.close();
            }
        }

        return false;
    }

    ArrayList<ArrayList<Catalogs>> getCatalogs(Context context) {
        ArrayList<ArrayList<Catalogs>> catalogs = new ArrayList<>();
        Cursor cursor = null;
        PreferenceHelper pHelper = new PreferenceHelper(context);
        String[] allitems = new String[]{"*"};
        for (int i = 0; i < 16; i++){
            switch (i){
                case 0:
                    cursor = database.query(Const.Database.Tables.TABLE_MARCA, allitems, null, null, null, null, "MARCA_VEHICULO ASC");
                    break;
                case 1:
                    cursor = database.query(Const.Database.Tables.TABLE_SUBMARCA, allitems, null, null, null, null, "SUBMARCA_VEHICULO ASC");
                    break;
                case 2:
                    cursor = database.query(Const.Database.Tables.TABLE_TIPOVEHICULO, allitems, null, null, null, null, "TIPO ASC");
                    break;
                case 3:
                    cursor = database.query(Const.Database.Tables.TABLE_COLOR, allitems, null, null, null, null, "COLOR ASC");
                    break;
                case 4:
                    cursor = database.query(Const.Database.Tables.TABLE_DOCIDENT, allitems, null, null, null, null, "DOCUMENTO ASC");
                    break;
                case 5:
                    cursor = database.query(Const.Database.Tables.TABLE_ESTADO, new String[]{"SIIPTC_ESTADO.ID_ESTADO","SIIPTC_ESTADO.ESTADO"}, null, null, null, null, "SIIPTC_ESTADO.ESTADO  ASC");
                    break;
                case 6:
                    cursor = database.query(Const.Database.Tables.TABLE_AUTEXPIDE, allitems, null, null, null, null, "AUTORIDAD ASC");
                    break;
                case 7:
                    cursor = database.query(Const.Database.Tables.TABLE_ARTICULO, allitems, null, null, null, null, null);
                    break;
                case 8:
                    cursor = database.query(Const.Database.Tables.TABLE_FRACCION, new String[]{"SIIPTC_INFRACCION_FRACCION.ID_FRACCION", "SIIPTC_INFRACCION_FRACCION.ID_ARTICULO", "SIIPTC_INFRACCION_FRACCION.FRACCION", "SIIPTC_INFRACCION_FRACCION.DESCRIPCION", "SIIPTC_INFRACCION_FRACCION.SALARIOS_MINIMOS", "SIIPTC_INFRACCION_FRACCION.PUNTOS_SANCION"}, null, null, null, null, null);
                    break;
                case 9:
                    cursor = database.query(Const.Database.Tables.TABLE_DISPOSICION, allitems, null, null, null, null, "DISPOSICION ASC");
                    break;
                case 10:
                    cursor = database.query(Const.Database.Tables.TABLE_DOCRETENIDO, allitems, null, null, null, null, "DOCUMENTO ASC");
                    break;
                case 11:
                    cursor = database.query(Const.Database.Tables.TABLE_MUNICIPIO, allitems, null, null, null, null, "MUNICIPIO ASC");
                    break;
                case 12:
                    cursor = database.query(Const.Database.Tables.TABLE_TIPOLIC, allitems, null, null, null, null, "TIPO_LICENCIA ASC");
                    break;
                case 13:
                    cursor = database.query(Const.Database.Tables.TABLE_CONFIG, allitems, null, null, null, null, null);
                    break;
                case 14:
                    cursor = database.rawQuery(Const.Database.SQLQueries.Catalogs.GET_LAST_INFRA + pHelper.getPrefix() +"%';",null);
                    ArrayList<Catalogs> list = new ArrayList<>();
                    Catalogs item;
                    if (cursor != null && cursor.getCount() > 0){
                        cursor.moveToLast();
                        item = new Catalogs();
                        item.setId("");
                        item.setValue("");
                        item.setUnion("");
                        item.setDataone(cursor.getString(0));
                        item.setDatatwo("");
                        item.setDatathree("");
                    } else {
                        item = new Catalogs();
                        item.setId("");
                        item.setValue("");
                        item.setUnion("");
                        item.setDataone(pHelper.getPrefix() + "-0");
                        item.setDatatwo("");
                        item.setDatathree("");
                    }
                    list.add(item);
                    cursor.close();
                    catalogs.add(list);
                    break;
                case 15:
                    cursor = database.query(Const.Database.Tables.TABLE_DIANOHABIL, new String[]{"SIIPTC_DIA_NO_HABIL.FECHA"}, null, null, null, null, null);
                    ArrayList<Catalogs> list1 = new ArrayList<>();
                    Catalogs item1;
                    if (cursor != null && cursor.getCount() > 0){
                        cursor.moveToFirst();
                        for (int i1 = 0; i1 < cursor.getCount(); i1++){
                            cursor.moveToPosition(i1);
                            item1 = new Catalogs();
                            item1.setId("");
                            item1.setValue("");
                            item1.setUnion("");
                            item1.setDataone(cursor.getString(0));
                            item1.setDatatwo("");
                            item1.setDatathree("");
                            list1.add(item1);
                        }
                    }
                    cursor.close();
                    catalogs.add(list1);
                    break;
            }

            if (i != 14 && i != 15){
                ArrayList<Catalogs> list = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    Catalogs item;
                    for (int y = 0; y < cursor.getCount(); y++){
                        item = new Catalogs();
                        item.setId("");
                        item.setValue("");
                        item.setUnion("");
                        item.setDataone("");
                        item.setDatatwo("");
                        item.setDatathree("");
                        cursor.moveToPosition(y);
                        if (i == 0 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 9 || i == 10 || i == 12){
                            item.setId(cursor.getString(0));
                            if(cursor.getString(0).equals("0")){
                                item.setValue("Seleccione uno...");
                            } else {
                                item.setValue(cursor.getString(1));
                            }
                        }
                        if (i == 1 || i == 11){
                            item.setId(cursor.getString(0));
                            item.setUnion(cursor.getString(1));
                            if(cursor.getString(0).equals("0")){
                                item.setValue("Seleccione uno...");
                            } else {
                                item.setValue(cursor.getString(2));
                            }
                        }
                        if (i == 7){
                            item.setId(cursor.getString(0));
                            if(cursor.getString(0).equals("0")){
                                item.setValue("Seleccione uno...");
                            } else {
                                item.setValue(cursor.getString(1));
                            }
                            item.setDataone(cursor.getString(2));
                        }
                        if  (i == 8){
                            item.setId(cursor.getString(0));
                            item.setUnion(cursor.getString(1));
                            if(cursor.getString(0).equals("0")){
                                item.setValue("Seleccione uno...");
                            } else {
                                item.setValue(cursor.getString(2));
                            }
                            item.setDataone(cursor.getString(3));
                            item.setDatatwo(cursor.getString(4));
                            item.setDatathree(cursor.getString(5));
                        }

                        if (i == 13){
                            item.setDataone(cursor.getString(0));
                            item.setDatatwo(cursor.getString(1));
                            item.setDatathree(cursor.getString(2));
                        }


                        list.add(item);
                    }
                }
                assert cursor != null;
                cursor.close();
                catalogs.add(list);
            }

        }
        return catalogs;
    }

    GetSaveInfra savedata(GetSaveInfra saveData) {
        GetSaveInfra infraid = saveData;

        try {

            if (saveData.getIsUpdate() != 1) {
                StringBuilder insertinfra = new StringBuilder();
                if (infraid.isOnline()) {
                    insertinfra.append(Const.Database.SQLQueries.Insert.Save.INSERT_INFRA_2);
                } else {
                    insertinfra.append(Const.Database.SQLQueries.Insert.Save.INSERT_INFRA_1);
                }

                insertinfra.append("'" + infraid.getInfraFolio() + "', ");
                if (infraid.getIsAusente() == 0) {
                    insertinfra.append("'" + infraid.getTipolic_num() + "', ");
                    insertinfra.append("'" + infraid.getTipolic_tipo() + "', ");
                    insertinfra.append("'" + infraid.getTipolic_expedida() + "', ");
                } else {
                    insertinfra.append("'', ");
                    insertinfra.append("'0', ");
                    insertinfra.append("'0', ");
                }
                insertinfra.append("'" + infraid.getMotivacion() + "', ");
                insertinfra.append("'" + infraid.getIsRemision() + "', ");
                insertinfra.append("'" + infraid.getSalariosminimos() + "', ");
                insertinfra.append("'" + infraid.getImporte() + "', ");
                insertinfra.append("'" + infraid.getTaza_salario() + "', ");
                insertinfra.append("'', ");
                insertinfra.append("'" + infraid.getId_persona_ayun() + "', ");
                insertinfra.append("'" + infraid.getFechahora() + "', ");
                insertinfra.append("'" + infraid.getSe_retiene() + "', ");
                insertinfra.append("'" + infraid.getTipo_doc() + "', ");
                insertinfra.append("'" + infraid.getDisposicion() + "', ");
                insertinfra.append("'" + infraid.getIsAusente() + "', ");
                insertinfra.append("'" + infraid.getPuntossancion() + "', ");
                insertinfra.append("'" + infraid.getLinea_uno() + "', ");
                insertinfra.append("'" + infraid.getLinea_dos() + "', ");
                insertinfra.append("'" + infraid.getLinea_tres() + "', ");
                insertinfra.append("'" + infraid.getFecha_uno() + "', ");
                insertinfra.append("'" + infraid.getFecha_dos() + "', ");
                insertinfra.append("'" + infraid.getFecha_tres() + "', ");
                insertinfra.append("'" + infraid.getImporte_uno() + "', ");
                insertinfra.append("'" + infraid.getImporte_dos() + "', ");
                insertinfra.append("'" + infraid.getImporte_tres() + "', ");
                if (infraid.isOnline()){
                    insertinfra.append("'1', ");
                }
                insertinfra.append("'0');");

                database.execSQL(insertinfra.toString());



                Cursor cursor = database.rawQuery("SELECT SIIPTA_INFRACCION.ID_INFRACCION FROM SIIPTA_INFRACCION;", null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToLast();
                    infraid.setInfraid(cursor.getString(0));
                }
                assert cursor != null;
                cursor.close();

                cursor = database.rawQuery(Const.Database.SQLQueries.Search.SEARCH_OFICIAL + infraid.getId_persona_ayun() + "';",null);
                cursor.moveToFirst();
                infraid.setOficialcaptura(cursor.getString(0));
                infraid.setOficialnum(cursor.getString(1));
                cursor.close();

                StringBuilder auto = new StringBuilder();
                auto.append("SELECT SIIPTA_INFRACCION_VEHICULO.ID_VEHICULO FROM SIIPTA_INFRACCION_VEHICULO WHERE ID_DOCUMENTO_IDENTIFICADOR = '");
                auto.append(infraid.getIdentificacion());
                auto.append("' AND NUM_DOCUMENTO_IDENTIFICADOR = '");
                auto.append(infraid.getIdentificacion_num());
                auto.append("';");

                int autoid = 0;
                cursor = database.rawQuery(auto.toString(), null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    autoid = cursor.getInt(0);
                }
                assert cursor != null;
                cursor.close();

                if (autoid == 0) {
                    if (infraid.getSubmarca() == 0) {
                        database.execSQL("INSERT INTO SIIPTC_SUBMARCA_VEHICULO (ID_MARCA_VEHICULO, SUBMARCA_VEHICULO) VALUES ('" + infraid.getMarca() + "', '" + infraid.getSubmarca_otra() + "');");
                        cursor = database.rawQuery("SELECT ID_SUBMARCA_VEHICULO, SUBMARCA_VEHICULO FROM SIIPTC_SUBMARCA_VEHICULO;", null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToLast();
                            infraid.setSubmarca(cursor.getInt(0));
                            infraid.setSubmarcatext(cursor.getString(1));
                        }
                        assert cursor != null;
                        cursor.close();
                    }

                    StringBuilder autoinsert = new StringBuilder();
                    autoinsert.append("INSERT INTO SIIPTA_INFRACCION_VEHICULO (ID_SUBMARCA_VEHICULO, ID_TIPO, ID_COLOR, MODELO, ID_DOCUMENTO_IDENTIFICADOR, NUM_DOCUMENTO_IDENTIFICADOR, DOCUMENTO_EXPEDIDO_EN, TARJETA_CIRCULACION) VALUES (");
                    autoinsert.append("'" + infraid.getSubmarca() + "', ");
                    autoinsert.append("'" + infraid.getTipo() + "', ");
                    autoinsert.append("'" + infraid.getColor() + "', ");
                    autoinsert.append("'" + infraid.getAno() + "', ");
                    autoinsert.append("'" + infraid.getIdentificacion() + "', ");
                    autoinsert.append("'" + infraid.getIdentificacion_num() + "', ");
                    autoinsert.append("'" + infraid.getExpedido_entext() + "', ");
                    autoinsert.append("'" + infraid.getTarjeta_circ() + "');");

                    database.execSQL(autoinsert.toString());

                    cursor = database.rawQuery("SELECT SIIPTA_INFRACCION_VEHICULO.ID_VEHICULO FROM SIIPTA_INFRACCION_VEHICULO;", null);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToLast();
                        autoid = cursor.getInt(0);
                    }
                    assert cursor != null;
                    cursor.close();

                }

                if (infraid.getIsAusente() == 0){
                    StringBuilder infractor = new StringBuilder();
                    infractor.append("SELECT SIIPTA_DIRECCION.ID_DIRECCION, SIIPTC_PERSONA.ID_PERSONA FROM SIIPTC_PERSONA INNER JOIN SIIPTA_DIRECCION_PERSONA_INFRACTOR ON SIIPTC_PERSONA.ID_PERSONA = SIIPTA_DIRECCION_PERSONA_INFRACTOR.ID_PERSONA INNER JOIN SIIPTA_DIRECCION ON SIIPTA_DIRECCION_PERSONA_INFRACTOR.ID_DIRECCION = SIIPTA_DIRECCION.ID_DIRECCION WHERE A_PATERNO LIKE '");
                    infractor.append(infraid.getInfractor_paterno());
                    infractor.append("' AND RFC = '");
                    infractor.append(infraid.getInfractor_rfc());
                    infractor.append("';");

                    int infractorid = 0;
                    int dirinfractorid = 0;
                    cursor = database.rawQuery(infractor.toString(), null);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        dirinfractorid = cursor.getInt(0);
                        infractorid = cursor.getInt(1);
                    }
                    assert cursor != null;
                    cursor.close();

                    if (infractorid == 0) {
                        StringBuilder infractorinsert = new StringBuilder();
                        infractorinsert.append("INSERT INTO SIIPTC_PERSONA (NOMBRE, A_PATERNO, A_MATERNO, RFC) VALUES (");
                        if (infraid.getInfractor_nombre() == null){
                            infractorinsert.append("'', ");
                        } else {
                            infractorinsert.append("'" + infraid.getInfractor_nombre() + "', ");
                        }
                        if (infraid.getInfractor_paterno() == null){
                            infractorinsert.append("'', ");
                        } else {
                            infractorinsert.append("'" + infraid.getInfractor_paterno() + "', ");
                        }
                        if (infraid.getInfractor_materno() == null){
                            infractorinsert.append("'', ");
                        } else {
                            infractorinsert.append("'" + infraid.getInfractor_materno() + "', ");
                        }
                        if (infraid.getInfractor_rfc() == null){
                            infractorinsert.append("'');");
                        } else {
                            infractorinsert.append("'" + infraid.getInfractor_rfc() + "');");
                        }

                        database.execSQL(infractorinsert.toString());

                        cursor = database.rawQuery("SELECT SIIPTC_PERSONA.ID_PERSONA FROM SIIPTC_PERSONA;", null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToLast();
                            infractorid = cursor.getInt(0);
                        }
                        assert cursor != null;
                        cursor.close();

                        StringBuilder dirinfractorinsert = new StringBuilder();
                        dirinfractorinsert.append("INSERT INTO SIIPTA_DIRECCION (ID_PAIS, ID_ESTADO, COLONIA, CALLE, EXTERIOR, INTERIOR, ID_MUNICIPIO) VALUES (");
                        dirinfractorinsert.append("'1', ");
                        dirinfractorinsert.append("'" + infraid.getInfractor_entidad() + "', ");
                        dirinfractorinsert.append("'" + infraid.getInfractor_colonia() + "', ");
                        dirinfractorinsert.append("'" + infraid.getInfractor_calle() + "', ");
                        dirinfractorinsert.append("'" + infraid.getInfractor_exterior() + "', ");
                        dirinfractorinsert.append("'" + infraid.getInfractor_interior() + "', ");
                        dirinfractorinsert.append("'" + infraid.getInfractor_delmun() + "');");

                        database.execSQL(dirinfractorinsert.toString());

                        cursor = database.rawQuery("SELECT SIIPTA_DIRECCION.ID_DIRECCION FROM SIIPTA_DIRECCION;", null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToLast();
                            dirinfractorid = cursor.getInt(0);
                        }
                        assert cursor != null;
                        cursor.close();

                    }

                    database.execSQL("INSERT INTO SIIPTA_DIRECCION_PERSONA_INFRACTOR VALUES ('" + dirinfractorid + "', '" + infractorid + "');");
                    database.execSQL("INSERT INTO SIIPTA_INFRACCION_PERSONA_INFRACTOR VALUES ('" + infraid.getInfraid() + "', '" + infractorid + "');");
                }


                ArrayList<ArtFraccion> fracciones = infraid.getArticulos();
                ArtFraccion artfraccion;

                for (int i = 0; i < fracciones.size(); i++) {
                    artfraccion = fracciones.get(i);
                    database.execSQL("INSERT INTO SIIPTA_INFRACCION_FRACCION VALUES ('" + infraid.getInfraid() + "', '" + artfraccion.getId() + "');");
                }

                StringBuilder dirinfrainsert = new StringBuilder();
                dirinfrainsert.append("INSERT INTO SIIPTA_DIRECCION (COLONIA, CALLE, ENTRE_CALLE, Y_CALLE, X, Y) VALUES (");
                dirinfrainsert.append("'" + infraid.getInfradir_colonia() + "', ");
                dirinfrainsert.append("'" + infraid.getInfradir_calle() + "', ");
                dirinfrainsert.append("'" + infraid.getInfradir_entre() + "', ");
                dirinfrainsert.append("'" + infraid.getInfradir_y() + "', ");
                dirinfrainsert.append("'" + infraid.getLatitude() + "', ");
                dirinfrainsert.append("'" + infraid.getLongitude() + "');");

                database.execSQL(dirinfrainsert.toString());

                int dirinfraccion = 0;

                cursor = database.rawQuery("SELECT SIIPTA_DIRECCION.ID_DIRECCION FROM SIIPTA_DIRECCION;", null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToLast();
                    dirinfraccion = cursor.getInt(0);
                }
                assert cursor != null;
                cursor.close();

                database.execSQL("INSERT INTO SIIPTA_DIRECCION_INFRACCION VALUES ('" + dirinfraccion + "', '" + infraid.getInfraid() + "');");
                database.execSQL("INSERT INTO SIIPTA_INFRACCION_INFRACCION_VEHICULO VALUES ('" + infraid.getInfraid() + "', '" + autoid + "');");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return infraid;
    }

    ArrayList<GetSaveInfra> getInfracciones(SearchTerms searchterms, boolean isSync) {

        try {

            boolean hasWhere = false;

            ArrayList<GetSaveInfra> list = new ArrayList<>();
            GetSaveInfra response;

            StringBuilder query = new StringBuilder();
            query.append(Const.Database.SQLQueries.Search.SEARCH_INFRA);

            if (searchterms.getFolio() != null) {
                hasWhere = true;
                query.append("WHERE SIIPTA_INFRACCION.FOLIO = '");
                query.append(searchterms.getFolio());
                query.append("'");
            }
            if (searchterms.getIdentifica() != null){
                if (!searchterms.getIdentifica().equals("0")) {
                    if (!hasWhere) {
                        query.append("WHERE ");
                    } else {
                        query.append("AND ");
                    }
                    query.append("SIIPTA_INFRACCION_VEHICULO.ID_DOCUMENTO_IDENTIFICADOR = '");
                    query.append(searchterms.getIdentifica());
                    query.append("' AND SIIPTA_INFRACCION_VEHICULO.NUM_DOCUMENTO_IDENTIFICADOR LIKE '%");
                    query.append(searchterms.getNumidentifica());
                    query.append("%'");
                }
            }

            if (isSync){
                query.append("WHERE SIIPTA_INFRACCION.IS_SYNC = '0'");
            } else {
                query.append(" AND SIIPTA_INFRACCION.IS_SYNC = '0'");
            }

            query.append(";");

            Cursor cursor = database.rawQuery(query.toString(), null);
            Cursor cursor1;
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    response = new GetSaveInfra();
                    response = responseFillclean(response);
                    response.setInfraid(cursor.getString(0));
                    response.setInfraFolio(cursor.getString(1));
                    response.setInfradir_calle(cursor.getString(2));
                    response.setInfradir_entre(cursor.getString(3));
                    response.setInfradir_y(cursor.getString(4));
                    response.setInfradir_colonia(cursor.getString(5));
                    response.setAno(cursor.getString(6));
                    response.setIdentificacion_num(cursor.getString(7));
                    response.setTarjeta_circ(cursor.getString(8));
                    response.setMotivacion(cursor.getString(9));
                    response.setSalariosminimos(cursor.getString(10));
                    response.setImporte(cursor.getString(11));
                    response.setPuntossancion(cursor.getString(12));
                    response.setLinea_uno(cursor.getString(13));
                    response.setLinea_dos(cursor.getString(14));
                    response.setLinea_tres(cursor.getString(15));
                    response.setFecha_uno(cursor.getString(16));
                    response.setFecha_dos(cursor.getString(17));
                    response.setFecha_tres(cursor.getString(18));
                    response.setImporte_uno(cursor.getString(19));
                    response.setImporte_dos(cursor.getString(20));
                    response.setImporte_tres(cursor.getString(21));
                    response.setId_persona_ayun(cursor.getString(22));
                    response.setFechahora(cursor.getString(23));
                    response.setMarca(cursor.getInt(24));
                    response.setMarcatext(cursor.getString(25));
                    response.setSubmarca(cursor.getInt(26));
                    response.setSubmarcatext(cursor.getString(27));
                    response.setTipo(cursor.getInt(28));
                    response.setTipotext(cursor.getString(29));
                    response.setColor(cursor.getInt(30));
                    response.setColortext(cursor.getString(31));
                    response.setIdentificacion(cursor.getInt(32));
                    response.setIdentificaciontext(cursor.getString(33));
                    response.setTipo_doc(cursor.getInt(34));
                    response.setTipo_doctext(cursor.getString(35));
                    response.setExpedido_en(cursor.getInt(36));
                    response.setExpedido_entext(cursor.getString(37));
                    response.setTaza_salario(cursor.getString(38));
                    response.setIsRemision(cursor.getInt(39));
                    response.setIsAusente(cursor.getInt(40));
                    response.setIdVehiculo(cursor.getInt(41));
                    response.setSe_retiene(cursor.getInt(42));
                    response.setSe_retienetext(cursor.getString(43));
                    if (isSync){
                        response.setIsSync(cursor.getInt(44));
                        response.setUpdated(cursor.getInt(45));
                    }
                    response.setLatitude(cursor.getDouble(46));
                    response.setLongitude(cursor.getDouble(47));

                    cursor1 = database.rawQuery(Const.Database.SQLQueries.Search.SEARCH_OFICIAL + response.getId_persona_ayun() + "';",null);
                    cursor1.moveToFirst();
                    response.setOficialcaptura(cursor1.getString(0));
                    response.setOficialnum(cursor1.getString(1));
                    cursor1.close();

                    ArrayList<ArtFraccion> artlist = new ArrayList<>();
                    ArtFraccion artFraccion;
                    cursor1 = database.rawQuery(Const.Database.SQLQueries.Search.SEARCH_FRACCION + response.getInfraid() + "';", null);

                    if (cursor1 != null && cursor1.getCount() > 0) {
                        cursor1.moveToFirst();
                        for (int c = 0; c < cursor1.getCount(); c++) {
                            cursor1.moveToPosition(c);
                            artFraccion = new ArtFraccion();
                            artFraccion.setArticulo(cursor1.getString(0));
                            artFraccion.setFraccion(cursor1.getString(1));
                            artFraccion.setDescripcion(cursor1.getString(2));
                            artFraccion.setSalarios(cursor1.getString(3));
                            artFraccion.setPuntos(cursor1.getString(4));
                            artFraccion.setId(cursor1.getString(5));
                            artFraccion.setIdarticulo(cursor1.getString(6));
                            artFraccion.setIsSearch(1);
                            artlist.add(artFraccion);
                        }
                        response.setArticulos(artlist);
                    }

                    cursor1.close();

                    if (response.getIsAusente() == 0) {

                        cursor1 = database.rawQuery(Const.Database.SQLQueries.Search.SEARCH_INFRACTOR + response.getInfraid() + "';", null);

                        if (cursor1 != null && cursor.getCount() > 0) {
                            cursor1.moveToFirst();
                            response.setInfractor_nombre(cursor1.getString(0));
                            response.setInfractor_paterno(cursor1.getString(1));
                            response.setInfractor_materno(cursor1.getString(2));
                            response.setInfractor_rfc(cursor1.getString(3));

                            if (cursor1.getString(4).equals("") && cursor1.getString(5).equals("") && cursor1.getString(6).equals("") && cursor1.getString(7).equals("")){
                                response.setInfractor_calle("");
                                response.setInfractor_exterior("");
                                response.setInfractor_interior("");
                                response.setInfractor_colonia("");
                                response.setIdDBDirPersona(false);
                            } else {
                                response.setInfractor_calle(cursor1.getString(4));
                                response.setInfractor_exterior(cursor1.getString(5));
                                response.setInfractor_interior(cursor1.getString(6));
                                response.setInfractor_colonia(cursor1.getString(7));
                                response.setIdDBDirPersona(true);
                            }

                            response.setInfractor_entidad(cursor1.getInt(8));
                            response.setInfractor_entidadtext(cursor1.getString(9));
                            response.setInfractor_delmun(cursor1.getInt(10));
                            response.setInfractor_delmuntext(cursor1.getString(11));
                            response.setIdDbDir(cursor1.getInt(12));
                            response.setIdDBPersona(cursor1.getInt(13));
                            response.setTipolic_num(cursor1.getString(14));
                        } else {
                            response.setInfractor_nombre("Q");
                            response.setInfractor_paterno("R");
                            response.setInfractor_materno("R");
                        }

                        cursor1.close();

                        if (!response.getTipolic_num().equals("")){
                            cursor1 = database.rawQuery(Const.Database.SQLQueries.Search.SEARCH_INFRACTORLIC + response.getInfraid() + "';", null);
                            cursor1.moveToFirst();
                            response.setTipolic_tipo(cursor1.getInt(0));
                            response.setTipolic_tipotext(cursor1.getString(1));
                            response.setTipolic_expedida(cursor1.getInt(2));
                            response.setTipolic_expedidatext(cursor1.getString(3));
                            cursor1.close();
                        }
                    }

                    if (response.getIsAusente() == 2){
                        cursor1 = database.rawQuery("SELECT SIIPTC_PERSONA.ID_PERSONA, SIIPTC_PERSONA.NOMBRE, SIIPTC_PERSONA.A_PATERNO, SIIPTC_PERSONA.A_MATERNO FROM SIIPTA_INFRACCION INNER JOIN SIIPTA_INFRACCION_PERSONA_INFRACTOR ON SIIPTA_INFRACCION.ID_INFRACCION = SIIPTA_INFRACCION_PERSONA_INFRACTOR.ID_INFRACCION INNER JOIN SIIPTC_PERSONA ON SIIPTA_INFRACCION_PERSONA_INFRACTOR.ID_PERSONA = SIIPTC_PERSONA.ID_PERSONA WHERE SIIPTA_INFRACCION.ID_INFRACCION = '"+ response.getInfraid() + "';", null);
                        if (cursor1 != null && cursor.getCount() > 0) {
                            cursor1.moveToFirst();
                            response.setIdDBPersona(cursor1.getInt(0));
                            response.setInfractor_nombre(cursor1.getString(1));
                            response.setInfractor_paterno(cursor1.getString(2));
                            response.setInfractor_materno(cursor1.getString(3));
                        }

                        cursor1.close();

                        if (isSync){
                            response.setIsAusente(0);
                            response.setInfractor_rfc("");
                            response.setInfractor_calle("");
                            response.setInfractor_exterior("");
                            response.setInfractor_interior("");
                            response.setInfractor_colonia("");
                            response.setTipolic_num("");
                            response.setInfractor_entidad(0);
                            response.setInfractor_entidadtext("");
                            response.setInfractor_delmun(0);
                            response.setInfractor_delmuntext("");
                            response.setTipolic_tipo(0);
                            response.setTipolic_tipotext("");
                            response.setTipolic_expedida(0);
                            response.setTipolic_expedidatext("");
                            response.setIdDbDir(0);
                        } else {
                            response.setInfractor_rfc("-----");
                            response.setInfractor_calle("-----");
                            response.setInfractor_exterior("-----");
                            response.setInfractor_interior("-----");
                            response.setInfractor_colonia("-----");
                            response.setTipolic_num("-----");
                            response.setInfractor_entidad(0);
                            response.setInfractor_entidadtext("-----");
                            response.setInfractor_delmun(0);
                            response.setInfractor_delmuntext("-----");
                            response.setTipolic_tipo(0);
                            response.setTipolic_tipotext("-----");
                            response.setTipolic_expedida(0);
                            response.setTipolic_expedidatext("-----");
                            response.setIdDbDir(0);
                        }
                    }

                    if (response.getIsRemision() == 1) {
                        cursor1 = database.rawQuery(Const.Database.SQLQueries.Search.SEARCH_DISPOSICION + response.getInfraid() + "';", null);
                        if (cursor1 != null && cursor1.getCount() > 0) {
                            cursor1.moveToFirst();
                            response.setDisposicion(cursor1.getInt(0));
                            response.setDisposicion_text(cursor1.getString(1));
                        }
                        cursor1.close();
                    }
                    list.add(response);
                }

                cursor.close();
                return list;

            } else {
                assert cursor != null;
                cursor.close();
                return null;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private GetSaveInfra responseFillclean(GetSaveInfra response) {
        response.setInfraid("");
        response.setInfraFolio("");
        response.setInfradir_calle("");
        response.setInfradir_entre("");
        response.setInfradir_y("");
        response.setInfradir_colonia("");
        response.setAno("");
        response.setIdentificacion_num("");
        response.setTarjeta_circ("");
        response.setMotivacion("");
        response.setSalariosminimos("");
        response.setImporte("");
        response.setPuntossancion("");
        response.setLinea_uno("");
        response.setLinea_dos("");
        response.setLinea_tres("");
        response.setFecha_uno("");
        response.setFecha_dos("");
        response.setFecha_tres("");
        response.setImporte_uno("");
        response.setImporte_dos("");
        response.setImporte_tres("");
        response.setId_persona_ayun("");
        response.setFechahora("");
        response.setMarcatext("");
        response.setSubmarcatext("");
        response.setTipotext("");
        response.setColortext("");
        response.setIdentificaciontext("");
        response.setTipo_doctext("");
        response.setExpedido_entext("");
        response.setTaza_salario("");
        response.setSubmarca_otra("");
        response.setInfractor_nombre("");
        response.setInfractor_paterno("");
        response.setInfractor_materno("");
        response.setInfractor_rfc("");
        response.setInfractor_calle("");
        response.setInfractor_exterior("");
        response.setInfractor_interior("");
        response.setInfractor_colonia("");
        response.setTipolic_num("");
        response.setDisposicion_text("");
        response.setSe_retienetext("");
        response.setInfractor_entidadtext("");
        response.setInfractor_delmuntext("");
        response.setTipolic_tipotext("");
        response.setTipolic_expedidatext("");
        return response;
    }

    GetSaveInfra updateData(Persona persona, GetSaveInfra saveData) {
        GetSaveInfra response = saveData;
        try {
            if (saveData.getIsAusente() == 1){
                database.execSQL(Const.Database.SQLQueries.Insert.INSERT_PERSONA + persona.getNombre()+ "', '" + persona.getaPaterno() + "', '" + persona.getaMaterno() + "', '');");
                Cursor cursor = database.rawQuery("SELECT * FROM SIIPTC_PERSONA;", null);
                cursor.moveToLast();
                response.setIdDBPersona(cursor.getInt(0));
                response.setInfractor_nombre(cursor.getString(1));
                response.setInfractor_paterno(cursor.getString(2));
                response.setInfractor_materno(cursor.getString(3));
                cursor.close();
                response.setIsAusente(2);
                response.setInfractor_entidad(0);
                response.setInfractor_entidadtext("");
                response.setInfractor_rfc("");
                response.setInfractor_calle("");
                response.setInfractor_interior("");
                response.setInfractor_exterior("");
                response.setInfractor_delmun(0);
                response.setInfractor_delmuntext("");
                response.setInfractor_colonia("");
                response.setTipolic_num("");
                response.setTipolic_tipo(0);
                response.setTipolic_expedida(0);
                response.setTipolic_expedidatext("");
                database.execSQL("INSERT INTO SIIPTA_INFRACCION_PERSONA_INFRACTOR VALUES ('" + response.getInfraid() + "', '" + response.getIdDBPersona() + "');");
                database.execSQL("UPDATE SIIPTA_INFRACCION SET BAN_INFRACTOR_AUSENTE='2' WHERE ID_INFRACCION = '" + response.getInfraid() + "';");
                database.execSQL("UPDATE SIIPTA_INFRACCION SET BAN_ACTUALIZA='1' WHERE ID_INFRACCION = '" + response.getInfraid() + "';");
                database.execSQL("UPDATE SIIPTA_INFRACCION SET IS_SYNC='0' WHERE ID_INFRACCION = '" + response.getInfraid() + "';");
            } else {
                database.execSQL(Const.Database.SQLQueries.Update.UPDATE_PERSONA + "NOMBRE='"+persona.getNombre()+"', A_PATERNO='"+persona.getaPaterno()+"', A_MATERNO='"+persona.getaMaterno()+"' WHERE ID_PERSONA = '"+saveData.getIdDBPersona()+"'; ");
                response.setInfractor_nombre(persona.getNombre());
                response.setInfractor_paterno(persona.getaPaterno());
                response.setInfractor_materno(persona.getaMaterno());
                database.execSQL("UPDATE SIIPTA_INFRACCION SET BAN_ACTUALIZA='1' WHERE ID_INFRACCION = '" + response.getInfraid() + "';");
                database.execSQL("UPDATE SIIPTA_INFRACCION SET IS_SYNC='0' WHERE ID_INFRACCION = '" + response.getInfraid() + "';");
            }

            return response;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    ArrayList<JSONObject> getSync(Context context) {

        PreferenceHelper pHelper = new PreferenceHelper(context);
        ArrayList<JSONObject> list = new ArrayList<>();

        ArrayList<GetSaveInfra> infracciones = getInfracciones(new SearchTerms(), true);

        if (infracciones != null){
            if (infracciones.size() > 0){
                try {

                    JSONArray identificators = new JSONArray();
                    JSONObject ids;

                    JSONObject fullobject =  new JSONObject();
                    fullobject.put("username", "InfraMobile");
                    fullobject.put("password", "CF2E3EF25C90EB567243ADFACD4AA868");
                    JSONArray infraarray = new JSONArray();
                    JSONObject infraobject;
                    GetSaveInfra inframodel;
                    for (int i = 0; i < infracciones.size(); i++){
                        infraobject = new JSONObject();
                        inframodel = infracciones.get(i);

                        infraobject.put("InfraccionGuardar", inframodel.getUpdated());
                        infraobject.put("InfraccionFolio", inframodel.getInfraFolio());
                        infraobject.put("InfraccionFecha", inframodel.getFechahora());
                        infraobject.put("InfraccionMotivo", inframodel.getMotivacion());
                        infraobject.put("InfraccionBanRemitido", inframodel.getIsRemision());
                        infraobject.put("InfraccionDocumentoRetenido", inframodel.getSe_retienetext());
                        infraobject.put("InfraccionSalariosMinimosTotales", Integer.parseInt(inframodel.getSalariosminimos()));
                        infraobject.put("InfraccionImporteTotal", Double.parseDouble(inframodel.getImporte()));
                        infraobject.put("InfraccionTazaSalarioMinimo", Double.parseDouble(inframodel.getTaza_salario()));
                        infraobject.put("InfraccionBanPagada", 0);
                        infraobject.put("InfraccionIdFuente", 1);
                        infraobject.put("InfraccionIdSector", 4);
                        infraobject.put("InfraccionIdOficial", Integer.parseInt(inframodel.getId_persona_ayun()));
                        //infraobject.put("InfraccionIdOficial", 34);
                        infraobject.put("InfraccionIdEstatus", 1);
                        infraobject.put("InfraccionNumHojasExpediente", 2);
                        infraobject.put("InfraccionIdAutoridadExpidePlaca", inframodel.getTipo_doc());
                        infraobject.put("InfraccionFolioEvidencia", "");
                        infraobject.put("InfraccionIdDisposicion", inframodel.getDisposicion());
                        infraobject.put("InfraccionBanInfractorAusente", inframodel.getIsAusente());
                        infraobject.put("InfraccionPuntosSancion", Integer.parseInt(inframodel.getPuntossancion()));
                        infraobject.put("InfraccionLineaCapturaI", inframodel.getLinea_uno());
                        infraobject.put("InfraccionLineaCapturaII", inframodel.getLinea_dos());
                        infraobject.put("InfraccionLineaCapturaIII", inframodel.getLinea_tres());
                        infraobject.put("InfraccionFechaLineaCapturaI", inframodel.getFecha_uno());
                        infraobject.put("InfraccionFechaLineaCapturaII", inframodel.getFecha_dos());
                        infraobject.put("InfraccionFechaLineaCapturaIII", inframodel.getFecha_tres());
                        infraobject.put("InfraccionImporteLineaCapturaI", inframodel.getImporte_uno());
                        infraobject.put("InfraccionImporteLineaCapturaII", inframodel.getImporte_dos());
                        infraobject.put("InfraccionImporteLineaCapturaIII", inframodel.getImporte_tres());
                        infraobject.put("InfraccionDomicilioIdPais", pHelper.getIDPais());
                        infraobject.put("InfraccionDomicilioIdEstado", pHelper.getIDEntidad());
                        infraobject.put("InfraccionDomicilioIdMunicipio", pHelper.getIDMunicipio());
                        infraobject.put("InfraccionDomicilioColonia", inframodel.getInfradir_colonia());
                        infraobject.put("InfraccionDomicilioCalle", inframodel.getInfradir_calle());
                        infraobject.put("InfraccionDomicilioEntreCalle", inframodel.getInfradir_entre());
                        infraobject.put("InfraccionDomicilioYCalle", inframodel.getInfradir_y());
                        infraobject.put("InfraccionDomicilioX", inframodel.getLatitude());
                        infraobject.put("InfraccionDomicilioY", inframodel.getLongitude());

                        JSONArray fraccionesarray = new JSONArray();
                        JSONObject fracciones;

                        ArrayList<ArtFraccion> arts = inframodel.getArticulos();
                        for (int i2 = 0; i2 < arts.size(); i2++){
                            fracciones = new JSONObject();
                            fracciones.put("FraccionIdFraccion", Integer.parseInt(arts.get(i2).getId()));
                            fracciones.put("FraccionSalariosMinimos", Integer.parseInt(arts.get(i2).getSalarios()));
                            fracciones.put("FraccionPuntosSancion", Integer.parseInt(arts.get(i2).getPuntos()));
                            fraccionesarray.put(fracciones);
                        }

                        infraobject.put("InfraccionFracciones", fraccionesarray);

                        if (inframodel.getIsAusente() == 1){
                            infraobject.put("InfractorNombre", "Q");
                            infraobject.put("InfractorPaterno", "R");
                            infraobject.put("InfractorMaterno", "R");
                        } else {
                            infraobject.put("InfractorNombre", inframodel.getInfractor_nombre());
                            infraobject.put("InfractorPaterno", inframodel.getInfractor_paterno());
                            infraobject.put("InfractorMaterno", inframodel.getInfractor_materno());
                        }
                        infraobject.put("InfractorRfc", inframodel.getInfractor_rfc());
                        infraobject.put("InfractorNumPermisoLicencia", inframodel.getTipolic_num());
                        infraobject.put("InfractorIdTipoLicencia", inframodel.getTipolic_tipo());
                        infraobject.put("InfractorLicenciaExpedidaEn", inframodel.getTipolic_expedida());
                        infraobject.put("InfractorDomicilioExiste", inframodel.isIdDBDirPersona());
                        infraobject.put("InfractorDomicilioIdPais", pHelper.getIDPais());
                        infraobject.put("InfractorDomicilioIdEstado", inframodel.getInfractor_entidad());
                        infraobject.put("InfractorDomicilioIdMunicipioSEPOMEX", inframodel.getInfractor_delmun());
                        infraobject.put("InfractorDomicilioMunicipioSEPOMEX", inframodel.getInfractor_delmuntext());
                        infraobject.put("InfractorDomicilioColonia", inframodel.getInfractor_colonia());
                        infraobject.put("InfractorDomicilioCalle", inframodel.getInfractor_calle());
                        infraobject.put("InfractorDomicilioNumeroExterior", inframodel.getInfractor_exterior());
                        infraobject.put("InfractorDomicilioNumeroInterior", inframodel.getInfractor_interior());
                        infraobject.put("VehiculoIdMarca", inframodel.getMarca());
                        infraobject.put("VehiculoSubMarca", inframodel.getSubmarcatext());
                        infraobject.put("VehiculoTipo", inframodel.getTipotext());
                        infraobject.put("VehiculoColor", inframodel.getColortext());
                        infraobject.put("VehiculoModelo", inframodel.getAno());
                        infraobject.put("VehiculoIdDocumentoIdentificador", inframodel.getIdentificacion());
                        infraobject.put("VehiculoNumeroDocumentoIdentificador", inframodel.getIdentificacion_num());
                        infraobject.put("VehiculoDocumentoIdentificadorExpedidoEn", inframodel.getExpedido_entext());
                        infraobject.put("VehiculoBarCodePlaca", "");
                        infraobject.put("VehiculoTarjetaCirculacion", inframodel.getTarjeta_circ());
                        infraobject.put("PagoTarjetaNumeroAutorizacion", "");
                        infraobject.put("PagoTarjetaAID", "");
                        infraobject.put("PagoTarjetaAPPLabel", "");
                        infraobject.put("PagoTarjetaARQC", "");
                        infraobject.put("PagoTarjetaEntryType", "");
                        infraobject.put("PagoTarjetaMaskedPAN", "");
                        infraobject.put("PagoTarjetaTrxDate", "");
                        infraobject.put("PagoTarjetaTrxNb", "");
                        infraobject.put("PagoTarjetaTrxTime", "");
                        infraobject.put("PagoTarjetaSerieMovil", "");
                        infraobject.put("PagoTarjetaAfiliacion", "");
                        infraobject.put("PagoTarjetaVigenciaTarjeta", "");
                        infraobject.put("PagoTarjetaMensaje", "");
                        infraobject.put("PagoTarjetaTipoTarjeta", "");
                        infraobject.put("PagoTarjetaTipo", "");
                        infraobject.put("PagoTarjetaBancoEmisor", "");
                        infraobject.put("PagoTarjetaReferencia", "");
                        infraobject.put("PagoTarjetaImporte", "");
                        infraobject.put("PagoTarjetaTVR", "");
                        infraobject.put("PagoTarjetaTSI", "");
                        infraobject.put("PagoTarjetaNumeroControl", "");
                        infraobject.put("PagoTarjetaTarjetaHabiente", "");
                        infraobject.put("PagoTarjetaEMV_Data", "");
                        infraobject.put("PagoTarjetaTipoTransaccion", "");
                        infraobject.put("PagoInfraccionIdFormaPago", 0);
                        infraobject.put("PagoInfraccionSubtotal", 0);
                        infraobject.put("PagoInfraccionDescuento", 0);
                        infraobject.put("PagoInfraccionTotal", 0);
                        infraobject.put("PagoInfraccionObservacion", "");
                        infraobject.put("PagoInfraccionRecargos", 0);
                        infraobject.put("SincronizarIdUsuario", pHelper.getIdPersona());
                        //infraobject.put("SincronizarIdUsuario", 10700);

                        infraarray.put(infraobject);

                        ids = new JSONObject();
                        ids.put("id", inframodel.getInfraid());
                        identificators.put(ids);
                    }
                    fullobject.put("Infracciones", infraarray);

                    JSONObject idlist = new JSONObject();
                    idlist.put("ids", identificators);

                    list.add(fullobject);
                    list.add(idlist);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


        return list;
    }

    boolean updateSync(String[] ids_got, String[] ids_sent) {

        for (String anIds_sent : ids_sent) {
            database.execSQL("UPDATE SIIPTA_INFRACCION SET IS_SYNC='1' WHERE ID_INFRACCION = '" + anIds_sent + "';");
        }

        if (ids_got.length != 0){
            for (String anIds_got : ids_got) {
                database.execSQL("UPDATE SIIPTA_INFRACCION SET IS_SYNC='0' WHERE ID_INFRACCION = '" + anIds_got + "';");
            }
        }

        return true;
    }

    String getLastSync(Context context) {

        PreferenceHelper pHelper = new PreferenceHelper(context);

        String last = null;
        Cursor c = database.rawQuery("SELECT ULTIMA_SINCRONIZACION_CATALOGOS FROM SIIPTC_SINCRONIZACION;", null);
        if (c != null && c.getCount() > 0){
            c.moveToFirst();
            last = c.getString(0);
        }
        c.close();

        pHelper.putLastFecha(last);

        return last;
    }

    boolean setCatalogs(JSONObject object, Context context) {

        PreferenceHelper pHelper = new PreferenceHelper(context);
        JSONArray array;
        JSONObject object1;
        String[] args;

        try {

            int testcount = 0;
            int updatecount = 0;
            ContentValues values;

            array = object.getJSONArray("SIIPTA_PERSONA_ATRIBUTO");
            database.execSQL("DELETE FROM SIIPTA_PERSONA_ATRIBUTO;");
            database.execSQL(Const.Database.SQLQueries.Insert.INSERT_ADMIN_SIIPTA_PERSONA_ATRIBUTO);
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_PERSONA_ATRIBUTO", object1.getString("ID_PERSONA_ATRIBUTO"));
                values.put("ID_PERSONA", object1.getString("ID_PERSONA"));
                values.put("ID_ATRIBUTO", object1.getString("ID_ATRIBUTO"));
                database.insert("SIIPTA_PERSONA_ATRIBUTO", null, values);
            }

            array = object.getJSONArray("SIIPTA_PERSONA_CUENTA");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_CUENTA_USUARIO", object1.getString("ID_CUENTA_USUARIO"));
                values.put("ID_PERSONA", object1.getString("ID_PERSONA"));
                values.put("BAN_ACTIVA", object1.getString("BAN_ACTIVA"));
                values.put("USER_NAME", object1.getString("USER_NAME"));
                values.put("PASSWORD", object1.getString("PASSWORD"));

                args = new String[]{object1.getString("ID_CUENTA_USUARIO")};

                if (database.update("SIIPTA_PERSONA_CUENTA", values, "ID_CUENTA_USUARIO=?", args) == 0){
                    database.insert("SIIPTA_PERSONA_CUENTA", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_ADSCRIPCION");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_ADSCRIPCION", object1.getString("ID_ADSCRIPCION"));
                values.put("ADSCRIPCION", object1.getString("ADSCRIPCION"));
                values.put("BAN_VISIBLE", object1.getString("BAN_VISIBLE"));

                args = new String[]{object1.getString("ID_ADSCRIPCION")};

                if (database.update("SIIPTC_ADSCRIPCION", values, "ID_ADSCRIPCION=?", args) == 0){
                    database.insert("SIIPTC_ADSCRIPCION", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_ATRIBUTO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_ATRIBUTO", object1.getString("ID_ATRIBUTO"));
                values.put("ID_MODULO", object1.getString("ID_MODULO"));
                values.put("ATRIBUTO", object1.getString("ATRIBUTO"));

                args = new String[]{object1.getString("ID_ATRIBUTO")};

                if (database.update("SIIPTC_ATRIBUTO", values, "ID_ATRIBUTO=?", args) == 0){
                    database.insert("SIIPTC_ATRIBUTO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_COLOR");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_COLOR", object1.getString("ID_COLOR"));
                values.put("COLOR", object1.getString("COLOR"));

                args = new String[]{object1.getString("ID_COLOR")};

                if (database.update("SIIPTC_COLOR", values, "ID_COLOR=?", args) == 0){
                    database.insert("SIIPTC_COLOR", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_CONFIGURACION");
            database.execSQL("DELETE FROM SIIPTC_CONFIGURACION");
            object1 = array.getJSONObject(0);
            values = new ContentValues();
            values.put("SALARIO_MINIMO", object1.getString("SALARIO_MINIMO"));
            values.put("PLAZO_DIAS_DESCUENTO_INFRACCION", object1.getString("PLAZO_DIAS_DESCUENTO_INFRACCION"));
            values.put("TAZA_DESCUENTO_INFRACCION_MISMO_DIA", object1.getString("TAZA_DESCUENTO_INFRACCION_MISMO_DIA"));
            database.insert("SIIPTC_CONFIGURACION", null, values);
            pHelper.putHeader1(object1.getString("INFRACCION_IMPRESION_ENCABEZADO_I"));
            pHelper.putHeader2(object1.getString("INFRACCION_IMPRESION_ENCABEZADO_II"));
            pHelper.putHeader3(object1.getString("INFRACCION_IMPRESION_ENCABEZADO_III"));
            pHelper.putHeader4(object1.getString("INFRACCION_IMPRESION_ENCABEZADO_IV"));
            pHelper.putHeader5(object1.getString("INFRACCION_IMPRESION_ENCABEZADO_V"));
            pHelper.putHeader6(object1.getString("INFRACCION_IMPRESION_ENCABEZADO_VI"));
            pHelper.putFooter1(object1.getString("INFRACCION_IMPRESION_PIE_I"));
            pHelper.putFooter2(object1.getString("INFRACCION_IMPRESION_PIE_II"));
            pHelper.putFooter3(object1.getString("INFRACCION_IMPRESION_PIE_III"));
            pHelper.putDirOne(object1.getString("INFRACCION_IMPRESION_DIR_PAGO_I"));
            pHelper.putDirTwo(object1.getString("INFRACCION_IMPRESION_DIR_PAGO_II"));
            pHelper.putDirThree(object1.getString("INFRACCION_IMPRESION_DIR_PAGO_III"));
            pHelper.putDirFour(object1.getString("INFRACCION_IMPRESION_DIR_PAGO_IV"));
            pHelper.putMunicipio(object1.getString("INFRACCION_MUNICIPIO"));
            pHelper.putIDPais(object1.getString("INFRACCION_ID_PAIS"));
            pHelper.putIDEntidad(object1.getString("INFRACCION_ID_ENTIDAD"));
            pHelper.putIDMunicipio(object1.getString("INFRACCION_ID_MUNICIPIO"));

            array = object.getJSONArray("SIIPTC_DIA_NO_HABIL");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_DIA", object1.getString("ID_DIA"));
                values.put("FECHA", object1.getString("FECHA"));

                args = new String[]{object1.getString("ID_DIA")};

                if (database.update("SIIPTC_DIA_NO_HABIL", values, "ID_DIA=?", args) == 0){
                    database.insert("SIIPTC_DIA_NO_HABIL", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_ESTADO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_ESTADO", object1.getString("ID_ESTADO"));
                values.put("ID_PAIS", object1.getString("ID_PAIS"));
                values.put("ESTADO", object1.getString("ESTADO"));

                args = new String[]{object1.getString("ID_ESTADO")};

                if (database.update("SIIPTC_ESTADO", values, "ID_ESTADO=?", args) == 0){
                    database.insert("SIIPTC_ESTADO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_INFRACCION_ARTICULO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_ARTICULO", object1.getString("ID"));
                values.put("ARTICULO", object1.getString("ARTICULO"));
                values.put("DESCRIPCION", object1.getString("DESCRIPCION"));

                args = new String[]{object1.getString("ID")};

                if (database.update("SIIPTC_INFRACCION_ARTICULO", values, "ID_ARTICULO=?", args) == 0){
                    database.insert("SIIPTC_INFRACCION_ARTICULO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_INFRACCION_AUTORIDAD_EXPIDE");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_AUTORIDAD_EXPIDE", object1.getString("ID_AUTORIDAD"));
                values.put("AUTORIDAD", object1.getString("AUTORIDAD"));

                args = new String[]{object1.getString("ID_AUTORIDAD")};

                if (database.update("SIIPTC_INFRACCION_AUTORIDAD_EXPIDE", values, "ID_AUTORIDAD_EXPIDE=?", args) == 0){
                    database.insert("SIIPTC_INFRACCION_AUTORIDAD_EXPIDE", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_INFRACCION_DISPOSICION");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_DISPOSICION", object1.getString("ID_DISPOSICION"));
                values.put("DISPOSICION", object1.getString("DISPOSICION"));

                args = new String[]{object1.getString("ID_DISPOSICION")};

                if (database.update("SIIPTC_INFRACCION_DISPOSICION", values, "ID_DISPOSICION=?", args) == 0){
                    database.insert("SIIPTC_INFRACCION_DISPOSICION", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_INFRACCION_DOCUMENTO_IDENTIFICADOR");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_DOCUMENTO_IDENTIFICADOR", object1.getString("ID"));
                values.put("DOCUMENTO", object1.getString("DOCUMENTO"));

                args = new String[]{object1.getString("ID")};

                if (database.update("SIIPTC_INFRACCION_DOCUMENTO_IDENTIFICADOR", values, "ID_DOCUMENTO_IDENTIFICADOR=?", args) == 0){
                    database.insert("SIIPTC_INFRACCION_DOCUMENTO_IDENTIFICADOR", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_INFRACCION_DOCUMENTO_RETENIDO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_DOCUMENTO_RETENIDO", object1.getString("ID_DOCUMENTO"));
                values.put("DOCUMENTO", object1.getString("DOCUMENTO"));

                args = new String[]{object1.getString("ID_DOCUMENTO")};

                if (database.update("SIIPTC_INFRACCION_DOCUMENTO_RETENIDO", values, "ID_DOCUMENTO_RETENIDO=?", args) == 0){
                    database.insert("SIIPTC_INFRACCION_DOCUMENTO_RETENIDO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_INFRACCION_FRACCION");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_FRACCION", object1.getString("ID"));
                values.put("ID_ARTICULO", object1.getString("ID_ARTICULO"));
                values.put("FRACCION", object1.getString("FRACCION"));
                values.put("DESCRIPCION", object1.getString("DESCRIPCION"));
                values.put("ALIAS_TICKET", object1.getString("ALIAS_TICKET"));
                values.put("SALARIOS_MINIMOS", object1.getString("SALARIOS_MINIMO"));
                values.put("PUNTOS_SANCION", object1.getString("PUNTOS_SANCION"));

                args = new String[]{object1.getString("ID")};

                if (database.update("SIIPTC_INFRACCION_FRACCION", values, "ID_FRACCION=?", args) == 0){
                    database.insert("SIIPTC_INFRACCION_FRACCION", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_INFRACCION_TIPO_LICENCIA");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_TIPO_LICENCIA", object1.getString("ID"));
                values.put("TIPO_LICENCIA", object1.getString("TIPO_LICENCIA"));

                args = new String[]{object1.getString("ID")};

                if (database.update("SIIPTC_INFRACCION_TIPO_LICENCIA", values, "ID_TIPO_LICENCIA=?", args) == 0){
                    database.insert("SIIPTC_INFRACCION_TIPO_LICENCIA", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_INFRACCION_TIPO_VEHICULO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_TIPO", object1.getString("ID_TIPO"));
                values.put("TIPO", object1.getString("TIPO"));

                args = new String[]{object1.getString("ID_TIPO")};

                if (database.update("SIIPTC_INFRACCION_TIPO_VEHICULO", values, "ID_TIPO=?", args) == 0){
                    database.insert("SIIPTC_INFRACCION_TIPO_VEHICULO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_MARCA_VEHICULO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_MARCA_VEHICULO", object1.getString("ID_MARCA_VEHICULO"));
                values.put("MARCA_VEHICULO", object1.getString("MARCA_VEHICULO"));

                args = new String[]{object1.getString("ID_MARCA_VEHICULO")};

                if (database.update("SIIPTC_MARCA_VEHICULO", values, "ID_MARCA_VEHICULO=?", args) == 0){
                    database.insert("SIIPTC_MARCA_VEHICULO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_MODULO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_MODULO", object1.getString("ID_MODULO"));
                values.put("MODULO", object1.getString("MODULO"));

                args = new String[]{object1.getString("ID_MODULO")};

                if (database.update("SIIPTC_MODULO", values, "ID_MODULO=?", args) == 0){
                    database.insert("SIIPTC_MODULO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_MUNICIPIO_SEPOMEX");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_MUNICIPIO", object1.getString("ID_MUNICIPIO"));
                values.put("ID_ESTADO", object1.getString("ID_ESTADO"));
                values.put("MUNICIPIO", object1.getString("MUNICIPIO"));

                args = new String[]{object1.getString("ID_MUNICIPIO")};

                if (database.update("SIIPTC_MUNICIPIO_SEPOMEX", values, "ID_MUNICIPIO=?", args) == 0){
                    database.insert("SIIPTC_MUNICIPIO_SEPOMEX", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_PERSONA");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_PERSONA", object1.getString("ID_PERSONA"));
                values.put("NOMBRE", object1.getString("NOMBRE"));
                values.put("A_PATERNO", object1.getString("A_PATERNO"));
                values.put("A_MATERNO", object1.getString("A_MATERNO"));

                args = new String[]{object1.getString("ID_PERSONA")};

                if (database.update("SIIPTC_PERSONA", values, "ID_PERSONA=?", args) == 0){
                    database.insert("SIIPTC_PERSONA", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_PERSONA_AYUNTAMIENTO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_PERSONA_AYUNTAMIENTO", object1.getString("ID_PERSONA_AYUNTAMIENTO"));
                values.put("ID_PERSONA", object1.getString("ID_PERSONA"));
                values.put("EMPLEADO", object1.getString("EMPLEADO"));

                args = new String[]{object1.getString("ID_PERSONA_AYUNTAMIENTO")};

                if (database.update("SIIPTC_PERSONA_AYUNTAMIENTO", values, "ID_PERSONA_AYUNTAMIENTO=?", args) == 0){
                    database.insert("SIIPTC_PERSONA_AYUNTAMIENTO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_SUBMARCA_VEHICULO");
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                values = new ContentValues();
                values.put("ID_SUBMARCA_VEHICULO", object1.getString("ID_SUBMARCA_VEHICULO"));
                values.put("ID_MARCA_VEHICULO", object1.getString("ID_MARCA_VEHICULO"));
                values.put("SUBMARCA_VEHICULO", object1.getString("SUBMARCA_VEHICULO"));

                args = new String[]{object1.getString("ID_SUBMARCA_VEHICULO")};

                if (database.update("SIIPTC_SUBMARCA_VEHICULO", values, "ID_SUBMARCA_VEHICULO=?", args) == 0){
                    database.insert("SIIPTC_SUBMARCA_VEHICULO", null, values);
                }
            }

            array = object.getJSONArray("SIIPTC_SYNCRONIZACION");
            database.execSQL("DELETE FROM SIIPTC_SINCRONIZACION;");
            object1 = array.getJSONObject(0);
            values = new ContentValues();
            values.put("APLICACION_ACTIVA", object1.getString("APLICACION_ACTIVA"));
            values.put("ULTIMA_SINCRONIZACION_CATALOGOS", object.getString("ultimaFechaSincronizacion"));
            database.insert("SIIPTC_SINCRONIZACION", null, values);

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    String[] getImageSync() {

        String[] array = null;

        Cursor cursor = database.rawQuery("SELECT NAME FROM IMAGE_SYNC",null);
        if (cursor != null && cursor.getCount() > 0){
            array = new String[cursor.getCount() + 1];
            for (int i= 0; i < cursor.getCount(); i++){
                cursor.moveToPosition(i);
                array[i] = cursor.getString(0);
            }
        }
        cursor.close();
        return array;
    }

    void savephotos(String imageone, String imagetwo) {
        ContentValues values = new ContentValues();
        values.put("NAME", imageone);
        database.insert("IMAGE_SYNC", null, values);
        values.put("NAME", imagetwo);
        database.insert("IMAGE_SYNC", null, values);
    }

    void removeCorrectImg(String[] strings) {

        database.execSQL("DELETE FROM IMAGE_SYNC;");
        ContentValues values = new ContentValues();
        if (strings != null && strings.length < 0){
            for (int i = 0; i < strings.length; i++){
                values.put("NAME", strings[i]);
                database.insert("IMAGE_SYNC", null, values);
            }
        }

    }

    public String[] getOficial(String idayun) {
        Cursor cursor = database.rawQuery(Const.Database.SQLQueries.Search.SEARCH_OFICIAL + idayun + "';",null);
        cursor.moveToFirst();
        String[] response = new String[2];
        response[0] = cursor.getString(0);
        response[1] = cursor.getString(1);
        cursor.close();
        return response;
    }


    private class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context) {
            super(context, Const.DATABASE_NAME, null, Const.Database.DATABASE_VERSION);
            SQLiteDatabase.loadLibs(context);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(Const.DebugType.DATABASE, "Creating Database");
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_CONFIGURACION);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_DIA_NO_HABIL);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_FOLIO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_KEY);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_SINCRONIZACION);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_INFRACCION_AUTORIDAD_EXPIDE);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_INFRACCION_DISPOSICION);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_INFRACCION_DOCUMENTO_RETENIDO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_INFRACCION_TIPO_LICENCIA);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_PERSONA);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_PERSONA_AYUNTAMIENTO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_PERSONA_CUENTA);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_MODULO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_ATRIBUTO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_PERSONA_ATRIBUTO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_INFRACCION_ARTICULO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_INFRACCION_FRACCION);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_PAIS);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_ESTADO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_MUNICIPIO_SEPOMEX);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_DIRECCION);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_DIRECCION_PERSONA_INFRACTOR);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_COLOR);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_INFRACCION_TIPO_VEHICULO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_INFRACCION_DOCUMENTO_IDENTIFICADOR);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_MARCA_VEHICULO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_SUBMARCA_VEHICULO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_INFRACCION_VEHICULO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_INFRACCION);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_INFRACCION_PAGO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_INFRACCION_PAGO_TARJETA);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_INFRACCION_FRACCION);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_INFRACCION_INFRACCION_VEHICULO);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_INFRACCION_PERSONA_INFRACTOR);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTA_DIRECCION_INFRACCION);
            db.execSQL(Const.Database.SQLQueries.Create.ADD_SINC);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_SIIPTC_ADSCRIPCION);
            db.execSQL(Const.Database.SQLQueries.Create.CREATE_IMAGESYNC);

            db.execSQL(Const.Database.SQLQueries.Insert.INSERT_ADMIN_SIIPTC_MODULO);
            db.execSQL(Const.Database.SQLQueries.Insert.INSERT_ADMIN_SIIPTC_ATRIBUTO);
            db.execSQL(Const.Database.SQLQueries.Insert.INSERT_ADMIN_SIIPTC_PERSONA);
            db.execSQL(Const.Database.SQLQueries.Insert.INSERT_ADMIN_SIIPTC_PERSONA_AYUNTAMIENTO);
            db.execSQL(Const.Database.SQLQueries.Insert.INSERT_ADMIN_SIIPTA_PERSONA_CUENTA);
            db.execSQL(Const.Database.SQLQueries.Insert.INSERT_ADMIN_SIIPTA_PERSONA_ATRIBUTO);

            Log.i(Const.DebugType.DATABASE, "Created Database");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            /*if (i == 1 && i1==2){
                db.execSQL("ALTER TABLE SIIPTA_DIRECCION ADD COLUMN X REAL");
                db.execSQL("ALTER TABLE SIIPTA_DIRECCION ADD COLUMN Y REAL");
            }*/
        }
    }

    public DBAdapter open() throws SQLiteException{
        database = dbHelper.getWritableDatabase("99002701");
        return this;
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    void close() {
        dbHelper.close();
        database.close();
    }

    UserLogin getUserLogin(String username){

        UserLogin userLogin = null;
        try {
            Cursor cursor = database.rawQuery(Const.Database.SQLQueries.GET_USER_LOGIN + username +"' AND SIIPTA_PERSONA_CUENTA.BAN_ACTIVA = '1';", null);
            userLogin = new UserLogin();
            if (cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                userLogin.setIdPersonaAyuntamiento(cursor.getString(0));
                userLogin.setUsername(cursor.getString(1));
                userLogin.setPassword(cursor.getString(2));
                userLogin.setNombre(cursor.getString(3));
                userLogin.setaPaterno(cursor.getString(4));
                userLogin.setaMaterno(cursor.getString(5));
                userLogin.setIdPersona(cursor.getString(6));
            }
            cursor.close();
        } catch (SQLiteException | StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return userLogin;
    }

}
