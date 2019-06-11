package mx.qsistemas.infracciones.modules.login

import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R

class LogInIterator(private val presenter: LogInContracts.Presenter) : LogInContracts.Iterator {

    private val USER_REPORTER = "user"
    private val USER_ADMIN = "admin"
    private val PSS = "123456"

    override fun registerAlarm() {
        //Alarms()
    }

    override fun isCorrectCredentials(user: String, pss: String): Boolean {
        return if ((user != USER_REPORTER && user != USER_ADMIN) || pss != PSS) false
        else {
            Application.prefs?.saveDataBool(R.string.sp_has_session, true)
            true
        }
    }
}