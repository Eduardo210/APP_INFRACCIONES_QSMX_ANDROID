package mx.qsistemas.infracciones.Functions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.basewin.services.ServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import mx.qsistemas.infracciones.AsyncTaskCompleteListener;
import mx.qsistemas.infracciones.DataManagement.Models.ArtFraccion;
import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.Utils.Utils;
import vpos.apipackage.Print;

/**
 * Created by ingmtz on 03/01/18.
 */

public class PrintInfraNew {

    private Activity activity;
    private GetSaveInfra getSaveInfra;
    private int serviceCode;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;
    private Print printer;
    public AsyncPrint task;
    private PreferenceHelper pHelper;
    int ret;

    public PrintInfraNew(Activity activity, GetSaveInfra getSaveInfra, int serviceCode, AsyncTaskCompleteListener asyncTaskCompleteListener){
        this.activity = activity;
        this.getSaveInfra = getSaveInfra;
        this.serviceCode = serviceCode;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;

        task = (PrintInfraNew.AsyncPrint) new PrintInfraNew.AsyncPrint().execute();
    }

    class AsyncPrint extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            pHelper = new PreferenceHelper(activity);

            try {

                //ServiceManager.getInstence().init(activity.getApplicationContext());

                JSONObject objectprint = new JSONObject();
                StringBuilder currentsb;
                JSONArray spos = new JSONArray();
                JSONObject current;

                LinkedHashMap<String, String> content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "1");
                content.put("content", pHelper.getHeader1() + "\n" + pHelper.getHeader2()+ "\n" + pHelper.getHeader3()+ "\n" + pHelper.getHeader4()+ "\n" + pHelper.getHeader5()+ "\n" + pHelper.getHeader6()+ "\n\n\n");
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "1");
                content.put("content", "DETECCION Y LEVANTAMIENTO ELECTRONICO DE INFRACCIONES A CONDUCTORES DE VEHICULOS QUE CONTRAVENGAN LAS DISPOSICIONES EN MATERIA DE TRANSITO, EQUILIBRIO ECOLOGICO, PROTECCIÓN AL AMBIENTE Y PARA LA PREVENCION Y CONTROL DE LA CONTAMINACION, ASI COMO PAGO DE SANCIONES Y APLICACION DE MEDIDAS DE SEGURIDAD.\n\nEL C. AGENTE QUE SUSCRIBE LA PRESENTE BOLETA DE INFRACCION, ESTA FACULTADO EN TERMINOS DE LOS QUE SE ESTABLECE EN LOS ARTICULOS 21 Y 115, FRACCION III, INCISO H), DE LA CONSTITUCION  POLITICA DE LOS ESTADOS UNIDOS MEXICANOS DE ACUERDO A LO ESTABLECIDO EN LOS ARTICULOS 8.3, 8.10, 8.18, 8.19 BIS, 8.19 TERCERO Y 8.19 CUARTO, DEL CODIGO ADMINISTRATIVO DEL ESTADO DE MEXICO. ASI COMO HACER CONSTAR LOS HECHOS QUE MOTIVAN LA INFRACCION EN TERMINOS DEL ARTICULO 16 DE NUESTRA CARTA MAGNA.\n\n\n");
                content.put("position", "left");
                current = new JSONObject(content);
                spos.put(current);


                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "2");
                content.put("content", getSaveInfra.getFechahora() + "\n" + getSaveInfra.getInfraFolio() + "\n\n\n" );
                content.put("position", "right");
                current = new JSONObject(content);
                spos.put(current);

                if (getSaveInfra.getIsAusente() == 0){
                    currentsb = new StringBuilder();

                    currentsb.append((getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno()).toUpperCase());

                    if (!getSaveInfra.getInfractor_rfc().equals(""))
                        currentsb.append("\n" + getSaveInfra.getInfractor_rfc());
                    if (!getSaveInfra.getInfractor_exterior().equals(""))
                        currentsb.append("\n" + getSaveInfra.getInfractor_exterior());
                    if (!getSaveInfra.getInfractor_interior().equals(""))
                        currentsb.append("\n" + getSaveInfra.getInfractor_interior());
                    if (!getSaveInfra.getInfractor_colonia().equals(""))
                        currentsb.append("\n" + getSaveInfra.getInfractor_colonia());

                    currentsb.append("\n\n");

                    content = new LinkedHashMap<>();
                    content.put("content-type", "txt");
                    content.put("size", "2");
                    content.put("content", currentsb.toString());
                    content.put("position", "center");
                    current = new JSONObject(content);
                    spos.put(current);

                    currentsb = new StringBuilder();

                    if (!getSaveInfra.getTipolic_num().equals(""))
                        currentsb.append("\n" + "LICENCIA/PERMISO: " + getSaveInfra.getTipolic_num());
                    if (getSaveInfra.getTipolic_tipo() != 0)
                        currentsb.append("\n" + "TIPO LICENCIA: " + getSaveInfra.getTipolic_tipotext());
                    if (getSaveInfra.getTipolic_expedida() != 0)
                        currentsb.append("\n" + "EXPEDIDA: " + getSaveInfra.getTipolic_expedidatext());

                    currentsb.append("\n\n");

                    content = new LinkedHashMap<>();
                    content.put("content-type", "txt");
                    content.put("size", "1");
                    content.put("content", currentsb.toString());
                    content.put("position", "left");
                    current = new JSONObject(content);
                    spos.put(current);

                }

                if (getSaveInfra.getIsAusente() == 2){
                    content = new LinkedHashMap<>();
                    content.put("content-type", "txt");
                    content.put("size", "2");
                    content.put("content", (getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno()).toUpperCase() + "\n\n");
                    content.put("position", "center");
                    current = new JSONObject(content);
                    spos.put(current);
                }


                currentsb = new StringBuilder();

                currentsb.append("\n" + "CARACTERISTICAS DEL VEHICULO: ");
                currentsb.append("\n" + "MARCA: " + getSaveInfra.getMarcatext());
                if (getSaveInfra.getSubmarca() != 0){
                    currentsb.append("\n" + "SUBMARCA: " + getSaveInfra.getSubmarcatext());
                } else {
                    currentsb.append("\n" + "SUBMARCA: " + getSaveInfra.getSubmarca_otra());
                }

                currentsb.append("\n" + "TIPO: " + getSaveInfra.getTipotext());
                currentsb.append("\n" + "COLOR: " + getSaveInfra.getColortext());
                currentsb.append("\n" + "MODELO: " + getSaveInfra.getAno());
                currentsb.append("\n" + "IDENTIFICADOR: " + getSaveInfra.getIdentificaciontext());
                currentsb.append("\n" + "NUMERO: " + getSaveInfra.getIdentificacion_num());
                currentsb.append("\n" + "AUTORIDAD QUE EXPIDE: " + getSaveInfra.getTipo_doctext());
                currentsb.append("\n" + "EXPEDIDO: " + getSaveInfra.getExpedido_entext() + "\n");

                currentsb.append("\n" + "ARTICULOS DEL REGLAMENTO DE TRANSITO DEL ESTADO DE MEXICO:" );

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "1");
                content.put("content",  currentsb.toString());
                content.put("position", "left");
                current = new JSONObject(content);
                spos.put(current);


                currentsb = new StringBuilder();

                currentsb.append("\n\n" + "ARTICULO/FRACCION      U.M.A.     PUNTOS");
                currentsb.append("\n" + "*******************");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "1");
                content.put("content", currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);


                ArrayList<ArtFraccion> list = getSaveInfra.getArticulos();
                ArtFraccion artFraccion;

                for (int i=0; i < list.size(); i++){
                    artFraccion = list.get(i);
                    content = new LinkedHashMap<>();
                    content.put("content-type", "txt");
                    content.put("size", "2");
                    content.put("content", artFraccion.getArticulo() + "/" + artFraccion.getFraccion() + "      "  + artFraccion.getSalarios() + "      " + artFraccion.getPuntos());
                    content.put("position", "center");
                    current = new JSONObject(content);
                    spos.put(current);
                }


                currentsb = new StringBuilder();

                currentsb.append("\n\n" + "CONDUCTA QUE MOTIVA LA INFRACCION:");
                currentsb.append("\n" + getSaveInfra.getMotivacion() + "\n");



                currentsb.append("\n" + "CALLE:  " + getSaveInfra.getInfradir_calle());
                currentsb.append("\n" + "ENTRE: " + getSaveInfra.getInfradir_entre());
                currentsb.append("\n" + "Y: " + getSaveInfra.getInfradir_y());
                currentsb.append("\n" + "COLONIA: " + getSaveInfra.getInfradir_colonia() + "\n");


                currentsb.append("\n" + "DOCUMENTO QUE SE RETIENE:");

                if (getSaveInfra.getSe_retiene() == 0){
                    currentsb.append("\n" + "NINGUNO" + "\n");
                } else {
                    currentsb.append("\n" + getSaveInfra.getSe_retienetext() + "\n");
                }


                if (getSaveInfra.getIsRemision() == 1){
                    currentsb.append("\n" + "REMISION DEL VEHICULO: SI");
                    currentsb.append("\n" + getSaveInfra.getDisposicion_text()+ "\n\n");
                }



                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "1");
                content.put("content", currentsb.toString());
                content.put("position", "left");
                current = new JSONObject(content);
                spos.put(current);


                currentsb = new StringBuilder();
                currentsb.append("\n" + "RESPONSABLE DEL VEHICULO:"+ "\n");
                if (getSaveInfra.getIsAusente() == 0 || getSaveInfra.getIsAusente() == 2) {
                    currentsb.append("\n" + getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno() + "\n");
                } else {
                    currentsb.append("\n" + "Q R R" + "\n");
                }

                currentsb.append("\n" + "RECIBO DE CONFORMIDAD"+ "\n\n\n\n\n\n");
                currentsb.append("\n" + "FIRMA" + "\n\n");
                currentsb.append("\n" + "AGENTE:");
                currentsb.append("\n" + getSaveInfra.getOficialcaptura()+ "\n\n");
                currentsb.append("\n" + "EMPLEADO: " + getSaveInfra.getOficialnum()+ "\n");
                currentsb.append("\n" + "FIRMA");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "2");
                content.put("content", currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                //generateOxxoBarcode

                content = new LinkedHashMap<>();
                content.put("content-type", "one-dimension");
                content.put("size", "3");
                content.put("height", "2");
                content.put("content", getSaveInfra.getLinea_uno());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                currentsb = new StringBuilder();
                currentsb.append("\n" + getSaveInfra.getLinea_uno()+ "\n");
                currentsb.append("\n" + "CON 70 % DE DESCUENTO");
                currentsb.append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_uno());
                currentsb.append("\n" + "IMPORTE: " + getSaveInfra.getImporte_uno()+ "\n\n");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "2");
                content.put("content",currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);


                content = new LinkedHashMap<>();
                content.put("content-type", "one-dimension");
                content.put("size", "3");
                content.put("height", "2");
                content.put("content", getSaveInfra.getLinea_dos());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                currentsb = new StringBuilder();
                currentsb.append("\n" + getSaveInfra.getLinea_dos()+ "\n");
                currentsb.append("\n" + "CON 50% DE DESCUENTO");
                currentsb.append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_dos());
                currentsb.append("\n" + "IMPORTE: " + getSaveInfra.getImporte_dos()+ "\n\n");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "2");
                content.put("content",currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);



                content = new LinkedHashMap<>();
                content.put("content-type", "one-dimension");
                content.put("size", "3");
                content.put("height", "2");
                content.put("content", getSaveInfra.getLinea_tres());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                currentsb = new StringBuilder();
                currentsb.append("\n" + getSaveInfra.getLinea_tres()+ "\n");
                currentsb.append("\n" + "SIN DESCUENTO");
                currentsb.append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_tres());
                currentsb.append("\n" + "IMPORTE: " + getSaveInfra.getImporte_tres()+ "\n\n");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "2");
                content.put("content",currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);



                // INICIA OXXO

                currentsb = new StringBuilder();
                currentsb.append("\n" + "PAGO EN OXXO");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "3");
                content.put("content", currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat datefcap = new SimpleDateFormat("yyyyMMdd");
                Date da = datef.parse(getSaveInfra.getFecha_uno());
                String first = Utils.generateOxxoBarcode(datefcap.format(da), getSaveInfra.getInfraFolio(), getSaveInfra.getImporte_uno().replaceAll("\\D+",""), activity);
                da = datef.parse(getSaveInfra.getFecha_dos());
                String second = Utils.generateOxxoBarcode(datefcap.format(da), getSaveInfra.getInfraFolio(), getSaveInfra.getImporte_dos().replaceAll("\\D+",""), activity);
                da = datef.parse(getSaveInfra.getFecha_tres());
                String third = Utils.generateOxxoBarcode(datefcap.format(da), getSaveInfra.getInfraFolio(), getSaveInfra.getImporte_tres().replaceAll("\\D+",""), activity);

                content = new LinkedHashMap<>();
                content.put("content-type", "one-dimension");
                content.put("size", "3");
                content.put("height", "2");
                content.put("content", first);
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                currentsb = new StringBuilder();
                currentsb.append("\n" + first + "\n");
                currentsb.append("\n" + "CON 70 % DE DESCUENTO");
                currentsb.append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_uno());
                currentsb.append("\n" + "IMPORTE: " + getSaveInfra.getImporte_uno()+ "\n\n");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "2");
                content.put("content",currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);


                content = new LinkedHashMap<>();
                content.put("content-type", "one-dimension");
                content.put("size", "3");
                content.put("height", "2");
                content.put("content", second);
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                currentsb = new StringBuilder();
                currentsb.append("\n" + second+ "\n");
                currentsb.append("\n" + "CON 50% DE DESCUENTO");
                currentsb.append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_dos());
                currentsb.append("\n" + "IMPORTE: " + getSaveInfra.getImporte_dos()+ "\n\n");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "2");
                content.put("content",currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);



                content = new LinkedHashMap<>();
                content.put("content-type", "one-dimension");
                content.put("size", "3");
                content.put("height", "2");
                content.put("content", third);
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                currentsb = new StringBuilder();
                currentsb.append("\n" + third + "\n");
                currentsb.append("\n" + "SIN DESCUENTO");
                currentsb.append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_tres());
                currentsb.append("\n" + "IMPORTE: " + getSaveInfra.getImporte_tres()+ "\n\n");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "2");
                content.put("content",currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);

                currentsb = new StringBuilder();

                currentsb.append("\n" + pHelper.getFooter1());
                currentsb.append("\n" + pHelper.getFooter2());
                currentsb.append("\n" + pHelper.getFooter3() + "\n");
                currentsb.append("\n" + "Los datos personales proporcionados en este documento, están protegidos por la Ley de Proteccion de Datos Personales en Posesión de Sujetos Obligados del Estado de México y Municipios. El aviso de Privacidad Integral correspondiente puede ser consultado en su formáto vigente en la página electrónica\nhttps://www.atizapan.gob.mx/transparencia/avisos-privacidad" + "\n");
                currentsb.append("\n" + "FUENTE DE CAPTURA: SIIP" + "\n\n");

                content = new LinkedHashMap<>();
                content.put("content-type", "txt");
                content.put("size", "1");
                content.put("content", currentsb.toString());
                content.put("position", "center");
                current = new JSONObject(content);
                spos.put(current);


                objectprint.put("spos", spos);

                ServiceManager.getInstence().getPrinter().print(objectprint.toString(), null, null);

                return true;

            } catch (Exception e){
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean response) {
            if (asyncTaskCompleteListener != null){
                if (response){
                    try {
                        asyncTaskCompleteListener.onTaskCompleted("true", serviceCode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        asyncTaskCompleteListener.onTaskCompleted("false", serviceCode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
