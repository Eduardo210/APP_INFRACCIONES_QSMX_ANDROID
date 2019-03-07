package mx.qsistemas.infracciones.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import mx.qsistemas.infracciones.Activities.LoginActivity;
import mx.qsistemas.infracciones.Activities.SearchActivity;
import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;
import mx.qsistemas.infracciones.OnProgressCancelListener;
import mx.qsistemas.infracciones.OnVolleyResponse;
import mx.qsistemas.infracciones.R;

/**
 * Developed by ingmtz on 10/21/16.
 */

@SuppressWarnings("ALL")
public class Utils {

    private static Dialog customdialog;
    private static OnProgressCancelListener progressCancelListener;

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }


    public static void showToast(String msg, Context ctx) {
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }

    public static void setMobileDataState(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
        try {
            setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static final SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    }

    public static void showCustomProgressDialog(Context context, boolean iscancelable, OnProgressCancelListener progressCancelListener) {
        if (customdialog != null && customdialog.isShowing())
            return;
        Utils.progressCancelListener = progressCancelListener;

        customdialog = new Dialog(context, R.style.MyDialog);
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customdialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customdialog.setContentView(R.layout.custom_full_dialog);

        Button btnCancel = (Button) customdialog.findViewById(R.id.btnDialogButton);

        if (Utils.progressCancelListener == null) {
            btnCancel.setVisibility(View.GONE);
        } else {
            btnCancel.setVisibility(View.VISIBLE);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utils.progressCancelListener != null) {
                    Utils.progressCancelListener.onProgressCancel();
                }
            }
        });
        customdialog.setCancelable(iscancelable);
        customdialog.show();

    }

    public static void removeCustomProgressDialog() {
        try {
            Utils.progressCancelListener = null;
            if (customdialog != null) {
                customdialog.dismiss();
                customdialog = null;
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String generateOxxoBarcode(String fechaVigencia, String idInfraccion, String monto, Context context) {
        PreferenceHelper pHelper = new PreferenceHelper(context);
        String reference = generateReference(idInfraccion.toUpperCase(), pHelper.getIDMunicipio());
        String preCode = Const.OXXO_IDENTIFICADOR + reference + fechaVigencia + StringUtils.leftPad(monto, 7, "0");
        String verificador = generateVerificador(preCode);
        return preCode + verificador;
    }

    private static String generateVerificador(String preCode) {

        int curr = 0;
        int[] code = new int[Const.OXXO_LENGTH - 1];
        char[] pchar = preCode.toCharArray();
        int counter = 1;

        for (int i = 0; i < pchar.length; i++){
            curr = Integer.valueOf(Character.toString(pchar[i]));
            code[i] = curr * counter;
            if (counter == 9){
                counter = 1;
            } else {
                counter ++;
            }
        }

        int sum = 0;

        for (int cu: code) {
            sum += cu;
        }

        String sumfull = String.valueOf(sum);

        return  sumfull.substring(sumfull.length() - 1);

    }

    @SuppressLint("DefaultLocale")
    private static String generateReference(String idInfraccion, String idMunicipio) {


        StringBuilder sb = new StringBuilder();
        String current = "";
        StringBuilder infraid = new StringBuilder();

        sb.append(StringUtils.leftPad(idMunicipio, 3, "0"));

        for (char ch: idInfraccion.toCharArray()){
            switch (Character.toString(ch)) {
                case "A": case "B": case "C":
                    current = "2";
                    break;
                case "D": case "E": case "F":
                    current = "3";
                    break;
                case "G": case "H": case "I":
                    current = "4";
                    break;
                case "J": case "K": case "L":
                    current = "5";
                    break;
                case "M": case "N": case "O":
                    current = "6";
                    break;
                case "P": case "Q": case "R":
                    current = "7";
                    break;
                case "S": case "T": case "U":
                    current = "8";
                    break;
                case "V": case "W": case "X":
                    current = "9";
                    break;
                case "Y": case "Z":
                    current = "0";
                    break;
                default:
                    if (!Utils.isInteger(Character.toString(ch), 10)) {
                        current = "";
                    } else {
                        current = Character.toString(ch);
                    }

                    break;
            }

            if (!current.equals("")){
                infraid.append(current);
            }
        }

        sb.append(StringUtils.leftPad(infraid.toString(), 11, "0"));

        return sb.toString();
    }

    public static void showAlertDialog(final Activity context, String text, int drawable_ok, int drawable_no){

        final Intent[] i = new Intent[1];
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_exit);
        TextView tvCatDialTitle = (TextView) dialog.findViewById(R.id.tvCatDialTitle);
        ImageButton ibCatDialSave = (ImageButton) dialog.findViewById(R.id.ibExitDialSave);
        ibCatDialSave.setImageResource(drawable_no);
        ImageButton ibCatDialCancel = (ImageButton) dialog.findViewById(R.id.ibExitDialCancel);
        ibCatDialCancel.setImageResource(drawable_ok);
        tvCatDialTitle.setText(text);
        ibCatDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i[0] = new Intent().setClass(context, SearchActivity.class);
                i[0].putExtra("isOnline", true);
                context.startActivity(i[0]);
                context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                dialog.dismiss();
            }
        });
        ibCatDialSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i[0] = new Intent().setClass(context, SearchActivity.class);
                i[0].putExtra("isOnline", false);
                context.startActivity(i[0]);
                context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showAlertDialogOk(final Activity context, String message, String messagetwo, String hint, boolean hasedit, final int identifier){
        final Dialog dialog = new Dialog(context);
        final PreferenceHelper pHelper;
        final Intent[] i = new Intent[1];
        pHelper = new PreferenceHelper(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView tvCusDialMessageTwo = (TextView) dialog.findViewById(R.id.tvCusDialMessageTwo);
        if (messagetwo != null){
            tvCusDialMessageTwo.setVisibility(View.GONE);
        }

        final EditText etCusDialServ = (EditText) dialog.findViewById(R.id.etCusDialServ);
        if (!hasedit){
            etCusDialServ.setVisibility(View.GONE);
        } else {
            etCusDialServ.setHint(hint);
            switch (identifier){
                case 1:
                    etCusDialServ.setCompoundDrawablesWithIntrinsicBounds(R.drawable.et_world,0,0,0);
                    break;
            }
        }

        TextView tvCusDialMessage = (TextView) dialog.findViewById(R.id.tvCusDialMessage);
        tvCusDialMessage.setText(message);
        Button btnCusDialOK = (Button) dialog.findViewById(R.id.btnCusDialOK);
        ImageButton ibCusDialCancel = (ImageButton)  dialog.findViewById(R.id.ibCusDialCancel);

        btnCusDialOK.setText(context.getResources().getString(R.string.ok));
        btnCusDialOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (identifier){
                    case 0:
                        pHelper.logout();
                        i[0] = new Intent().setClass(context, LoginActivity.class);
                        i[0].setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i[0].setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dialog.dismiss();
                        context.startActivity(i[0]);
                        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        context.finish();
                        break;
                    case 1:
                        if (!etCusDialServ.getText().toString().isEmpty()){
                            pHelper.putServiceIP("http://" + etCusDialServ.getText().toString());
                            Utils.showToast(context.getResources().getString(R.string.service_added), context);
                            dialog.dismiss();
                        } else {
                            Utils.showToast(context.getResources().getString(R.string.input_a_ip), context);
                        }
                        break;
                    case 2:
                        if(!etCusDialServ.getText().toString().equals("")){
                            pHelper.putPrefix(etCusDialServ.getText().toString());
                            dialog.dismiss();
                        } else {
                            Utils.showToast("Inserta un prefijo en el cuadro de texto.", context);
                        }
                        break;
                    case 3:
                        pHelper.logout();
                        pHelper.putPrefix(null);
                        File dbpath = new File(Const.DATABASE_NAME);
                        dbpath.delete();
                        i[0] = new Intent().setClass(context, LoginActivity.class);
                        i[0].setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i[0].setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dialog.dismiss();
                        context.startActivity(i[0]);
                        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        context.finish();
                        break;
                }
            }
        });

        ibCusDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void volleyWS(final Context context, String url, final HashMap<String, String> map, RequestQueue postQueue, final Integer serviceCode, final OnVolleyResponse listener){
        try {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        listener.onServiceCompleted(object.getBoolean("SUCCESS"), object.getString("ERROR_MESSAGE"), object, serviceCode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onServiceCompleted(false, context.getString(R.string.server_error) + ": " + e.getMessage() , null, serviceCode);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errmess = "Error desconocido";
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        errmess = "Se supero el tiempo de espera al servidor";
                    } else if (error instanceof AuthFailureError) {
                        errmess = "Error de autenticación con el servidor";
                    } else if (error instanceof ServerError) {
                        errmess = "Error interno del servidor";
                    } else if (error instanceof NetworkError) {
                        errmess = "Se supero el tiempo de espera al servidor";
                    } else if (error instanceof ParseError) {
                        errmess = "Error al parsear la respuesta";
                    }
                    listener.onServiceCompleted(false, context.getString(R.string.server_error) + ": " + errmess , null, serviceCode);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return map;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    return super.parseNetworkResponse(response);
                }

            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            postQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
