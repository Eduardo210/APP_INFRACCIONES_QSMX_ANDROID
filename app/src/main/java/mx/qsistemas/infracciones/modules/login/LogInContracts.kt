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
        fun downloadCatalogs()
        fun login(userName: String, psd: String)
    }

    interface Router {
        fun presentMainActivity()
    }
}