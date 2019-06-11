package mx.qsistemas.incidencias.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings


class Validator {
    companion object {

        fun isValidFields(vararg fields: String): Boolean {
            for (field in fields) {
                if (field.isEmpty()) return false
            }
            return true
        }

        fun isNetworkEnable(context: Context): Boolean {
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
                return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                val networkCapabilities = connManager.activeNetworkInfo
                return networkCapabilities != null && networkCapabilities.isConnectedOrConnecting
            }
        }

        fun isConnectedToWifi(context: Context): Boolean {
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
                return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                        && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } else {
                val networkCapabilities = connManager.activeNetworkInfo
                return networkCapabilities != null && networkCapabilities.type == ConnectivityManager.TYPE_WIFI
            }
        }

        fun isHighAccuracyEnable(context: Context): Boolean {
            return Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE) == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY
        }

        fun isDateAutomatic(context: Context): Boolean {
            try {
                val opcion = Settings.System.getInt(context.contentResolver, Settings.Global.AUTO_TIME)
                return opcion == 1
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }
            return false
        }

        fun isTimeZoneAutomatic(context: Context): Boolean {
            try {
                val opcion1 = Settings.System.getInt(context.contentResolver, Settings.Global.AUTO_TIME_ZONE)
                return opcion1 == 1
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }
            return false
        }

        fun isMockLocationEnable(context: Context): Boolean {
            // returns true if mock location enabled, false if not enabled.
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION) != "0"
        }
    }
}