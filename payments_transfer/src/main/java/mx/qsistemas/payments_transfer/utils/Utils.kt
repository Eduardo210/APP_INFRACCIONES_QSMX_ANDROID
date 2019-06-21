package mx.qsistemas.payments_transfer.utils

import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import mx.qsistemas.payments_transfer.BuildConfig
import mx.qsistemas.payments_transfer.dtos.Info_Cuenta
import mx.qsistemas.payments_transfer.dtos.ValidacionCuentasDecryp_Data
import okhttp3.Headers
import org.apache.commons.codec.binary.Hex
import org.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.zip.CRC32
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.HashMap
import kotlin.experimental.xor


class Utils {
    companion object {
        /** El método se emplea para poder validar que el contenido del QR realmente pertenezca al formato
         * correspondiente a un Mensaje de Cobro emitido para el metodo de pago CoDi. */
        fun isValidCoDi(content: String): Boolean {
            return (content.contains("TYP") && content.contains("IDC") && content.contains("DEV") && content.contains("ic")
                    && content.contains("SER") && content.contains("ENC") && content.contains("CRY"))
        }

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

        fun mapHeaders(headers: Headers): String {
            /* Cast headers to HashMap and after convert it to JSON */
            val headersMap = HashMap<String, String>()
            for (i in 0 until headers.size()) {
                headersMap[headers.name(i)] = headers.value(i)
            }
            /* Parse response headers to response body in json format */
            return JSONObject(headersMap.toMap()).toString()
        }

        /** La llave de paso es un valor de 16 bytes (32 caracteres) geerado por el Pinpad, cada ve que realice
         *  la carga de llave, y funciona para cifrar la llave que se entregará en la respuesta del comando. La
         *  manera en que el Pinpad genere la llace de paso es libre, pero se recomienda utiliazr algún algoritmo
         *  adecuado que permita garantizar la seguridad de la llave definitiva que se cargará al Pinpad, también
         *  debe ser compatible con AES 266 que es el que utiliza Banorte para el descifrado de la llave.*/
        fun generateGatekeeper(tokenDevice: String): String {
            val appName = BuildConfig.APPLICATION_ID
            val time = System.nanoTime()
            val input = "$tokenDevice-$appName-$time"
            try {
                val md = MessageDigest.getInstance("SHA-512")
                val messageDigest = md.digest(input.toByteArray())
                val no = BigInteger(1, messageDigest)
                var hashtext = no.toString(16)
                while (hashtext.length < 32) {
                    hashtext = "0$hashtext"
                }
                return hashtext.substring(0, 32).toUpperCase()
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            }
        }

        /** El número de serie será el que tenga el Pinpad asignado, con longitud máxima de 16 bytes. En caso
         *  de que el pinpad tenga un número de serie de longitud mayor podrá truncarse a conveniencia del cliente.
         *  En el caso de que el número de serie del pinpad tenga una longitud menor deberá justificarse a la
         *  izquierda y se rellenará con espacios en blanco. Este número de serie deberá ser expresado en código
         *  ASCII (si incluye letras y otros caracteres también se deben colocar en código ASCII correspondiente). */
        fun getNoSerie(): String {
            val uuid = "88888888"
            var hexString = String(Hex.encodeHex(uuid.toByteArray(Charset.forName("UTF-8"))))
            return if (hexString.length >= 32) {
                hexString.substring(0, 32)
            } else {
                val positions = (32 - hexString.length) / 2
                for (i in 0 until positions) {
                    hexString += "20"
                }
                hexString
            }
        }

        /** El CRC (Cyclic Redundancy Check) es un valor de 4 bytes (32bits) que deberá ser calculado sobre la
         *  llave de paso únicamente (no incluir el número de serie).  Al recibir el selector como variable de
         *  entrada en el comando de solicitud de llave Payworks 2.0 realizará la verificación del CRC contra
         *  los Bytes recibidos en la llave de paso; en caso de una inconsistencia el comando fallará. */
        fun generateCrc(input: ByteArray): String {
            val checksum = CRC32()
            // update the current checksum with the specified array of bytes
            checksum.update(input)
            return String.format("%08X", checksum.value)
        }

        /** El AES CBC PKCS es un algoritmo de cifrado necesario para obtener ciertos datos de
         * cadenas cifradas en el QR de CoDi así como para poder cifrar los mismos datos para poder
         * desplegarlos posteriormente en el celular */
        fun AesCbcPkcs(key: String, initVector: String, value: String, mode: Int, transformation: String): String? {
            var result: String?
            try {
                val iv = IvParameterSpec(Hex.decodeHex(initVector.toCharArray()))
                val skeySpec = SecretKeySpec(Hex.decodeHex(key.toCharArray()), "AES")

                val cipher = Cipher.getInstance(/*"AES/CBC/PKCS5PADDING"*/transformation)
                cipher.init(mode, skeySpec, iv)
                if (mode == Cipher.ENCRYPT_MODE) {
                    val encrypted = cipher.doFinal(value.toByteArray())
                    result = Base64.encodeToString(encrypted, Base64.DEFAULT)
                } else {
                    val original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT))
                    result = String(original)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                result = null
            }
            return result
        }

