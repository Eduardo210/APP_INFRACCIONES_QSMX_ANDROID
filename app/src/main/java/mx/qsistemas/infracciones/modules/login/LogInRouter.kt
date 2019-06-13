package mx.qsistemas.infracciones.modules.login

import android.content.Intent
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.modules.main.MainActivity

class LogInRouter(private val activity: LogInActivity) : LogInContracts.Router {

    override fun presentMainActivity() {
        activity.startActivity(Intent(Application.getContext(), MainActivity::class.java))
        activity.finish()
    }
}