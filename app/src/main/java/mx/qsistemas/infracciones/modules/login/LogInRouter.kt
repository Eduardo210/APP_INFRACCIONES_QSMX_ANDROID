package mx.qsistemas.infracciones.modules.login

class LogInRouter(private val activity: LogInActivity) : LogInContracts.Router {

    override fun presentMainActivity() {
        /*activity.startActivity(Intent(Application.getContext(), MainActivity::class.java))
        activity.finish()*/
    }
}