        /** Con esta función, se genera un arreglo de 64 bytes, que se usará de la siguiente manera:
         *     Bytes 0 al 15  Clave de 16 bytes para el algoritmo AES-128 CBC PKCS5Padding.
         *     Bytes 16 al 31  Arreglo de 16 bytes como vector de inicialización para el modo CBC
         *        del algoritmo AES-128 CBC PKCS5Padding.
         *     Bytes 32 al 63  Clave de 32 bytes para inicializar el algoritmo HMAC-SHA-256. */
        fun Sha512Hex(input: String): String {
            try {
                val md = MessageDigest.getInstance("SHA-512")
                val messageDigest = md.digest(input.toByteArray())
                val no = BigInteger(1, messageDigest)
                var hashtext = no.toString(16)
                while (hashtext.length < 32) {
                    hashtext = "0$hashtext"
                }
                return hashtext
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            }
        }

        /** Un código de autentificación de mensajes en clave-hash (HMAC) es una construcción específica
         *  para calcular un código de autentificación de mensaje (MAC) que implica una función hash
         *  criptográfica en combinación con una llave criptográfica secreta */
        @Throws(Exception::class)
        fun HmacSha256(key: String, data: String): String {
            val sha256_HMAC = Mac.getInstance("HmacSHA256")
            val secret_key = SecretKeySpec(Hex.decodeHex(key.toCharArray()), "HmacSHA256")
            sha256_HMAC.init(secret_key)
            val encrypted = sha256_HMAC.doFinal(data.toByteArray())
            return Base64.encodeToString(encrypted, Base64.DEFAULT).trim()
        }


        /** Función necesaria para acompletar el Dígito Verificador en caso de que la longitud sea
         *  menor a 3 posiciones */
        fun validateDv(dv: String): String {
            return when (dv.length) {
                1 -> "00".plus(dv)
                2 -> "0".plus(dv)
                else -> dv
            }
        }

        /** El folio de mensaje de cobro es un valor alfanumérico de 20 caracteres, que se crea en la
         *  App CoDi generadora del mensaje y que posteriormente edita el receptor del Mensaje de cobro.*/
        fun getCodiNewId(readQrId: String): String {
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
            return readQrId + newIdHex + secondIdHex
        }

        /** Si uno de los bits comparados es 0 y el otro 1, el resultado es 1. Si ambos bits comparados
         *  son iguales, el resultado es 0. */
        fun XOR(objOne: String, objTwo: String): String {
            var finalString = ""
            val arrayOne = Hex.decodeHex(objOne.toCharArray())
            val arrayTwo = Hex.decodeHex(objTwo.toCharArray())
            var byteArray = ByteArray(arrayOne.size)
            var i = 0
            while (i < arrayOne.size) {
                byteArray[i] = arrayOne[i] xor arrayTwo[i]
                i++
            }
            val charArray = Hex.encodeHex(byteArray)
            var j = 0
            while (j < charArray.size) {
                finalString += charArray[j]
                j++
            }
            return finalString
        }

        /* Obtiene la referencia numérica a partir de la fecha en el momento en que se mande a solicitar
         * la función, esto para poder mandarlo en un formato de 2-Año, 2-Mes, 2-Día */
        fun getNumericReference(): Long {
            val cal = Calendar.getInstance()
            cal.firstDayOfWeek = Calendar.MONDAY
            cal.timeInMillis = System.currentTimeMillis()
            val yearTwoDigits = cal.get(Calendar.YEAR) % 100
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            return "$yearTwoDigits$month$day".toLong()
        }

