package mx.qsistemas.infracciones.modules.login

class LogInContracts {

    interface Presenter {
        fun onError(msg: String)
        fun onCatalogsDownloaded()
    }

    interface Iterator {
        fun registerAlarm()
        fun isCorrectCredentials(user: String, pss: String): Boolean
        fun downloadCatalogs()
    }

    interface Router {
        fun presentMainActivity()
    }
}