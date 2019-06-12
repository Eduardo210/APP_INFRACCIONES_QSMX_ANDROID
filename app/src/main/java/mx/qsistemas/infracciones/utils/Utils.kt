package mx.qsistemas.infracciones.utils

import android.app.job.JobInfo
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*


class Utils {
    companion object {

        fun getTokenDevice(context: Context): String =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        fun getImeiDevice(context: Context): String {
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val imei: String?
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        telephonyManager.imei
                    } else {
                        telephonyManager.deviceId
                    }
                    return if (imei != null && imei.isNotEmpty()) {
                        imei
                    } else {
                        Build.SERIAL
                    }
                }
            } catch (e: Exception) {
                val errors = StringWriter()
                e.printStackTrace(PrintWriter(errors))
                return errors.toString()
            }
            return "not_found"
        }

        /**
         *  Generate folioIncidence for new incidence reports
         **/
        fun generateFolioCoDi(): String {
            val cal = Calendar.getInstance()
            cal.firstDayOfWeek = Calendar.MONDAY
            cal.timeInMillis = System.currentTimeMillis()
            val year = cal.get(Calendar.YEAR)
            val yearTwoDigits = cal.get(Calendar.YEAR) % 100
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minutes = cal.get(Calendar.MINUTE)
            val seconds = cal.get(Calendar.SECOND)
            val millisencond = cal.get(Calendar.MILLISECOND)
            var newId = 0
            newId = (newId shl 7) + yearTwoDigits
            newId = (newId shl 4) + month
            newId = (newId shl 5) + day
            var newIdHex = Integer.toHexString(newId)
            while (newIdHex.length < 4) {
                newIdHex = "0$newIdHex"
            }
            newId = 0
            newId = (newId shl 5) + hour
            newId = (newId shl 6) + minutes
            newId = (newId shl 6) + seconds
            newId = (newId shl 7) + millisencond
            var secondIdHex = Integer.toHexString(newId)
            while (secondIdHex.length < 6) {
                secondIdHex = "0$secondIdHex"
            }
            return newIdHex + secondIdHex
        }

        fun getOutputMediaFileUri(file: File): Uri {
            return Uri.fromFile(file)
        }

        fun getOutputMediaFile(type: Int): File? {
            // Check that the SDCard is mounted
            val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Infracciones")
            // Create the storage directory(MyCameraVideo) if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }
            // Create a media file name
            // For unique file name appending current timeStamp with file name
            val date = java.util.Date()
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(date.time)
            val mediaFile: File
            mediaFile = when (type) {
                MEDIA_TYPE_VIDEO -> File(mediaStorageDir.path + File.separator + "VID_" + timeStamp + ".mp4")
                MEDIA_TYPE_IMAGE -> File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
                else -> return null
            }
            return mediaFile
        }

        /**
         * Get Bitmap from Vector Image
         */
        fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
            val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
            vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
            val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        /**
         * Get IP address from first non-localhost interface
         * @param useIPv4   true=return ipv4, false=return ipv6
         * @return  address or empty string
         */
        fun getIPAddress(useIPv4: Boolean): String {
            try {
                val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs = Collections.list(intf.inetAddresses)
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress) {
                            val sAddr = addr.hostAddress
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            val isIPv4 = sAddr.indexOf(':') < 0
                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr
                            } else {
                                if (!isIPv4) {
                                    val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                    return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                            0, delim).toUpperCase()
                                }
                            }
                        }
                    }
                }
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
            return ""
        }

        /* Job Builder */
        fun createJob(idJob: Int, timeRepeat: Long, serviceComponent: ComponentName): JobInfo.Builder {
            val builder = JobInfo.Builder(idJob, serviceComponent)
            builder.setPeriodic(timeRepeat)
            builder.setPersisted(true)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            return builder
        }
    }
}