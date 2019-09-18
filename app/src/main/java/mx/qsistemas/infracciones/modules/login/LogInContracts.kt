package mx.qsistemas.infracciones.modules.login

class LogInContracts {

    interface Presenter {
        fun onError(msg: String)
        fun onCatalogsDownloaded()
        fun onLoginSuccessful()
        fun validateVersion()
    }

    interface Iterator {
        fun registerAlarm()
        suspend fun syncCatalogs()
        fun login(userName: String, psd: String)
    }

    interface Router {
        fun presentMainActivity()
    }
}