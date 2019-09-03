package mx.qsistemas.infracciones

import mx.qsistemas.payments_transfer.utils.Utils
import org.junit.Test
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class KotinUnitTest {

    @Test
    fun exampleSha512() {
        println(Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase())
        println(Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase().substring(0, 32))
        println(Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase().substring(120))
    }

    @Test
    fun compareDates() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd").format(SimpleDateFormat("dd/MM/yyyy").parse("02/09/2019"))
        println(dateFormat)
        /*val expDate = SimpleDateFormat("yyyy-MM-dd").parse("2019-08-30")
        val actualDate = Date()
        if (actualDate.before(expDate) || DateUtils.isSameDay(actualDate, expDate)) {
            print("Es menor o igual")
        } else {
            print("Es mayor")
        }*/
    }
}