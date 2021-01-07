package mx.qsistemas.infracciones.modules.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.Application.Companion.TAG
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ActivityLogInBinding
import mx.qsistemas.infracciones.dialogs.InitialConfigurationCallback
import mx.qsistemas.infracciones.dialogs.InitialConfigurationDialog
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.net.catalogs.Versions
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
            if (!Application.prefs.loadDataBoolean(R.string.sp_has_config_prefix, false)) {
                val dialog = InitialConfigurationDialog()
                dialog.listener = this
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, InitialConfigurationDialog::class.java.simpleName)
            } else if (Application.prefs.loadDataBoolean(R.string.sp_has_session, false)) {
                router.presentMainActivity()
            }
        } else if (Application.prefs.loadDataBoolean(R.string.sp_has_session, false)) {
            router.presentMainActivity()
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
        PaymentsTransfer.configDevice(idTownship, prefix, 2, 1, "13F0A294679546195AD092C4E5937A41",
                PTX_VOUCHER_TITLE, PTX_VOUCHER_ADDRESS_1, PTX_VOUCHER_ADDRESS_2)
        Handler(Looper.getMainLooper()).postDelayed({
            val loadKeyData = LoadKeyData(PTX_SERIAL_NUMBER, PTX_MERCHANT_ID, PTX_MAIN, PTX_PSW)
            PaymentsTransfer.loadKeyDevice(this, loadKeyData, object : IPaymentsTransfer.LoadKeyListener {
                override fun onLoadKey(success: Boolean, value: String) {
                    if (!success) {
                        onError(value)
                    } else {
                        showLoader(getString(R.string.l_download_catalogs))
                        lifecycleScope.launch {
                            iterator.syncCatalogs()
                        }
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
        Application.firestore.collection(FS_COL_APP_VERSIONS).document(FS_DOC_VERSION).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val doc = it.result?.toObject(Versions::class.java)!!
                if (doc.version != BuildConfig.VERSION_NAME) {
                    val builder = AlertDialogHelper.getGenericBuilder(
                            getString(R.string.w_dialog_update), getString(R.string.w_need_to_update), this
                    )
                    builder.setPositiveButton("Aceptar") { _, _ ->
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")))
                        } catch (anfe: android.content.ActivityNotFoundException) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(doc.url)))
                        }
                    }
                    builder.show()
                }
            } else {
                Log.e(TAG, "Cannot obtain remote version of application")
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
                    showLoader(getString(R.string.l_log_in))
                    iterator.login(binding.edtUserLogIn.text.toString(), binding.edtDwpLogIn.text.toString())
                }
            }
        }
    }
}