package mx.qsistemas.infracciones.Functions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import mx.qsistemas.infracciones.DataManagement.Const;
import mx.qsistemas.infracciones.DataManagement.PreferenceHelper;

/**
 * Developed by ingmtz on 12/2/16.
 */

public class NetworkCheck extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        PreferenceHelper pHelper = new PreferenceHelper(context);

        if (wifi.isConnected() || mobile.isConnected()) {
            Intent msgIntent = new Intent(context, SyncProcess.class);
            if (pHelper.getIsSearch() == null){
                context.startService(msgIntent);
            }
            Log.d(Const.DebugType.INTERNET, "Internet connected");
        }
    }
}
