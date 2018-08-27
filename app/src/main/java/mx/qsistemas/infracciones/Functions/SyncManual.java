package mx.qsistemas.infracciones.Functions;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mx.qsistemas.infracciones.AsyncTaskCompleteListener;
import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.Database.DBHelper;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.DataManagement.Webservices.ParseContent;
import mx.qsistemas.infracciones.R;
import mx.qsistemas.infracciones.Utils.Utils;

/**
 * Created by ingmtz on 22/02/18.
 */

public class SyncManual {

    public DBHelper dbHelper;
    String url;
    PreferenceHelper pHelper;
    ParseContent pContent;
    byte[] postDataBytes;
    public int id;
    ArrayList<JSONObject> list;
    Context context;

    public SyncManual(Context context){
        this.context = context;
        new doSync().execute();
    }

    private class doSync extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            Utils.removeCustomProgressDialog();
            super.onPostExecute(integer);

        }

        @Override
        protected Integer doInBackground(String... strings) {
            dbHelper = new DBHelper(context);

            list = dbHelper.getSync(context);

            pHelper = new PreferenceHelper(context);
            pContent = new ParseContent(context);

            url = pHelper.getServiceIP();

            if (url != null) {

                NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

                if (list.size() != 0) {

                    mBuilder.setContentTitle("Qsistemas infracciones")
                            .setContentText("Sincronizacion en progreso")
                            .setSmallIcon(android.R.drawable.stat_notify_sync)
                            .setContentTitle("Sincronizando infracciones");
                    mBuilder.setProgress(0, 0, true);
                    mNotifyManager.notify(999, mBuilder.build());

                    postDataBytes = null;

                    JSONObject object = list.get(0);

                    StringBuilder postData = new StringBuilder();

                    postData.append("Json=");
                    postData.append(object.toString());
                    try {
                        postDataBytes = postData.toString().getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    try {
                        Reader in;
                        URL route = new URL(url + "/LoginInicial.asmx/Receptor_Infracciones_Moviles");
                        HttpURLConnection conn = (HttpURLConnection) route.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setDoOutput(true);
                        conn.setConnectTimeout(30000);
                        conn.setReadTimeout(30000);
                        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                        Log.d(Const.DebugType.SYNC, "Sync Started");
                        conn.getOutputStream().write(postDataBytes);

                        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        if (in.read() > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int c; (c = in.read()) >= 0; )
                                sb.append((char) c);

                            in.close();
                            conn.disconnect();

                            Log.d(Const.DebugType.SYNC, "Sync End");

                            String reader = sb.toString();
                            reader = reader.substring(reader.indexOf("{"), reader.lastIndexOf("}") + 1);

                            String[] ids_got;
                            String[] ids_sent;

                            JSONObject responseobject = new JSONObject(reader);
                            if (responseobject.getString("Flag").equals("true")) {
                                JSONArray responsearray = responseobject.getJSONArray("Ids");
                                if (responsearray.length() < 1) {
                                    ids_got = new String[0];
                                } else {
                                    ids_got = new String[responsearray.length()];
                                }
                                for (int i = 0; i < responsearray.length(); i++) {
                                    JSONObject ids = responsearray.getJSONObject(i);
                                    ids_got[i] = ids.toString();
                                }

                                JSONObject sentobject = list.get(1);
                                JSONArray sentarray = sentobject.getJSONArray("ids");
                                ids_sent = new String[sentarray.length()];
                                for (int i = 0; i < sentarray.length(); i++) {
                                    JSONObject ids = sentarray.getJSONObject(i);
                                    ids_sent[i] = ids.getString("id");
                                }

                                dbHelper.updateSync(ids_got, ids_sent);

                                mBuilder.setContentText("Sincronización completa")
                                        .setProgress(0, 0, false)
                                        .setSmallIcon(R.drawable.ic_infra_ok)
                                        .setContentTitle("Sincronización completa");
                                mNotifyManager.notify(999, mBuilder.build());

                                Thread.sleep(3000);
                                mNotifyManager.cancel(999);

                            } else {
                                Log.d(Const.DebugType.SERVICE, "Falló " + responseobject.getString("Mensaje"));
                                Log.d(Const.DebugType.SERVICE, "Try Incomplete");
                                mBuilder.setContentText("Sincronización falló, se hará otro intento")
                                        .setProgress(0, 0, false)
                                        .setSmallIcon(R.drawable.ic_infra_cancel)
                                        .setContentTitle("Sincronización falló, se hará otro intento");
                                mNotifyManager.notify(999, mBuilder.build());
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(Const.DebugType.SERVICE, "Try Incomplete");
                        mBuilder.setContentText("Se intentara de nuevo la sincronización")
                                .setProgress(0, 0, false)
                                .setSmallIcon(R.drawable.ic_infra_cancel)
                                .setContentTitle("Sincronización falló");
                        mNotifyManager.notify(999, mBuilder.build());
                    }
                } else {
                    Log.d(Const.DebugType.SERVICE, "No new data");
                    mBuilder.setContentText("Sincronización Finalizada")
                            .setProgress(0, 0, false)
                            .setSmallIcon(R.drawable.ic_infra_cancel)
                            .setContentTitle(context.getString(R.string.no_new_data));
                    mNotifyManager.notify(999, mBuilder.build());                }
            }

            return 0;
        }
    }

}
