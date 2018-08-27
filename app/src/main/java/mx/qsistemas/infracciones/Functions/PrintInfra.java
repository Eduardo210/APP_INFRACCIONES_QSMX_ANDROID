package mx.qsistemas.infracciones.Functions;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.zxing.BarcodeFormat;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mx.qsistemas.infracciones.AsyncTaskCompleteListener;
import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Models.ArtFraccion;
import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;

import mx.qsistemas.infracciones.DataManagement.Models.Persona;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import vpos.apipackage.Print;

/**
 * Developed by ingmtz on 11/3/16.
 */

public class PrintInfra {

    private Activity activity;
    private GetSaveInfra getSaveInfra;
    private int serviceCode;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;
    private Print printer;
    public AsyncPrint task;
    private PreferenceHelper pHelper;
    int ret;

    public PrintInfra(Activity activity, GetSaveInfra getSaveInfra, int serviceCode, AsyncTaskCompleteListener asyncTaskCompleteListener){
        this.activity = activity;
        this.getSaveInfra = getSaveInfra;
        this.serviceCode = serviceCode;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;

        task = (AsyncPrint) new AsyncPrint().execute();
    }

    class AsyncPrint extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {

            pHelper = new PreferenceHelper(activity);

            try {


                Print.Lib_PrnSetGray((byte) 7);
                Print.Lib_PrnSetFont((byte) 20, (byte) 20, (byte) 0x00);
                Print.Lib_PrnInit();
                printer = new Print();

                Thread.sleep(400);

                Print.Lib_PrnStr(textoSangria(pHelper.getHeader1()));
                Print.Lib_PrnStr(textoSangria(pHelper.getHeader2()));
                Print.Lib_PrnStr(textoSangria(pHelper.getHeader3()));
                Print.Lib_PrnStr(textoSangria(pHelper.getHeader4()));
                Print.Lib_PrnStr(textoSangria(pHelper.getHeader5()));
                Print.Lib_PrnStr(textoSangria(pHelper.getHeader6()));

                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());

                ret = Print.Lib_PrnStart();

                if(ret != 0){
                    Log.d(Const.DebugType.PRINT, "Lib_PrnStart fail, ret = " + ret);
                    return false;
                }

                Thread.sleep(100);
                Print.Lib_PrnInit();

                Print.Lib_PrnStr("DETECCION Y LEVANTAMIENTO ELEC-");
                Print.Lib_PrnStr("TRONICO DE INFRACCIONES A  CON-");
                Print.Lib_PrnStr("DUCTORES DE  VEHICULOS QUE  CON-");
                Print.Lib_PrnStr("TRAVENGAN LAS DISPOSICIONES  EN");
                Print.Lib_PrnStr("MATERIA DE TRANSITO, EQUILIBRIO");
                Print.Lib_PrnStr("ECOLOGICO,PROTECCIÓN AL AMBIEN-");
                Print.Lib_PrnStr("TE Y PARA LA PREVENCION Y  CON-");
                Print.Lib_PrnStr("TROL DE  LA  CONTAMINACION, ASI");
                Print.Lib_PrnStr("COMO PAGO DE SANCIONES Y  APLI-");
                Print.Lib_PrnStr("CACION DE MEDIDAS DE SEGURIDAD.");
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr("EL C.  AGENTE  QUE SUSCRIBE  LA");
                Print.Lib_PrnStr("PRESENTE   BOLETA   DE  INFRAC-");
                Print.Lib_PrnStr("CION, ESTA  FACULTADO  EN  TER-");
                Print.Lib_PrnStr("MINOS  DE LOS  QUE SE ESTABLECE");
                Print.Lib_PrnStr("EN  LOS  ARTICULOS   21  Y  115,");
                Print.Lib_PrnStr("FRACCION III, INCISO H),  DE LA");
                Print.Lib_PrnStr("CONSTITUCION  POLITICA  DE  LOS");
                Print.Lib_PrnStr("ESTADOS  UNIDOS   MEXICANOS  DE");
                Print.Lib_PrnStr("ACUERDO  A  LO  ESTABLECIDO  EN");
                Print.Lib_PrnStr("LOS ARTICULOS   8.3, 8.10, 8.18,");
                Print.Lib_PrnStr("8.19 BIS, 8.19 TERCERO  Y  8.19");
                Print.Lib_PrnStr("CUARTO, DEL  CODIGO ADMINISTRA-");
                Print.Lib_PrnStr("TIVO DEL ESTADO DE MEXICO.");
                Print.Lib_PrnStr("ASI COMO  HACER CONSTAR LOS  HE-");
                Print.Lib_PrnStr("CHOS  QUE MOTIVAN LA INFRACCION");
                Print.Lib_PrnStr("EN  TERMINOS  DEL ARTICULO 16 DE");
                Print.Lib_PrnStr("NUESTRA CARTA MAGNA.");

