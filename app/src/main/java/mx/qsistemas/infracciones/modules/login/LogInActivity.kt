package mx.qsistemas.infracciones.modules.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivityLogInBinding
import mx.qsistemas.infracciones.dialogs.InitialConfigurationCallback
import mx.qsistemas.infracciones.dialogs.InitialConfigurationDialog
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.net.catalogs.RemoteVersion
import mx.qsistemas.infracciones.utils.*
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.LoadKeyData


class LogInActivity : ActivityHelper(), LogInContracts.Presenter, View.OnClickListener, InitialConfigurationCallback {

    private lateinit var binding: ActivityLogInBinding
    private val iterator = LogInIterator(this)
    private val router = LogInRouter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        binding.btnLogIn.setOnClickListener(this)
        iterator.registerAlarm()
        /* Validate internet connection */
        if (Validator.isNetworkEnable(Application.getContext())) {
            /* If user doesn't config the device, open Configuration Dialog */
            if (!Application.prefs?.loadDataBoolean(R.string.sp_has_config_prefix, false)!!) {
                val dialog = InitialConfigurationDialog()
                dialog.listener = this
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, InitialConfigurationDialog::class.java.simpleName)
            } else {
                /* If the user has config the device, check if he has already session. If hasn't session then,
                 * must need to download catalogs */
                showLoader(getString(R.string.l_download_catalogs))
                iterator.downloadCatalogs()
            }
        } else {
            onError(Application.getContext().getString(R.string.e_without_internet))
        }
    }

    override fun onError(msg: String) {
        hideLoader()
        SnackbarHelper.showErrorSnackBar(this, msg, Snackbar.LENGTH_LONG)
    }

    override fun onDialogError(msg: String) {
        SnackbarHelper.showErrorSnackBar(this, msg, Snackbar.LENGTH_SHORT)
    }

    override fun onConfigurationSuccessful(idTownship: String, prefix: String) {
        showLoader(getString(R.string.l_config_terminal))
        PaymentsTransfer.configDevice(idTownship, prefix)
        Handler().postDelayed({
            val loadKeyData = LoadKeyData(PTX_SERIAL_NUMBER, PTX_MERCHANT_ID, PTX_USER, PTX_PSW)
            PaymentsTransfer.loadKeyDevice(this, loadKeyData, object : IPaymentsTransfer.LoadKeyListener {
                override fun onLoadKey(success: Boolean, value: String) {
                    if (!success) {
                        onError(value)
                    } else {
                        showLoader(getString(R.string.l_download_catalogs))
                        iterator.downloadCatalogs()
                    }
                }
            })
        }, 2000)
    }

    override fun onResume() {
        super.onResume()
        if (Validator.isNetworkEnable(this) && !BuildConfig.DEBUG)
            validateVersion()
    }

    override fun onLoginSuccessful() {
        router.presentMainActivity()
    }

    override fun onCatalogsDownloaded() {
        hideLoader()
    }

    override fun validateVersion() {
        Application.remoteConfig?.fetch(0)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Application.remoteConfig?.activateFetched()
            }
            Log.i(this.javaClass.simpleName, "Fetch Succeed?: ${it.result}")
        }
        val versionJson = Application.remoteConfig?.getString(getString(R.string.rc_version)) ?: ""
        if (versionJson.isNotEmpty()) {
            val versionData = Gson().fromJson(versionJson, RemoteVersion::class.java)
            if (versionData.version != BuildConfig.VERSION_NAME) {
                val builder = AlertDialogHelper.getGenericBuilder(
                        getString(R.string.w_dialog_update), getString(R.string.w_need_to_update), this
                )
                builder.setPositiveButton("Aceptar") { _, _ ->
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")))
                    } catch (anfe: android.content.ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(versionData.url)))
                    }
                }
                builder.show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLogIn.id -> {
                val fields = arrayOf(binding.edtUserLogIn.text.toString(), binding.edtDwpLogIn.text.toString())
                if (!Validator.isNetworkEnable(Application.getContext())) {
                    onError(Application.getContext().getString(R.string.e_without_internet))
                } else if (!Validator.isValidFields(*fields)) {
                    onError(getString(R.string.e_empty_fields))
                } else {
                    iterator.login(binding.edtUserLogIn.text.toString().toUpperCase(), binding.edtDwpLogIn.text.toString().toUpperCase())
                }
            }
        }
    }
}