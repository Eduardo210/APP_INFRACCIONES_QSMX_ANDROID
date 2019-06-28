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

        /* Generate Banking Capture Lines */
        fun generateCaptureLine(folio: String, expirationDate: String, import: String, constant: String): String {
            var captureLine = ""
            val firstPart = getIdCondensed(folio)
            val secondPart = getDateCondensed(expirationDate)
            val thirdPart = getImportCondensed(import)
            val fourthPart = getVerificationNumbers(firstPart + secondPart + thirdPart + constant)
            captureLine = firstPart + secondPart + thirdPart + constant + fourthPart
            for (i in captureLine!!.length..19) {
                captureLine = "0$captureLine"
            }
            return captureLine
        }

        private fun getIdCondensed(folio: String): String {
            val idInfraction = folio.toUpperCase()
            var c: String
            var idCondensed = ""
            for (i in 0 until idInfraction.length) {
                c = idInfraction.substring(i, i + 1)
                when (c) {
                    "A" -> idCondensed += "2"
                    "B" -> idCondensed += "2"
                    "C" -> idCondensed += "2"
                    "D" -> idCondensed += "3"
                    "E" -> idCondensed += "3"
                    "F" -> idCondensed += "3"
                    "G" -> idCondensed += "4"
                    "H" -> idCondensed += "4"
                    "I" -> idCondensed += "4"
                    "J" -> idCondensed += "5"
                    "K" -> idCondensed += "5"
                    "L" -> idCondensed += "5"
                    "M" -> idCondensed += "6"
                    "N" -> idCondensed += "6"
                    "O" -> idCondensed += "6"
                    "P" -> idCondensed += "7"
                    "Q" -> idCondensed += "7"
                    "R" -> idCondensed += "7"
                    "S" -> idCondensed += "8"
                    "T" -> idCondensed += "8"
                    "U" -> idCondensed += "8"
                    "V" -> idCondensed += "9"
                    "W" -> idCondensed += "9"
                    "X" -> idCondensed += "9"
                    "Y" -> idCondensed += "0"
                    "Z" -> idCondensed += "0"
                    else -> idCondensed += c
                }
            }
            return idCondensed
        }

        private fun getDateCondensed(date: String): String {
            val splitDate = date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var day = Integer.parseInt(splitDate[0])
            var month = Integer.parseInt(splitDate[1])
            var year = Integer.parseInt(splitDate[2])
            year = (year - 1988) * 372
            month = (month - 1) * 31
            day -= 1
            return (year + month + day).toString()
        }

        private fun getImportCondensed(import: String): String {
            val newImport = import.replace("0", "").replace(".", "").replace(",", "")
            var x = 0
            val array = IntArray(newImport.length)
            var sum = 0
            newImport.forEachIndexed { index, c ->
                var digit = c.toInt()
                when (x) {
                    0 -> {
                        digit *= 7
                        x += 1
                    }
                    1 -> {
                        digit *= 3
                        x += 1
                    }
                    2 -> x = 0
                }
                array[index] = digit
            }
            array.forEach { sum += it }
            return (sum % 10).toString()
        }

        private fun getVerificationNumbers(text: String): String {
            var x = 0
            val array = IntArray(text.length)
            var sum = 0
            text.forEachIndexed { index, c ->
                var digit = c.toInt()
                when (x) {
                    0 -> {
                        digit *= 11
                        x += 1
                    }
                    1 -> {
                        digit *= 13
                        x += 1
                    }
                    2 -> {
                        digit *= 17
                        x += 1
                    }
                    3 -> {
                        digit *= 19
                        x += 1
                    }
                    4 -> {
                        digit *= 23
                        x = 0
                    }
                }
                array[index] = digit
            }
            array.forEach { sum += it }
            return (sum % 97 + 1).toString()
        }
    }
}