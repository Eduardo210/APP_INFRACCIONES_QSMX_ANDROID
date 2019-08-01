package mx.qsistemas.infracciones.modules.main

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivityMainBinding
import mx.qsistemas.infracciones.db_web.entities.PersonTownhall
import mx.qsistemas.infracciones.db_web.managers.SaveInfractionManagerWeb
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction

class MainActivity : ActivityHelper(), MainContracts.Presenter {

    val router = lazy { MainRouter(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //TODO: Sólo para crear base web, quitar después
        SaveInfractionManagerWeb.savePersonTownHall(PersonTownhall(1,"666666",true))
        Log.d("BASE_WEB", "---------> SUCCESS")
        router.value.presentInfractionList(Direction.NONE)
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(this, msg, Snackbar.LENGTH_LONG)
    }

    override fun enableHighAccuracyGps() {
        onError(getString(R.string.e_high_accuracy_required))
        Handler().postDelayed({ router.value.presentLocationSettings() }, 3000)
    }

    override fun onBackPressed() {
        val builder = AlertDialogHelper.getGenericBuilder(getString(R.string.w_dialog_close_session), getString(R.string.w_want_to_close_session), this)
        builder.setPositiveButton("Aceptar") { _, _ ->
            super.onBackPressed()
        }
        builder.setNegativeButton("Cancelar") { _, _ -> }
        builder.show()
    }
}
