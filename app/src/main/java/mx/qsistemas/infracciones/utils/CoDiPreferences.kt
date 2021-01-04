package mx.qsistemas.infracciones.utils

import android.content.Context
import android.content.SharedPreferences
import mx.qsistemas.infracciones.utils.UtilsCodi.Companion.AesCbcPkcs
import javax.crypto.Cipher

class CoDiPreferences(val context: Context, val encryptKey: Boolean) {

    private val preferences: SharedPreferences = context.getSharedPreferences("codi_preferences",
            Context.MODE_PRIVATE)

    fun saveData(key: Int, data: String) {
        preferences.edit().apply {
            val dataCipher = AesCbcPkcs(KEY, IV, data, Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
            if (encryptKey) {
                val encryptKey = AesCbcPkcs(KEY, IV, context.getString(key), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
                putString(encryptKey, dataCipher)
            } else {
                putString(context.getString(key), dataCipher)
            }
        }.apply()
    }

    fun saveDataBool(key: Int, data: Boolean) {
        preferences.edit().apply {
            val dataCipher = AesCbcPkcs(KEY, IV, data.toString(), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
            if (encryptKey) {
                val encryptKey = AesCbcPkcs(KEY, IV, context.getString(key), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
                putString(encryptKey, dataCipher)
            } else {
                putString(context.getString(key), dataCipher)
            }
        }.apply()
    }

    fun saveDataInt(key: Int, data: Int) {
        preferences.edit().apply {
            val dataCipher = AesCbcPkcs(KEY, IV, data.toString(), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
            if (encryptKey) {
                val encryptKey = AesCbcPkcs(KEY, IV, context.getString(key), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
                putString(encryptKey, dataCipher)
            } else {
                putString(context.getString(key), dataCipher)
            }
        }.apply()
    }

    fun loadDataBoolean(key: Int, defValue: Boolean): Boolean {
        return if (encryptKey) {
            val encryptKey = AesCbcPkcs(KEY, IV, context.getString(key), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
            val contentC = preferences.getString(encryptKey, defValue.toString())!!
            AesCbcPkcs(KEY, IV, contentC, Cipher.DECRYPT_MODE, AES_CBC_PKCS5)!!.toBoolean()
        } else {
            val contentC = preferences.getString(context.getString(key), defValue.toString())!!
            AesCbcPkcs(KEY, IV, contentC, Cipher.DECRYPT_MODE, AES_CBC_PKCS5)!!.toBoolean()
        }
    }

    fun loadData(key: Int, defValue: String): String? {
        return if (encryptKey) {
            val encryptKey = AesCbcPkcs(KEY, IV, context.getString(key), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
            val contentC = preferences.getString(encryptKey, defValue)!!
            AesCbcPkcs(KEY, IV, contentC, Cipher.DECRYPT_MODE, AES_CBC_PKCS5)!!
        } else {
            val contentC = preferences.getString(context.getString(key), defValue)!!
            AesCbcPkcs(KEY, IV, contentC, Cipher.DECRYPT_MODE, AES_CBC_PKCS5)!!
        }
    }

    fun loadDataInt(key: Int): Int {
        return if (encryptKey) {
            val encryptKey = AesCbcPkcs(KEY, IV, context.getString(key), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
            val contentC = preferences.getString(encryptKey, (-1).toString())!!
            AesCbcPkcs(KEY, IV, contentC, Cipher.DECRYPT_MODE, AES_CBC_PKCS5)!!.toInt()
        } else {
            val contentC = preferences.getString(context.getString(key), (-1).toString())!!
            AesCbcPkcs(KEY, IV, contentC, Cipher.DECRYPT_MODE, AES_CBC_PKCS5)!!.toInt()
        }
    }

    fun clearPreferences() {
        preferences.edit().apply {
            clear()
        }.apply()
        return
    }

    fun removeData(key: Int) {
        preferences.edit().apply {
            if (encryptKey) {
                val encryptKey = AesCbcPkcs(KEY, IV, context.getString(key), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
                remove(encryptKey)
            } else
                remove(context.getString(key))
        }.apply()
        return
    }

    fun containsData(key: Int): Boolean {
        return if (encryptKey) {
            val encryptKey = AesCbcPkcs(KEY, IV, context.getString(key), Cipher.ENCRYPT_MODE, AES_CBC_PKCS5)
            preferences.contains(encryptKey)
        } else
            preferences.contains(context.getString(key))
    }

    companion object {
        private val KEY = MD5.toMD5("CODI_QUETZALCOATL")
        private val IV = MD5.toMD5("QSI9900")
    }
}