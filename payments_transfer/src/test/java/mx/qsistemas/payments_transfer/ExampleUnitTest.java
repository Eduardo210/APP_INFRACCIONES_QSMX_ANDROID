package mx.qsistemas.payments_transfer;

import org.junit.Test;

import javax.crypto.Cipher;

import mx.qsistemas.payments_transfer.utils.Utils;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_tlv() {
       //String content = Utils.Companion.searchTag("5F20","9F2608B46E54B68E7054EA9F100706010A03A420089F2701809F37040CDB3C4E9F36020205950502A00088009A037009239C01009F02060000000000015F2A02048482025C009F1A0204849F03060000000000009F3303E0E9C89F34034103029F3501169F1E0830303030303132338407A00000000320109F090201409F410400000001");
    }

    @Test
    public void test_aes(){
        System.out.println(Utils.Companion.AesCbcPkcs("Stzvy`OoS7Dc6wYy","123ABCDEF123456","1qazxsw2&", Cipher.ENCRYPT_MODE));
    }
}