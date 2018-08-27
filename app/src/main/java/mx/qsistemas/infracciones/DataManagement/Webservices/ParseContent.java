package mx.qsistemas.infracciones.DataManagement.Webservices;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Database.DBHelper;
import mx.qsistemas.infracciones.DataManagement.Models.ArtFraccion;
import mx.qsistemas.infracciones.DataManagement.Models.Catalogs;
import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;
import mx.qsistemas.infracciones.Utils.Utils;

/**
 * Developed by ingmtz on 11/21/16.
 */

public class ParseContent {

    Context context;

    public ParseContent(Context context){
        this.context = context;
    }

    public boolean isSuccess(String response){
        boolean success = false;
        try {
            JSONObject object = new JSONObject(response);
            success = Boolean.parseBoolean(object.getString("Flag"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }

    public String getError(String response) {
        String error = null;
        try {
            JSONObject object = new JSONObject(response);
            error = object.getString("Mensaje");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error;
    }

    public GetSaveInfra getdataitem(String response, ArrayList<ArrayList<Catalogs>> catalogs) {

        ArrayList<Catalogs> marcalist, submarcalist, tipodoclist, fraccionlist, idlist, tipolist, colorlist, estadolist, disposicionlist, seretienelist, tipoliclist, articulolist;

        marcalist = catalogs.get(0);
        submarcalist = catalogs.get(1);
        tipolist = catalogs.get(2);
        colorlist = catalogs.get(3);
        idlist = catalogs.get(4);
        estadolist = catalogs.get(5);
        tipodoclist = catalogs.get(6);
        articulolist = catalogs.get(7);
        fraccionlist = catalogs.get(8);
        disposicionlist = catalogs.get(9);
        seretienelist = catalogs.get(10);
        tipoliclist = catalogs.get(12);

        Catalogs catalog;
        GetSaveInfra item = null;
        try {
            item = new GetSaveInfra();
            JSONObject object = new JSONObject(response);
            item.setInfraFolio(object.getString("InfraccionFolio"));
            item.setInfradir_calle(object.getString("InfraccionDomicilioCalle"));
            item.setInfradir_entre(object.getString("InfraccionDomicilioEntreCalle"));
            item.setInfradir_y(object.getString("InfraccionDomicilioYCalle"));
            item.setInfradir_colonia(object.getString("InfraccionDomicilioColonia"));
            item.setAno(object.getString("VehiculoModelo"));
            item.setIdentificacion_num(object.getString("VehiculoNumeroDocumentoIdentificador"));
            item.setTarjeta_circ(object.getString("VehiculoTarjetaCirculacion"));
            item.setMotivacion("");
            item.setSalariosminimos(object.getString("InfraccionSalariosMinimosTotales"));
            item.setImporte(object.getString("InfraccionImporteTotal"));
            item.setPuntossancion(object.getString("InfraccionPuntosSancion"));
            item.setLinea_uno(object.getString("InfraccionLineaCapturaI"));
            item.setLinea_dos(object.getString("InfraccionLineaCapturaII"));
            item.setLinea_tres(object.getString("InfraccionLineaCapturaIII"));
            item.setFecha_uno(object.getString("InfraccionFechaLineaCapturaI"));
            item.setFecha_dos(object.getString("InfraccionFechaLineaCapturaII"));
            item.setFecha_tres(object.getString("InfraccionFechaLineaCapturaIII"));
            item.setImporte_uno(object.getString("InfraccionImporteLineaCapturaI"));
            item.setImporte_dos(object.getString("InfraccionImporteLineaCapturaII"));
            item.setImporte_tres(object.getString("InfraccionImporteLineaCapturaIII"));
            item.setId_persona_ayun(object.getString("InfraccionIdOficial"));
            item.setFechahora(object.getString("InfraccionFecha"));


            item.setMarca(object.getInt("VehiculoIdMarca"));

            for (int i= 0; i < submarcalist.size() ; i++){
                catalog = submarcalist.get(i);
                if (catalog.getValue().equals(object.getString("VehiculoSubMarca"))){
                    item.setSubmarca(Integer.valueOf(catalog.getId()));
                }
            }

            for (int i= 0; i < tipolist.size() ; i++){
                catalog = tipolist.get(i);
                if (catalog.getValue().equals(object.getString("VehiculoTipo"))){
                    item.setTipo(Integer.valueOf(catalog.getId()));
                }
            }

            for (int i= 0; i < colorlist.size() ; i++){
                catalog = colorlist.get(i);
                if (catalog.getValue().equals(object.getString("VehiculoColor"))){
                    item.setColor(Integer.valueOf(catalog.getId()));
                }
            }

            for (int i= 0; i < estadolist.size() ; i++){
                catalog = estadolist.get(i);
                if (catalog.getValue().equals(object.getString("VehiculoDocumentoIdentificadorExpedidoEn"))){
                    item.setExpedido_en(Integer.valueOf(catalog.getId()));
                }
            }


            item.setIdentificacion(object.getInt("VehiculoIdDocumentoIdentificador"));
            item.setTipo_doc(object.getInt("InfraccionIdAutoridadExpidePlaca"));
            item.setUpdated(object.getInt("InfraccionGuardar"));

            for (int i= 0; i < marcalist.size() ; i++){
                catalog = marcalist.get(i);
                if (catalog.getId().equals(object.getString("VehiculoIdMarca"))){
                    item.setMarcatext(catalog.getValue());
                }
            }

            item.setSubmarcatext(object.getString("VehiculoSubMarca"));
            item.setTipotext(object.getString("VehiculoTipo"));
            item.setColortext(object.getString("VehiculoColor"));

            for (int i= 0; i < idlist.size() ; i++){
                catalog = idlist.get(i);
                if (catalog.getId().equals(object.getString("VehiculoIdDocumentoIdentificador"))){
                    item.setIdentificaciontext(catalog.getValue());
                }
            }

            for (int i= 0; i < tipodoclist.size() ; i++){
                catalog = tipodoclist.get(i);
                if (catalog.getId().equals(object.getString("InfraccionIdAutoridadExpidePlaca"))){
                    item.setTipo_doctext(catalog.getValue());
                }
            }

            item.setExpedido_entext(object.getString("VehiculoDocumentoIdentificadorExpedidoEn"));
            item.setTaza_salario(object.getString("InfraccionTazaSalarioMinimo"));

            item.setIsRemision(object.getInt("InfraccionBanRemitido"));
            item.setIsAusente(object.getInt("InfraccionBanInfractorAusente"));
            item.setIsUpdate(0);
            item.setIsSync(0);

            item.setSubmarca_otra("");
            item.setInfractor_nombre(object.getString("InfractorNombre"));
            item.setInfractor_paterno(object.getString("InfractorPaterno"));
            item.setInfractor_materno(object.getString("InfractorMaterno"));
            item.setInfractor_rfc(object.getString("InfractorRfc"));
            item.setInfractor_calle(object.getString("InfractorDomicilioCalle"));
            item.setInfractor_exterior(object.getString("InfractorDomicilioNumeroExterior"));
            item.setInfractor_interior(object.getString("InfractorDomicilioNumeroInterior"));
            item.setInfractor_colonia(object.getString("InfractorDomicilioColonia"));
            item.setTipolic_num(object.getString("InfractorNumPermisoLicencia"));
            item.setMotivacion(object.getString("InfraccionMotivo"));

            item.setDisposicion(object.getInt("InfraccionIdDisposicion"));

            for (int i= 0; i < seretienelist.size() ; i++){
                catalog = seretienelist.get(i);
                if (catalog.getValue().equals(object.getString("InfraccionDocumentoRetenido"))){
                    item.setSe_retiene(Integer.valueOf(catalog.getId()));
                }
            }

            item.setInfractor_entidad(object.getInt("InfractorDomicilioIdEstado"));
            item.setInfractor_delmun(object.getInt("InfractorDomicilioIdMunicipioSEPOMEX"));
            item.setTipolic_tipo(object.getInt("InfractorIdTipoLicencia"));

            String lic_exp = object.getString("InfractorLicenciaExpedidaEn");

            if (Utils.isInteger(lic_exp, 10)) {
                item.setTipolic_expedida(object.getInt("InfractorLicenciaExpedidaEn"));
            } else {
                item.setTipolic_expedidatext(object.getString("InfractorLicenciaExpedidaEn"));
            }

            for (int i= 0; i < disposicionlist.size() ; i++){
                catalog = disposicionlist.get(i);
                if (catalog.getId().equals(object.getString("InfraccionIdDisposicion"))){
                    item.setDisposicion_text(catalog.getValue());
                }
            }

            item.setSe_retienetext(object.getString("InfraccionDocumentoRetenido"));


            for (int i= 0; i < estadolist.size() ; i++){
                catalog = estadolist.get(i);
                if (catalog.getId().equals(object.getString("InfractorDomicilioIdEstado"))){
                    item.setInfractor_entidadtext(catalog.getValue());
                }
            }

            item.setInfractor_delmuntext(object.getString("InfractorDomicilioMunicipioSEPOMEX"));

            for (int i= 0; i < tipoliclist.size() ; i++){
                catalog = tipoliclist.get(i);
                if (catalog.getId().equals(object.getString("InfractorIdTipoLicencia"))){
                    item.setTipolic_tipotext(catalog.getValue());
                }
            }

            for (int i= 0; i < estadolist.size() ; i++){
                catalog = estadolist.get(i);
                if (catalog.getId().equals(object.getString("InfractorLicenciaExpedidaEn"))){
                    item.setTipolic_expedidatext(catalog.getValue());
                }
            }

            ArrayList<ArtFraccion> list = new ArrayList<>();
            ArtFraccion artFraccion;

            JSONArray array = object.getJSONArray("InfraccionFracciones");
            JSONObject object1;
            for (int i=0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                artFraccion = new ArtFraccion();
                artFraccion.setId(object1.getString("FraccionIdFraccion"));
                artFraccion.setSalarios(object1.getString("FraccionSalariosMinimos"));
                artFraccion.setPuntos(object1.getString("FraccionPuntosSancion"));
                artFraccion.setIsSearch(1);

                for (int j = 0; j < fraccionlist.size(); j++){
                    catalog = fraccionlist.get(j);
                    if (catalog.getId().equals(artFraccion.getId())){
                        artFraccion.setDescripcion(catalog.getDataone());
                        artFraccion.setFraccion(catalog.getValue());
                        artFraccion.setIdarticulo(catalog.getUnion());
                    }
                }

                for (int j = 0; j < articulolist.size(); j++){
                    catalog = articulolist.get(j);
                    if (catalog.getId().equals(artFraccion.getIdarticulo())){
                        artFraccion.setArticulo(catalog.getValue());
                    }
                }
                list.add(artFraccion);
            }

            item.setArticulos(list);

        } catch (JSONException e) {
            item = null;
            e.printStackTrace();
        }
        return item;
    }

    public String[] errorImages(String response) {
        String[] results = null;
        try {
            JSONObject object = new JSONObject(response);
            JSONObject object1;
            JSONArray array = object.getJSONArray("Ids");
            results = new String[array.length() + 1];
            for (int i = 0; i < array.length(); i++){
                object1 = array.getJSONObject(i);
                results[i] = object1.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    String unifyReaders(String s) throws JSONException {

        String response;

        String[] readers = s.split(";");
        boolean flag = true;
        JSONObject object;
        StringBuilder failids = new StringBuilder();
        StringBuilder failmessages = new StringBuilder();
        LinkedHashMap<String, String> map;

        String separatorid = "";
        String separatormsj = "";

        for (int i = 0; i < readers.length; i++){
            object = new JSONObject(readers[i]);
            if (!object.getBoolean("Flag")){
                failids.append(separatorid);
                failids.append(object.getString("Ids"));
                separatorid = ",";
                failids.append(separatormsj);
                failmessages.append(object.getString("Mensaje"));
                separatormsj = ",";
                flag = false;
            }
        }

        if (flag){
            map = new LinkedHashMap<>();
            map.put("Flag", String.valueOf(flag));
            map.put("Mensaje", "Sincronización finalizada correctamente");
            map.put("Ids", "");
            object = new JSONObject(map);
            response = object.toString();
        } else {
            map = new LinkedHashMap<>();
            map.put("Flag", String.valueOf(flag));
            map.put("Mensaje", failmessages.toString());
            map.put("Ids", failids.toString());
            object = new JSONObject(map);
            response = object.toString();        }

        return response;

    }
}
