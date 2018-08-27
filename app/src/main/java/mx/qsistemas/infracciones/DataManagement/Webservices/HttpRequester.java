package mx.qsistemas.infracciones.DataManagement.Webservices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import junit.runner.BaseTestRunner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import mx.qsistemas.infracciones.AsyncTaskCompleteListener;
import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Database.DBHelper;
import mx.qsistemas.infracciones.DataManagement.Models.SearchTerms;

/**
 * Created by ingmtz on 8/12/16.
 */
public class HttpRequester {

    private AsyncTaskCompleteListener mAsyncListener;
    public AsyncHttpRequest request;
    public Context context;
    private DBHelper dbHelper;
    private String url;
    private byte[] postDataBytes;
    private int typedata;
    private int serviceCode;
    SearchTerms params;

    public HttpRequester(Context context, int typedata, AsyncTaskCompleteListener mAsyncListener, int serviceCode, String url, SearchTerms params){
        this.context = context;
        this.typedata = typedata;
        this.mAsyncListener = mAsyncListener;
        this.serviceCode = serviceCode;
        this.params = params;
        this.url = url;

        dbHelper = new DBHelper(context);

        request = (AsyncHttpRequest) new AsyncHttpRequest().execute();
    }


    private class AsyncHttpRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            String reader = null;
            StringBuilder postData;
            HashMap<String, String> map;
            JSONObject object;
            ArrayList<JSONObject> list;
            StringBuilder readers = new StringBuilder();
            ParseContent pContent;

            switch (typedata){
                case Const.Typedata.CATALOGS:
                    String syncdate = dbHelper.getLastSync(context);
                    postData = new StringBuilder();
                    postData.append("FechaSincronizacion=");
                    postData.append(syncdate);
                    reader = doService(postData.toString(), "/LoginInicial.asmx/Envio_Catalogos_Infracciones");
                    break;
                case Const.Typedata.IMAGESYNC:

                    Base64Helper helper = new Base64Helper(context);
                    try {
                        list = helper.imageSyncRecursive();
                        String separator = "";
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                object = list.get(i);
                                String jobject = object.toString().replace("+", "%2B");
                                jobject = jobject.replace("\\", "%5C");
                                postData = new StringBuilder();
                                postData.append("Json=");
                                postData.append(jobject);
                                reader = doService(postData.toString(), "/LoginInicial.asmx/Receptor_Imagenes");
                                readers.append(separator);
                                readers.append(reader);
                                separator = ";";
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (reader == null){
                        readers.append("{\"Flag\":false, \"Mensaje\":\"No hay datos que sincronizar\", \"Ids\":\"null\"}");
                    }

                    pContent = new ParseContent(context);
                    try {
                        reader = pContent.unifyReaders(readers.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        reader = "";
                    }

                    break;
                case Const.Typedata.ONLINESEARCH:
                    map = new HashMap<>();
                    map.put("username","InfraMobile");
                    map.put("password","CF2E3EF25C90EB567243ADFACD4AA868");
                    if(params.getFolio() != null && !params.getFolio().equals("")){
                        map.put("Folio", params.getFolio());
                    } else {
                        map.put("Folio", "");
                    }
                    if (params.getIdentifica() !=  null && !params.getIdentifica().equals("0")){
                        map.put("IdDocumentoIdentificador", params.getIdentifica());
                        map.put("NumeroDocumentoIdentificador", params.getNumidentifica());
                    } else {
                        map.put("IdDocumentoIdentificador", "0");
                        map.put("NumeroDocumentoIdentificador", "");
                    }
                    object = new JSONObject(map);
                    postData = new StringBuilder();
                    postData.append("Json=");
                    postData.append(object);
                    reader = doService(postData.toString(), "/LoginInicial.asmx/Envio_Resultados_Previos_Busqueda_Infracciones");
                    break;
                case Const.Typedata.ONLINEITEM:
                    map = new HashMap<>();
                    map.put("username","InfraMobile");
                    map.put("password","CF2E3EF25C90EB567243ADFACD4AA868");
                    map.put("IdInfraccion",params.getIdinfra());
                    object = new JSONObject(map);
                    postData = new StringBuilder();
                    postData.append("Json=");
                    postData.append(object);
                    reader = doService(postData.toString(), "/LoginInicial.asmx/Envio_Resultado_Busqueda_Infraccion");
                    break;
            }

        return reader;

        }

        @Override
        protected void onPostExecute(String response) {
            if (mAsyncListener != null){
                try {
                    mAsyncListener.onTaskCompleted(response, serviceCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String doService(String postData, String service) {
        String reader;
        try {
            postDataBytes = postData.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Reader in;
            URL route = new URL(url + service);
            HttpURLConnection conn = (HttpURLConnection) route.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            Log.d(Const.DebugType.SYNC, "Sync SENT: " + postData);
            Log.d(Const.DebugType.SYNC, "Sync Started");
            conn.getOutputStream().write(postDataBytes);
            Log.d("INFRACCIONES",String.valueOf(conn.getResponseCode() + " " + conn.getErrorStream()));

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            if (in.read() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0; )
                    sb.append((char) c);

                in.close();
                conn.disconnect();

                reader = sb.toString();
                reader = reader.substring(reader.indexOf("{"), reader.lastIndexOf("}") + 1);

                Log.d(Const.DebugType.SYNC, "Sync End");
                Log.d(Const.DebugType.SYNC, "Sync DATA RECEIVED: " + reader);

            } else {
                reader = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Const.DebugType.SERVICE, "Try Incomplete ");
            reader = "";
        }

        return reader;
    }
}