                ret = Print.Lib_PrnStart();

                if(ret != 0){
                    Log.d(Const.DebugType.PRINT, "Lib_PrnStart fail, ret = " + ret);
                    return false;
                }

                Thread.sleep(100);
                Print.Lib_PrnInit();


                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr("       " + getSaveInfra.getFechahora());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());

                Print.Lib_PrnStr("                 FOLIO:" + getSaveInfra.getInfraFolio());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());


                if (getSaveInfra.getIsAusente() == 0){
                    Print.Lib_PrnStr(textoSangria(getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno()).toUpperCase());
                    if (!getSaveInfra.getInfractor_rfc().equals(""))
                        Print.Lib_PrnStr(textoSangria(getSaveInfra.getInfractor_rfc()));
                    if (!getSaveInfra.getInfractor_exterior().equals(""))
                        Print.Lib_PrnStr(textoSangria(getSaveInfra.getInfractor_exterior()));
                    if (!getSaveInfra.getInfractor_interior().equals(""))
                        Print.Lib_PrnStr(textoSangria(getSaveInfra.getInfractor_interior()));
                    if (!getSaveInfra.getInfractor_colonia().equals(""))
                        Print.Lib_PrnStr(textoSangria(getSaveInfra.getInfractor_colonia()));
                    
                    Print.Lib_PrnStr(salto());

                    if (!getSaveInfra.getTipolic_num().equals(""))
                        Print.Lib_PrnStr("LICENCIA/PERMISO: " + getSaveInfra.getTipolic_num());
                    if (getSaveInfra.getTipolic_tipo() != 0)
                        Print.Lib_PrnStr("TIPO LICENCIA: " + getSaveInfra.getTipolic_tipotext());
                    if (getSaveInfra.getTipolic_expedida() != 0)
                        Print.Lib_PrnStr("EXPEDIDA: " + getSaveInfra.getTipolic_expedidatext());

                    Print.Lib_PrnStr(salto());
                }

                if (getSaveInfra.getIsAusente() == 2){
                    Print.Lib_PrnStr(textoSangria(getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno()).toUpperCase());
                    Print.Lib_PrnStr(salto());
                }

                Print.Lib_PrnStr("CARACTERISTICAS DEL VEHICULO:" +salto());
                Print.Lib_PrnStr("MARCA: " + getSaveInfra.getMarcatext()+ salto());
                if (getSaveInfra.getSubmarca() != 0){
                    Print.Lib_PrnStr("SUBMARCA: " + getSaveInfra.getSubmarcatext());
                } else {
                    Print.Lib_PrnStr("SUBMARCA: " + getSaveInfra.getSubmarca_otra());
                }

                Print.Lib_PrnStr("TIPO: " + getSaveInfra.getTipotext());
                Print.Lib_PrnStr("COLOR: " + getSaveInfra.getColortext());
                Print.Lib_PrnStr("MODELO: " + getSaveInfra.getAno());
                Print.Lib_PrnStr("IDENTIFICADOR: " + getSaveInfra.getIdentificaciontext());
                Print.Lib_PrnStr("NUMERO: " + getSaveInfra.getIdentificacion_num());
                Print.Lib_PrnStr("AUTORIDAD QUE EXPIDE: " + getSaveInfra.getTipo_doctext());
                Print.Lib_PrnStr("EXPEDIDO: " + getSaveInfra.getExpedido_entext());
                Print.Lib_PrnStr(salto());

                Print.Lib_PrnStr("ARTICULOS DEL REGLAMENTO DE TRANSITO DEL ESTADO DE MEXICO:" + salto());


                Print.Lib_PrnStr("ART./FRAC.    D.S.M.     PUNTOS");
                Print.Lib_PrnStr("*******************************");

                ArrayList<ArtFraccion> list = getSaveInfra.getArticulos();
                ArtFraccion artFraccion;

                for (int i=0; i < list.size(); i++){
                    artFraccion = list.get(i);

                    String articulofraccion = artFraccion.getArticulo() + "/" + artFraccion.getFraccion();
                    String diassalario = artFraccion.getSalarios();
                    String puntos = artFraccion.getPuntos();

                    for (int l = articulofraccion.length(); l <= 15; l++){
                        articulofraccion = articulofraccion + " ";
                    }
                    for (int l = diassalario.length(); l <= 7; l++){
                        diassalario = diassalario + " ";
                    }
                    for (int l = puntos.length(); l <= 6; l++){
                        puntos = " " + puntos;
                    }

                    Print.Lib_PrnStr(articulofraccion + diassalario + puntos);
                }
                
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr("CONDUCTA QUE MOTIVA LA");
                Print.Lib_PrnStr("INFRACCION:");
                Print.Lib_PrnStr(getSaveInfra.getMotivacion() + salto());


                
                Print.Lib_PrnStr("CALLE:  " + getSaveInfra.getInfradir_calle());
                Print.Lib_PrnStr("ENTRE: " + getSaveInfra.getInfradir_entre());
                Print.Lib_PrnStr("Y: " + getSaveInfra.getInfradir_y());
                Print.Lib_PrnStr("COLONIA: " + getSaveInfra.getInfradir_colonia());

                Print.Lib_PrnStr(salto());

                Print.Lib_PrnStr("DOCUMENTO QUE SE RETIENE:" + salto());

                if (getSaveInfra.getSe_retiene() == 0){
                    Print.Lib_PrnStr("NINGUNO" + salto());
                } else {
                    Print.Lib_PrnStr(getSaveInfra.getSe_retienetext() + salto());
                }

                Print.Lib_PrnStr(salto());

                if (getSaveInfra.getIsRemision() == 1){
                    Print.Lib_PrnStr("REMISION DEL VEHICULO: SI");
                    Print.Lib_PrnStr(salto());
                    Print.Lib_PrnStr(getSaveInfra.getDisposicion_text());
                }

                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr("RESPONSABLE DEL VEHICULO:");
                Print.Lib_PrnStr(salto());

                if (getSaveInfra.getIsAusente() == 0 || getSaveInfra.getIsAusente() == 2) {
                    Print.Lib_PrnStr(textoSangria(getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno()));
                    Print.Lib_PrnStr(salto());
                } else {
                    Print.Lib_PrnStr(textoSangria("Q R R"));
                    Print.Lib_PrnStr(salto());
                }

                Print.Lib_PrnStr(textoSangria("RECIBO DE CONFORMIDAD"));
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(textoSangria("FIRMA"));
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr("AGENTE:");
                Print.Lib_PrnStr(getSaveInfra.getOficialcaptura());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(textoSangria("EMPLEADO: " + getSaveInfra.getOficialnum()));
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(textoSangria("FIRMA"));
                Print.Lib_PrnStr(salto());

                ret = Print.Lib_PrnStart();

                if(ret != 0){
                    Log.d(Const.DebugType.PRINT, "Lib_PrnStart fail, ret = " + ret);
                    return false;
                }

                Thread.sleep(100);
                Print.Lib_PrnInit();

                printer.Lib_PrnBarcode(getSaveInfra.getLinea_uno(), 360, 120, BarcodeFormat.CODE_39);
                Print.Lib_PrnStr(textoSangria(getSaveInfra.getLinea_uno()));
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(textoSangria("CON 70 POR CIENTO DE DESCUENTO"));
                Print.Lib_PrnStr(textoSangria("VIGENCIA: " + getSaveInfra.getFecha_uno()));
                Print.Lib_PrnStr(textoSangria("IMPORTE: " + getSaveInfra.getImporte_uno()));
                Print.Lib_PrnStr(salto());

                printer.Lib_PrnBarcode(getSaveInfra.getLinea_dos(), 360, 120, BarcodeFormat.CODE_39);
                Print.Lib_PrnStr(textoSangria(getSaveInfra.getLinea_dos()));
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(textoSangria("CON 50 POR CIENTO DE DESCUENTO"));
                Print.Lib_PrnStr(textoSangria("VIGENCIA: " + getSaveInfra.getFecha_dos()));
                Print.Lib_PrnStr(textoSangria("IMPORTE: " + getSaveInfra.getImporte_dos()));
                Print.Lib_PrnStr(salto());

                printer.Lib_PrnBarcode(getSaveInfra.getLinea_tres(), 360, 120, BarcodeFormat.CODE_39);
                Print.Lib_PrnStr(textoSangria(getSaveInfra.getLinea_tres()));
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr(textoSangria("SIN DESCUENTO"));
                Print.Lib_PrnStr(textoSangria("VIGENCIA: " + getSaveInfra.getFecha_tres()));
                Print.Lib_PrnStr(textoSangria("IMPORTE: " + getSaveInfra.getImporte_tres()));
                Print.Lib_PrnStr(salto());

                Print.Lib_PrnStr(textoSangria(pHelper.getFooter1()));
                Print.Lib_PrnStr(textoSangria(pHelper.getFooter2()));
                Print.Lib_PrnStr(textoSangria(pHelper.getFooter3()));
                Print.Lib_PrnStr(salto());
                Print.Lib_PrnStr("    FUENTE DE CAPTURA: SIIP    ");
                Print.Lib_PrnStr(salto()+salto()+salto()+salto()+salto()+salto());

                int ret = Print.Lib_PrnStart();
                Log.d(Const.DebugType.PRINT, String.valueOf(ret));

            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }

        private String salto() {
            return "                                ";
        }

        String textoSangria(String texto){
            int lengthtxt = (32 - texto.length()) / 2;
            String textosangria = "";
            if (lengthtxt > 0){
                for (int i = 1; i <= lengthtxt; i++){
                    textosangria += "\u0020";
                }
            }

            textosangria = textosangria + texto;

            return textosangria;
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
