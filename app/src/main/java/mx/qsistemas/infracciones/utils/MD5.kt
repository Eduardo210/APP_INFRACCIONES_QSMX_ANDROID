package mx.qsistemas.infracciones.utils

import java.security.NoSuchAlgorithmException

class MD5 {
    companion object {
        fun toMD5(s: String): String {
            try {
                val digest = java.security.MessageDigest.getInstance("MD5")
                digest.update(s.toByteArray())
                val messageDigest = digest.digest()
                val hexString = StringBuilder()
                for (msg in messageDigest) {
                    var h = Integer.toHexString(0xFF and msg.toInt())
                    while (h.length < 2)
                        h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString().toUpperCase()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}