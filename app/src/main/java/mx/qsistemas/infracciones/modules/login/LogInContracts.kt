package mx.qsistemas.infracciones.modules.login

class LogInContracts {

    interface Presenter {
        fun onError(msg: String)
    }

    interface Iterator {
        fun registerAlarm()
        fun isCorrectCredentials(user: String, pss: String): Boolean
    }

    interface Router {
        fun presentMainActivity()
    }
}