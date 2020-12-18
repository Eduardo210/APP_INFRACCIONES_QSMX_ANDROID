package mx.qsistemas.infracciones.modules.main

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivityMainBinding
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.payments_transfer.PaymentsTransfer

class MainActivity : ActivityHelper(), MainContracts.Presenter {

    val router = lazy { MainRouter(this) }
    private val iterator by lazy { MainIterator(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        router.value.presentInfractionList(Direction.NONE)
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(this, msg, Snackbar.LENGTH_LONG)
    }

    override fun onResume() {
        super.onResume()
        /* Always try to run reversal of tx's with no response */
        PaymentsTransfer.runReversal(this)
        /* Always reconfigure */
        PaymentsTransfer.reconfigure()
        /* Delete all photos in caché */
        val cache = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        cache?.delete()
        /* Always validate session */
        iterator.validateSession()
    }

    override fun enableHighAccuracyGps() {
        onError(getString(R.string.e_high_accuracy_required))
        Handler().postDelayed({ router.value.presentLocationSettings() }, 3000)
    }

    override fun onSessionClosed() {
        val builder = AlertDialogHelper.getGenericBuilder(getString(R.string.w_dialog_close_session),
                getString(R.string.w_please_close_session), this)
        builder.setPositiveButton("Aceptar") { _, _ ->
            router.value.presentLogIn()
        }
        builder.show()
    }
}
