package mx.qsistemas.infracciones.modules.login

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.incidencias.utils.Validator
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivityLogInBinding
import mx.qsistemas.infracciones.dialogs.initial_configuration.InitialConfigurationCallback
import mx.qsistemas.infracciones.dialogs.initial_configuration.InitialConfigurationDialog
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper

class LogInActivity : ActivityHelper(), LogInContracts.Presenter, View.OnClickListener, InitialConfigurationCallback {

    private lateinit var binding: ActivityLogInBinding
    private val iterator = LogInIterator(this)
    private val router = LogInRouter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        binding.btnLogIn.setOnClickListener(this)
        if (!Application.prefs?.loadDataBoolean(R.string.sp_has_config_prefix, false)!!) {
            val dialog = InitialConfigurationDialog()
            dialog.listener = this
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, InitialConfigurationDialog::class.java.simpleName)
        } else {
            // TODO: Sync Catalogs
        }
        if (Application.prefs?.loadDataBoolean(R.string.sp_has_session, false)!!) {
            router.presentMainActivity()
        }
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(this, msg, Snackbar.LENGTH_SHORT)
    }

    override fun onDialogError(msg: String) {
        SnackbarHelper.showErrorSnackBar(this, msg, Snackbar.LENGTH_SHORT)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLogIn.id -> {
                val fields = arrayOf(binding.edtUserLogIn.text.toString(), binding.edtDwpLogIn.text.toString())
                if (!Validator.isValidFields(*fields)) {
                    onError(getString(R.string.e_empty_fields))
                } else if (!iterator.isCorrectCredentials(binding.edtUserLogIn.text.toString(), binding.edtDwpLogIn.text.toString())) {
                    onError(getString(R.string.e_user_pss_incorrect))
                } else {
                    router.presentMainActivity()
                }
            }
        }
    }
}