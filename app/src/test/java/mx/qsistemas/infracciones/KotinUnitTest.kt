package mx.qsistemas.infracciones

import mx.qsistemas.payments_transfer.utils.Utils
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class KotinUnitTest {

    @Test
    fun exampleSha512() {
        println(Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase())
        println(Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase().substring(0,32))
        println(Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase().substring(120))
    }
}