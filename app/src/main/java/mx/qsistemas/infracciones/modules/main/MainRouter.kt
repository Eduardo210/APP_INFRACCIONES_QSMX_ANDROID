package mx.qsistemas.infracciones.modules.main

import android.content.Intent
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.login.LogInActivity
import mx.qsistemas.infracciones.modules.main.fr_infraction_history.InfractionListFr

class MainRouter(private val activity: MainActivity) : MainContracts.Router {

    override fun presentInfractionList(direction: Direction) {
        activity.loadFragment(InfractionListFr.newInstance(), R.id.main_container, direction, false)
    }

    override fun presentNewInfraction() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun presentDetailInfraction(folio: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun presentLogIn() {
        activity.startActivity(Intent(Application.getContext(), LogInActivity::class.java))
        activity.finish()
    }
}