        @TargetApi(Build.VERSION_CODES.Q)
        fun isAppIsInBackground(context: Context): Boolean {
            Log.e(this.javaClass.simpleName, "isAppIsInBackground: " + "show")
            var isInBackground = true
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                val runningProcesses = am.runningAppProcesses
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && processInfo.processName == context.packageName) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } else {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity
                if (componentInfo?.packageName == context.packageName) {
                    isInBackground = false
                }
            }

            return isInBackground
        }

        /**
         * Método empleado para procesar el cifrado traido desde la notificación por Banxico
         */
        fun processAccountValidation(key: String, iv: String, infoCuenta: Info_Cuenta): String {
            val dechyperPush = AesCbcPkcs(key, iv, infoCuenta.infCif, Cipher.DECRYPT_MODE, AES_CBC_PKCS5)
            val data = Gson().fromJson(dechyperPush, ValidacionCuentasDecryp_Data::class.java)
            return when (data.rv) {
                0 -> CODI_ACCOUNT_PENDENT
                1 -> CODI_ACCOUNT_VERIFIED
                3 -> CODI_ACCOUNT_INVALID
                else -> CODI_ACCOUNT_UNKNOWN
            }
        }

        /**
         * Parse Hex string to Ascii string
         **/
        fun hexToAscii(hexStr: String): String {
            val output = StringBuilder("")
            var i = 0
            while (i < hexStr.length) {
                val str = hexStr.substring(i, i + 2)
                output.append(Integer.parseInt(str, 16).toChar())
                i += 2
            }
            return output.toString()
        }

        fun translateTlv(tlv: String): String {
            val b = arrayOfNulls<String>(tlv.length / 2)
            var translate = String()
            var searchTag = true
            var tagHas2Bytes = false
            var searchLength = false
            var searchContent = false
            var lengthBytesContent = 0
            var bytesRead = 1
            var hexToBin: String
            var TAG = ""
            for (i in b.indices) {
                /* Obtención de bytes hexadecimales del String */
                val index = i * 2
                val v = tlv.substring(index, index + 2)
                b[i] = v
                /* Conversión hexadecimal a binario */
                hexToBin = BigInteger(b[i], 16).toString(2)
                /* Buscamos el TAG Emv */
                if (searchTag) {
                    /* Si el tag contiene un byte hexadecimal entonces se le asigna a la variable TAG,
                 * en caso de que contenga 2 bytes se deberá concatenar el segundo byte al TAG y se
                 * procede a buscar la longitud del contenido del tag */
                    if (tagHas2Bytes) {
                        TAG += b[i]
                        translate += b[i]
                        tagHas2Bytes = false
                        searchTag = false
                        searchLength = true
                    } else {
                        TAG = b[i].toString()
                        translate += TAG
                        /* Si los ultimos 5 bits del hexadecimal convertido en binario están prendidos entonces
                     * significa que el tag contiene 2 bytes hexadecimales */
                        if (hexToBin.length > 4 && hexToBin.substring(
                                        hexToBin.length - 5,
                                        hexToBin.length
                                ) == "11111"
                        ) {
                            tagHas2Bytes = true
                        } else {
                            tagHas2Bytes = false
                            searchTag = false
                            searchLength = true
                        }
                    }
                    /* Una vez encontrado el TAG procedemos a buscar la longitud del contenido del tag */
                } else if (searchLength) {
                    translate += b[i]
                    lengthBytesContent = Integer.parseInt(b[i].toString(), 16)
                    searchLength = false
                    searchContent = true
                    /* Una vez que ya se enontró la longitud, se debe empezar a buscar el contenido del
                 * dependiendo la longitud indicada, así como la limpieza para el TAG 91 */
                } else if (searchContent) {
                    /* Si el numero de bytes leidos aún son menores a la longitud solicitada o el TAG
                 * es 91 y bytes leidos son iguales a la longitud maxima del tag (16 bytes), se
                 * procede a guardar el byte e incrementar el contador de bytes leidos */
                    if (bytesRead < lengthBytesContent) {
                        translate += if (TAG == "9F1E") "08" else b[i]
                        bytesRead++
                        /* En caso de que no se cumplan las validaciones anteriores, significa que el TAG
                     * es diferente al 91 y que ya se encuentra leyendo el último byte por la longitud
                     * indicada.  Se guarda ese byte y se reinician las banderas para que continue
                     * leyendo TAG. */
                    } else {
                        translate += if (TAG == "9F1E") "08" else b[i]
                        bytesRead = 1
                        lengthBytesContent = 0
                        searchContent = false
                        searchTag = true
                    }
                }
            }
            Log.e("EMV", "ARPC ORIGINAL: $tlv")
            Log.e("EMV", "ARPC TRADUCIDO: $translate")
            return translate
        }
    }
}