package mx.qsistemas.infracciones.Functions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.basewin.aidl.OnPrinterListener;
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

                ServiceManager.getInstence().init(activity.getApplicationContext());

                //generateOxxoBarcode

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat datefcap = new SimpleDateFormat("ddMMyyyy");
                Date da = datef.parse(getSaveInfra.getFecha_uno());
                @SuppressLint("DefaultLocale")
                final String first = Utils.generateOxxoBarcode(datefcap.format(da), getSaveInfra.getInfraFolio(), String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_uno())).replaceAll("\\D+",""), activity);
                da = datef.parse(getSaveInfra.getFecha_dos());
                @SuppressLint("DefaultLocale")
                final String second = Utils.generateOxxoBarcode(datefcap.format(da), getSaveInfra.getInfraFolio(), String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_dos())).replaceAll("\\D+",""), activity);
                da = datef.parse(getSaveInfra.getFecha_tres());
                @SuppressLint("DefaultLocale")
                final String third = Utils.generateOxxoBarcode(datefcap.format(da), getSaveInfra.getInfraFolio(), String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_tres())).replaceAll("\\D+",""), activity);


                final JSONObject[] objectprint = {new JSONObject()};
                final StringBuilder[] currentsb = new StringBuilder[1];
                final JSONArray[] spos = {new JSONArray()};
                final JSONObject[] current = new JSONObject[1];

                final LinkedHashMap<String, String>[] content = new LinkedHashMap[]{new LinkedHashMap<>()};
                content[0].put("content-type", "txt");
                content[0].put("size", "1");
                content[0].put("content", pHelper.getHeader1() + "\n" + pHelper.getHeader2()+ "\n" + pHelper.getHeader3()+ "\n" + pHelper.getHeader4()+ "\n" + pHelper.getHeader5()+ "\n" + pHelper.getHeader6()+ "\n\n\n");
                content[0].put("position", "center");
                current[0] = new JSONObject(content[0]);
                spos[0].put(current[0]);

                content[0] = new LinkedHashMap<>();
                content[0].put("content-type", "txt");
                content[0].put("size", "1");
                content[0].put("content", "DETECCION Y LEVANTAMIENTO ELECTRONICO DE INFRACCIONES A CONDUCTORES DE VEHICULOS QUE CONTRAVENGAN LAS DISPOSICIONES EN MATERIA DE TRANSITO, EQUILIBRIO ECOLOGICO, PROTECCIÓN AL AMBIENTE Y PARA LA PREVENCION Y CONTROL DE LA CONTAMINACION, ASI COMO PAGO DE SANCIONES Y APLICACION DE MEDIDAS DE SEGURIDAD.\n\nEL C. AGENTE QUE SUSCRIBE LA PRESENTE BOLETA DE INFRACCION, ESTA FACULTADO EN TERMINOS DE LOS QUE SE ESTABLECE EN LOS ARTICULOS 21 Y 115, FRACCION III, INCISO H), DE LA CONSTITUCION  POLITICA DE LOS ESTADOS UNIDOS MEXICANOS DE ACUERDO A LO ESTABLECIDO EN LOS ARTICULOS 8.3, 8.10, 8.18, 8.19 BIS, 8.19 TERCERO Y 8.19 CUARTO, DEL CODIGO ADMINISTRATIVO DEL ESTADO DE MEXICO. ASI COMO HACER CONSTAR LOS HECHOS QUE MOTIVAN LA INFRACCION EN TERMINOS DEL ARTICULO 16 DE NUESTRA CARTA MAGNA.\n\n\n");
                content[0].put("position", "left");
                current[0] = new JSONObject(content[0]);
                spos[0].put(current[0]);


                content[0] = new LinkedHashMap<>();
                content[0].put("content-type", "txt");
                content[0].put("size", "2");
                content[0].put("content", getSaveInfra.getFechahora() + "\n" + "FOLIO: " + getSaveInfra.getInfraFolio() + "\n\n\n" );
                content[0].put("position", "right");
                current[0] = new JSONObject(content[0]);
                spos[0].put(current[0]);

                if (getSaveInfra.getIsAusente() == 0){
                    currentsb[0] = new StringBuilder();

                    currentsb[0].append((getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno()).toUpperCase());

                    if (!getSaveInfra.getInfractor_rfc().equals(""))
                        currentsb[0].append("\n" + getSaveInfra.getInfractor_rfc());
                    if (!getSaveInfra.getInfractor_exterior().equals(""))
                        currentsb[0].append("\n" + getSaveInfra.getInfractor_exterior());
                    if (!getSaveInfra.getInfractor_interior().equals(""))
                        currentsb[0].append("\n" + getSaveInfra.getInfractor_interior());
                    if (!getSaveInfra.getInfractor_colonia().equals(""))
                        currentsb[0].append("\n" + getSaveInfra.getInfractor_colonia());

                    currentsb[0].append("\n\n");

                    content[0] = new LinkedHashMap<>();
                    content[0].put("content-type", "txt");
                    content[0].put("size", "2");
                    content[0].put("content", currentsb[0].toString());
                    content[0].put("position", "center");
                    current[0] = new JSONObject(content[0]);
                    spos[0].put(current[0]);

                    currentsb[0] = new StringBuilder();

                    if (!getSaveInfra.getTipolic_num().equals(""))
                        currentsb[0].append("\n" + "LICENCIA/PERMISO: " + getSaveInfra.getTipolic_num());
                    if (getSaveInfra.getTipolic_tipo() != 0)
                        currentsb[0].append("\n" + "TIPO LICENCIA: " + getSaveInfra.getTipolic_tipotext());
                    if (getSaveInfra.getTipolic_expedida() != 0)
                        currentsb[0].append("\n" + "EXPEDIDA: " + getSaveInfra.getTipolic_expedidatext());

                    currentsb[0].append("\n\n");

                    content[0] = new LinkedHashMap<>();
                    content[0].put("content-type", "txt");
                    content[0].put("size", "1");
                    content[0].put("content", currentsb[0].toString());
                    content[0].put("position", "left");
                    current[0] = new JSONObject(content[0]);
                    spos[0].put(current[0]);

                }

                if (getSaveInfra.getIsAusente() == 2){
                    content[0] = new LinkedHashMap<>();
                    content[0].put("content-type", "txt");
                    content[0].put("size", "2");
                    content[0].put("content", (getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno()).toUpperCase() + "\n\n");
                    content[0].put("position", "center");
                    current[0] = new JSONObject(content[0]);
                    spos[0].put(current[0]);
                }


                currentsb[0] = new StringBuilder();

                currentsb[0].append("\n" + "CARACTERISTICAS DEL VEHICULO: ");
                currentsb[0].append("\n" + "MARCA: " + getSaveInfra.getMarcatext());
                if (getSaveInfra.getSubmarca() != 0){
                    currentsb[0].append("\n" + "SUBMARCA: " + getSaveInfra.getSubmarcatext());
                } else {
                    currentsb[0].append("\n" + "SUBMARCA: " + getSaveInfra.getSubmarca_otra());
                }

                currentsb[0].append("\n" + "TIPO: " + getSaveInfra.getTipotext());
                currentsb[0].append("\n" + "COLOR: " + getSaveInfra.getColortext());
                currentsb[0].append("\n" + "MODELO: " + getSaveInfra.getAno());
                currentsb[0].append("\n" + "IDENTIFICADOR: " + getSaveInfra.getIdentificaciontext());
                currentsb[0].append("\n" + "NUMERO: " + getSaveInfra.getIdentificacion_num());
                currentsb[0].append("\n" + "AUTORIDAD QUE EXPIDE: " + getSaveInfra.getTipo_doctext());
                currentsb[0].append("\n" + "EXPEDIDO: " + getSaveInfra.getExpedido_entext() + "\n");

                currentsb[0].append("\n" + "ARTICULOS DEL REGLAMENTO DE TRANSITO DEL ESTADO DE MEXICO:" );

                content[0] = new LinkedHashMap<>();
                content[0].put("content-type", "txt");
                content[0].put("size", "1");
                content[0].put("content",  currentsb[0].toString());
                content[0].put("position", "left");
                current[0] = new JSONObject(content[0]);
                spos[0].put(current[0]);


                currentsb[0] = new StringBuilder();

                currentsb[0].append("\n\n" + "ARTICULO/FRACCION      U.M.A.     PUNTOS");
                currentsb[0].append("\n" + "*******************");

                content[0] = new LinkedHashMap<>();
                content[0].put("content-type", "txt");
                content[0].put("size", "1");
                content[0].put("content", currentsb[0].toString());
                content[0].put("position", "center");
                current[0] = new JSONObject(content[0]);
                spos[0].put(current[0]);


                ArrayList<ArtFraccion> list = getSaveInfra.getArticulos();
                ArtFraccion artFraccion;

                for (int i=0; i < list.size(); i++){
                    artFraccion = list.get(i);
                    content[0] = new LinkedHashMap<>();
                    content[0].put("content-type", "txt");
                    content[0].put("size", "2");
                    content[0].put("content", artFraccion.getArticulo() + "/" + artFraccion.getFraccion() + "      "  + artFraccion.getSalarios() + "      " + artFraccion.getPuntos());
                    content[0].put("position", "center");
                    current[0] = new JSONObject(content[0]);
                    spos[0].put(current[0]);
                }


                currentsb[0] = new StringBuilder();

                currentsb[0].append("\n\n" + "CONDUCTA QUE MOTIVA LA INFRACCION:");
                currentsb[0].append("\n" + getSaveInfra.getMotivacion() + "\n");



                currentsb[0].append("\n" + "CALLE:  " + getSaveInfra.getInfradir_calle());
                currentsb[0].append("\n" + "ENTRE: " + getSaveInfra.getInfradir_entre());
                currentsb[0].append("\n" + "Y: " + getSaveInfra.getInfradir_y());
                currentsb[0].append("\n" + "COLONIA: " + getSaveInfra.getInfradir_colonia() + "\n");


                currentsb[0].append("\n" + "DOCUMENTO QUE SE RETIENE:");

                if (getSaveInfra.getSe_retiene() == 0){
                    currentsb[0].append("\n" + "NINGUNO" + "\n");
                } else {
                    currentsb[0].append("\n" + getSaveInfra.getSe_retienetext() + "\n");
                }


                if (getSaveInfra.getIsRemision() == 1){
                    currentsb[0].append("\n" + "REMISION DEL VEHICULO: SI");
                    currentsb[0].append("\n" + getSaveInfra.getDisposicion_text()+ "\n\n");
                }



                content[0] = new LinkedHashMap<>();
                content[0].put("content-type", "txt");
                content[0].put("size", "1");
                content[0].put("content", currentsb[0].toString());
                content[0].put("position", "left");
                current[0] = new JSONObject(content[0]);
                spos[0].put(current[0]);


                currentsb[0] = new StringBuilder();
                currentsb[0].append("\n" + "RESPONSABLE DEL VEHICULO:"+ "\n");
                if (getSaveInfra.getIsAusente() == 0 || getSaveInfra.getIsAusente() == 2) {
                    currentsb[0].append("\n" + getSaveInfra.getInfractor_nombre() + " " + getSaveInfra.getInfractor_paterno() + " " + getSaveInfra.getInfractor_materno() + "\n");
                } else {
                    currentsb[0].append("\n" + "Q R R" + "\n");
                }

                currentsb[0].append("\n" + "RECIBO DE CONFORMIDAD"+ "\n\n\n\n");
                currentsb[0].append("\n" + "FIRMA" + "\n");
                currentsb[0].append("\n" + "AGENTE:");
                currentsb[0].append("\n" + getSaveInfra.getOficialcaptura()+ "\n\n");
                currentsb[0].append("\n" + "EMPLEADO: " + getSaveInfra.getOficialnum() + "\n\n\n");

                content[0] = new LinkedHashMap<>();
                content[0].put("content-type", "txt");
                content[0].put("size", "2");
                content[0].put("content", currentsb[0].toString());
                content[0].put("position", "center");
                current[0] = new JSONObject(content[0]);
                spos[0].put(current[0]);

                currentsb[0] = new StringBuilder();
                currentsb[0].append("\n" + "        FIRMA");
                content[0] = new LinkedHashMap<>();
                content[0].put("content-type", "txt");
                content[0].put("size", "2");
                content[0].put("content", currentsb[0].toString());
                content[0].put("position", "left");
                current[0] = new JSONObject(content[0]);
                spos[0].put(current[0]);
                objectprint[0].put("spos", spos[0]);
                ServiceManager.getInstence().getPrinter().print(objectprint[0].toString(), null, new OnPrinterListener() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onFinish() {
                        objectprint[0] = new JSONObject();
                        spos[0] = new JSONArray();

                        content[0] = new LinkedHashMap<>();
                        content[0].put("content-type", "one-dimension");
                        content[0].put("size", "3");
                        content[0].put("height", "2");
                        content[0].put("content", getSaveInfra.getLinea_uno());
                        content[0].put("position", "center");
                        current[0] = new JSONObject(content[0]);
                        spos[0].put(current[0]);

                        currentsb[0] = new StringBuilder();
                        currentsb[0].append("\n" + getSaveInfra.getLinea_uno()+ "\n");
                        currentsb[0].append("\n" + "CON 70 % DE DESCUENTO");
                        currentsb[0].append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_uno());
                        currentsb[0].append("\n" + "IMPORTE: " + String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_uno())) + "\n");

                        content[0] = new LinkedHashMap<>();
                        content[0].put("content-type", "txt");
                        content[0].put("size", "2");
                        content[0].put("content", currentsb[0].toString());
                        content[0].put("position", "center");
                        current[0] = new JSONObject(content[0]);
                        spos[0].put(current[0]);

                        try {
                            objectprint[0].put("spos", spos[0]);
                            ServiceManager.getInstence().getPrinter().print(objectprint[0].toString(), null, new OnPrinterListener() {
                                @Override
                                public void onError(int i, String s) {

                                }

                                @Override
                                public void onFinish() {
                                    objectprint[0] = new JSONObject();
                                    spos[0] = new JSONArray();
                                    content[0] = new LinkedHashMap<>();
                                    content[0].put("content-type", "one-dimension");
                                    content[0].put("size", "3");
                                    content[0].put("height", "2");
                                    content[0].put("content", getSaveInfra.getLinea_dos());
                                    content[0].put("position", "center");
                                    current[0] = new JSONObject(content[0]);
                                    spos[0].put(current[0]);

                                    currentsb[0] = new StringBuilder();
                                    currentsb[0].append("\n" + getSaveInfra.getLinea_dos()+ "\n");
                                    currentsb[0].append("\n" + "CON 50% DE DESCUENTO");
                                    currentsb[0].append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_dos());
                                    currentsb[0].append("\n" + "IMPORTE: " + String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_dos()))+ "\n");

                                    content[0] = new LinkedHashMap<>();
                                    content[0].put("content-type", "txt");
                                    content[0].put("size", "2");
                                    content[0].put("content", currentsb[0].toString());
                                    content[0].put("position", "center");
                                    current[0] = new JSONObject(content[0]);
                                    spos[0].put(current[0]);

                                    try {
                                        objectprint[0].put("spos", spos[0]);
                                        ServiceManager.getInstence().getPrinter().print(objectprint[0].toString(), null, new OnPrinterListener() {
                                            @Override
                                            public void onError(int i, String s) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                objectprint[0] = new JSONObject();
                                                spos[0] = new JSONArray();
                                                content[0] = new LinkedHashMap<>();
                                                content[0].put("content-type", "one-dimension");
                                                content[0].put("size", "3");
                                                content[0].put("height", "2");
                                                content[0].put("content", getSaveInfra.getLinea_tres());
                                                content[0].put("position", "center");
                                                current[0] = new JSONObject(content[0]);
                                                spos[0].put(current[0]);

                                                currentsb[0] = new StringBuilder();
                                                currentsb[0].append("\n" + getSaveInfra.getLinea_tres()+ "\n");
                                                currentsb[0].append("\n" + "SIN DESCUENTO");
                                                currentsb[0].append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_tres());
                                                currentsb[0].append("\n" + "IMPORTE: " + String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_tres())) + "\n");

                                                content[0] = new LinkedHashMap<>();
                                                content[0].put("content-type", "txt");
                                                content[0].put("size", "2");
                                                content[0].put("content", currentsb[0].toString());
                                                content[0].put("position", "center");
                                                current[0] = new JSONObject(content[0]);
                                                spos[0].put(current[0]);

                                                try {
                                                    objectprint[0].put("spos", spos[0]);
                                                    ServiceManager.getInstence().getPrinter().print(objectprint[0].toString(), null, new OnPrinterListener() {
                                                        @Override
                                                        public void onError(int i, String s) {

                                                        }

                                                        @Override
                                                        public void onFinish() {
                                                            // INICIA OXXO
                                                            objectprint[0] = new JSONObject();
                                                            spos[0] = new JSONArray();
                                                            currentsb[0] = new StringBuilder();
                                                            currentsb[0].append("\n" + "PAGO EN OXXO");

                                                            content[0] = new LinkedHashMap<>();
                                                            content[0].put("content-type", "txt");
                                                            content[0].put("size", "3");
                                                            content[0].put("content", currentsb[0].toString());
                                                            content[0].put("position", "center");
                                                            current[0] = new JSONObject(content[0]);
                                                            spos[0].put(current[0]);


                                                            content[0] = new LinkedHashMap<>();
                                                            content[0].put("content-type", "one-dimension");
                                                            content[0].put("size", "3");
                                                            content[0].put("height", "2");
                                                            content[0].put("content", first);
                                                            content[0].put("position", "center");
                                                            current[0] = new JSONObject(content[0]);
                                                            spos[0].put(current[0]);

                                                            currentsb[0] = new StringBuilder();
                                                            currentsb[0].append("\n" + first + "\n");
                                                            currentsb[0].append("\n" + "CON 70 % DE DESCUENTO");
                                                            currentsb[0].append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_uno());
                                                            currentsb[0].append("\n" + "IMPORTE: " + String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_uno()))+ "\n");

                                                            content[0] = new LinkedHashMap<>();
                                                            content[0].put("content-type", "txt");
                                                            content[0].put("size", "2");
                                                            content[0].put("content", currentsb[0].toString());
                                                            content[0].put("position", "center");
                                                            current[0] = new JSONObject(content[0]);
                                                            spos[0].put(current[0]);

                                                            try {
                                                                objectprint[0].put("spos", spos[0]);

                                                                ServiceManager.getInstence().getPrinter().print(objectprint[0].toString(), null, new OnPrinterListener() {
                                                                    @Override
                                                                    public void onError(int i, String s) {

                                                                    }

                                                                    @Override
                                                                    public void onFinish() {

                                                                        objectprint[0] = new JSONObject();
                                                                        spos[0] = new JSONArray();
                                                                        content[0] = new LinkedHashMap<>();
                                                                        content[0].put("content-type", "one-dimension");
                                                                        content[0].put("size", "3");
                                                                        content[0].put("height", "2");
                                                                        content[0].put("content", second);
                                                                        content[0].put("position", "center");
                                                                        current[0] = new JSONObject(content[0]);
                                                                        spos[0].put(current[0]);

                                                                        currentsb[0] = new StringBuilder();
                                                                        currentsb[0].append("\n" + second+ "\n");
                                                                        currentsb[0].append("\n" + "CON 50% DE DESCUENTO");
                                                                        currentsb[0].append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_dos());
                                                                        currentsb[0].append("\n" + "IMPORTE: " + String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_dos()))+ "\n");

                                                                        content[0] = new LinkedHashMap<>();
                                                                        content[0].put("content-type", "txt");
                                                                        content[0].put("size", "2");
                                                                        content[0].put("content", currentsb[0].toString());
                                                                        content[0].put("position", "center");
                                                                        current[0] = new JSONObject(content[0]);
                                                                        spos[0].put(current[0]);

                                                                        try {
                                                                            objectprint[0].put("spos", spos[0]);

                                                                            ServiceManager.getInstence().getPrinter().print(objectprint[0].toString(), null, new OnPrinterListener() {
                                                                                @Override
                                                                                public void onError(int i, String s) {

                                                                                }

                                                                                @SuppressLint("DefaultLocale")
                                                                                @Override
                                                                                public void onFinish() {
                                                                                    objectprint[0] = new JSONObject();
                                                                                    spos[0] = new JSONArray();
                                                                                    content[0] = new LinkedHashMap<>();
                                                                                    content[0].put("content-type", "one-dimension");
                                                                                    content[0].put("size", "3");
                                                                                    content[0].put("height", "2");
                                                                                    content[0].put("content", third);
                                                                                    content[0].put("position", "center");
                                                                                    current[0] = new JSONObject(content[0]);
                                                                                    spos[0].put(current[0]);

                                                                                    currentsb[0] = new StringBuilder();
                                                                                    currentsb[0].append("\n" + third + "\n");
                                                                                    currentsb[0].append("\n" + "SIN DESCUENTO");
                                                                                    currentsb[0].append("\n" + "VIGENCIA: " + getSaveInfra.getFecha_tres());
                                                                                    currentsb[0].append("\n" + "IMPORTE: " + String.format("%.2f", Float.parseFloat(getSaveInfra.getImporte_tres())) + "\n");

                                                                                    content[0] = new LinkedHashMap<>();
                                                                                    content[0].put("content-type", "txt");
                                                                                    content[0].put("size", "2");
                                                                                    content[0].put("content", currentsb[0].toString());
                                                                                    content[0].put("position", "center");
                                                                                    current[0] = new JSONObject(content[0]);
                                                                                    spos[0].put(current[0]);

                                                                                    currentsb[0] = new StringBuilder();

                                                                                    currentsb[0].append("\n" + pHelper.getFooter1());
                                                                                    currentsb[0].append("\n" + pHelper.getFooter2());
                                                                                    currentsb[0].append("\n" + pHelper.getFooter3() + "\n");
                                                                                    //currentsb[0].append("\n" + "Los datos personales proporcionados en este documento, están protegidos por la Ley de Proteccion de Datos Personales en Posesión de Sujetos Obligados del Estado de México y Municipios. El aviso de Privacidad Integral correspondiente puede ser consultado en su formáto vigente en la página electrónica\nhttps://www.atizapan.gob.mx/transparencia/avisos-privacidad" + "\n");
                                                                                    currentsb[0].append("\n" + "-AVISO DE PRIVACIDAD-" + "\n\n");
                                                                                    currentsb[0].append("\n" + "FUENTE DE CAPTURA: SIIP" + "\n\n");

                                                                                    content[0] = new LinkedHashMap<>();
                                                                                    content[0].put("content-type", "txt");
                                                                                    content[0].put("size", "1");
                                                                                    content[0].put("content", currentsb[0].toString());
                                                                                    content[0].put("position", "center");
                                                                                    current[0] = new JSONObject(content[0]);
                                                                                    spos[0].put(current[0]);
                                                                                    try {
                                                                                        objectprint[0].put("spos", spos[0]);

                                                                                        ServiceManager.getInstence().getPrinter().print(objectprint[0].toString(), null, null);
                                                                                    }catch (Exception e){
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onStart() {

                                                                                }
                                                                            });
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }


                                                                    }

                                                                    @Override
                                                                    public void onStart() {

                                                                    }
                                                                });
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }


                                                        }

                                                        @Override
                                                        public void onStart() {

                                                        }
                                                    });
                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                            }

                                            @Override
                                            public void onStart() {

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onStart() {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onStart() {

                    }
                });






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
