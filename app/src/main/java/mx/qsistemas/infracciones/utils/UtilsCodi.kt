package mx.qsistemas.infracciones.utils

import android.util.Base64
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import org.apache.commons.codec.binary.Hex
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

class UtilsCodi {
    companion object {
        /**
         *  Generate folio para el mensaje de cobro
         **/
        fun generateFolioCoDi(): String {
            val cal = Calendar.getInstance()
            cal.firstDayOfWeek = Calendar.MONDAY
            cal.timeInMillis = System.currentTimeMillis()
            cal.timeZone = TimeZone.getTimeZone("GMT")
            val yearTwoDigits = cal.get(Calendar.YEAR) % 100
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minutes = cal.get(Calendar.MINUTE)
            val seconds = cal.get(Calendar.SECOND)
            val millisecond = cal.get(Calendar.MILLISECOND) % 128
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
            newId = (newId shl 7) + millisecond
            var secondIdHex = Integer.toHexString(newId)
            while (secondIdHex.length < 6) {
                secondIdHex = "0$secondIdHex"
            }
            return newIdHex + secondIdHex
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

        /** Función necesaria para acompletar el Dígito Verificador en caso de que la longitud sea
         *  menor a 3 posiciones */
        fun validateDv(dv: String): String {
            return when (dv.length) {
                1 -> "00".plus(dv)
                2 -> "0".plus(dv)
                else -> dv
            }
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
                result = if (mode == Cipher.ENCRYPT_MODE) {
                    val encrypted = cipher.doFinal(value.toByteArray())
                    Base64.encodeToString(encrypted, Base64.DEFAULT)
                } else {
                    val original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT))
                    String(original)
                }
            } catch (ex: Exception) {
                result = null
            }
            return result?.replace("\n", "")
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
                    hashtext += hashtext
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

        /** Obtener el número de serie del mensaje de cobro que se realizó durante el día, este serial
         *  se reinicia diario para así llevar una contabilidad de los cobros generados al día */
        fun getSerialCoDi(): Int {
            val serialUpdateTime = if (Application.prefsCodi?.containsData(R.string.sp_codi_serial_update)!!) Application.prefsCodi?.loadDataInt(R.string.sp_codi_serial_update)!!.toLong() else System.currentTimeMillis()
            val serialNumber = if (Application.prefsCodi?.containsData(R.string.sp_codi_serial)!!) Application.prefsCodi?.loadDataInt(R.string.sp_codi_serial)!! else 0
            val calendarUpdate = Calendar.getInstance()
            calendarUpdate.time = Date(serialUpdateTime)
            val calendarToday = Calendar.getInstance()
            calendarToday.time = Date()
            Application.prefsCodi?.saveDataInt(R.string.sp_codi_serial_update, System.currentTimeMillis().toInt())
            return if (calendarToday.get(Calendar.DAY_OF_MONTH) != calendarUpdate.get(Calendar.DAY_OF_MONTH)) {
                Application.prefsCodi?.saveDataInt(R.string.sp_codi_serial, 0)
                0
            } else {
                Application.prefsCodi?.saveDataInt(R.string.sp_codi_serial, serialNumber + 1)
                serialNumber + 1
            }
        }
    }
}