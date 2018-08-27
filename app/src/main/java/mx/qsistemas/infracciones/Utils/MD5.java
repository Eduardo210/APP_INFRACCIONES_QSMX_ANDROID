package mx.qsistemas.infracciones.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Developed by ingmtz on 10/21/16.
 */

public class MD5 {

    public static String toMD5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();

            for (byte msg : messageDigest) {
                String h = Integer.toHexString(0xFF & msg);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

}
