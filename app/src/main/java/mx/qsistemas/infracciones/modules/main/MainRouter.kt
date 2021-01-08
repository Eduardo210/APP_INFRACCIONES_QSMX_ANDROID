package mx.qsistemas.infracciones.modules.main

import android.content.Intent
import android.provider.Settings
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.modules.create.OPTION_CREATE_INFRACTION
import mx.qsistemas.infracciones.modules.login.LogInActivity
import mx.qsistemas.infracciones.modules.main.fr_menu.MenuFragment
import mx.qsistemas.infracciones.modules.main.fr_my_preferences.MyPreferencesFragment
import mx.qsistemas.infracciones.modules.search.SearchActivity
import mx.qsistemas.infracciones.utils.EXTRA_MODE_CODI
import mx.qsistemas.infracciones.utils.EXTRA_OPTION_INFRACTION
import mx.qsistemas.infracciones.utils.MODE_ACCOUNT_VALIDATION

class MainRouter(private val activity: MainActivity) : MainContracts.Router {

    override fun presentInfractionList(direction: Direction) {
        activity.loadFragment(MenuFragment.newInstance(), R.id.main_container, direction, false)
    }

    override fun presentNewInfraction() {
        val intent = Intent(Application.getContext(), CreateInfractionActivity::class.java)
        intent.putExtra(EXTRA_OPTION_INFRACTION, OPTION_CREATE_INFRACTION)
        activity.startActivity(intent)
    }

    override fun presentSearchInfraction() {
        activity.startActivity(Intent(Application.getContext(), SearchActivity::class.java))
    }

    override fun presentDetailInfraction(folio: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun presentLogIn() {
        activity.startActivity(Intent(Application.getContext(), LogInActivity::class.java))
        activity.finish()
    }

    override fun presentLocationSettings() {
        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    override fun presentMyPreferences(direction: Direction) {
        activity.loadFragment(MyPreferencesFragment.newInstance(), R.id.main_container, direction, false)
    }

    override fun presentBankAccountValidation() {
        /*val intent = Intent(Application.getContext(), CodiActivity::class.java)
        intent.putExtra(EXTRA_MODE_CODI, MODE_ACCOUNT_VALIDATION)
        activity.startActivity(intent)*/
    }
}