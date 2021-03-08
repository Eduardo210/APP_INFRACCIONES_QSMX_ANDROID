package mx.qsistemas.infracciones.modules.main.fr_menu

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.databinding.FragmentMenuBinding
import mx.qsistemas.infracciones.dialogs.ReconfigurationCallback
import mx.qsistemas.infracciones.dialogs.ReconfigureDialog
import mx.qsistemas.infracciones.enum.InfringementPermissions
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.main.MainActivity
import mx.qsistemas.infracciones.net.catalogs.HomeOptions
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.*
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.LoadKeyData
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class MenuFragment : Fragment(), MenuContracts.Presenter, MenuContracts.OnHomeOptionListener,
        ReconfigurationCallback, View.OnClickListener, View.OnLongClickListener {

    private val iterator by lazy { MenuIterator(this) }
    private lateinit var activity: MainActivity
    private lateinit var binding: FragmentMenuBinding
    private var dialogApplication: AlertDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        binding.rcvInfractions.layoutManager = GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false)
        binding.include.imgSearchInfraction.setOnClickListener(this)
        binding.include.imgUserPhoto.setOnClickListener(this)
        val name = Application.prefs.loadData(R.string.sp_person_name, "")
        val paternal = Application.prefs.loadData(R.string.sp_person_paternal, "")
        val maternal = Application.prefs.loadData(R.string.sp_person_maternal, "")
        binding.include.txtNameDashboard.text = "$name $paternal $maternal"
        binding.include.txtNameDashboard.isSelected = true
        Picasso.get().load(Application.prefs.loadData(R.string.sp_person_photo_url, "")).error(R.drawable.ic_account)
                .transform(CircleTransformation()).into(binding.include.imgUserPhoto)
        iterator.getHomeOptions()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        /* Si la aplicación no se encuentra activa se deberá mostrar el diálogo de bloqueo */
        iterator.checkIfApplicationIsActive()
        /* Validar que el usuario no haya iniciadio sesión en otro dispositivo */
        iterator.validateSession()
    }

    override fun onApplicationDisable() {
        dialogApplication = AlertDialogHelper.getErrorBuilder(getString(R.string.w_dialog_title), getString(R.string.w_application_block), activity).create()
        dialogApplication?.setCancelable(false)
        dialogApplication?.show()
    }

    override fun onApplicationEnable() {
        dialogApplication?.dismiss()
    }

    override fun onSessionExpired() {
        val sessionDialog = AlertDialogHelper.getErrorBuilder(getString(R.string.w_dialog_close_session), getString(R.string.w_please_close_session), activity)
        sessionDialog.setCancelable(false)
        sessionDialog.setPositiveButton(getString(R.string.t_close)) { _, _ ->
            iterator.closeSession()
            activity.router.presentLogIn()
        }
        sessionDialog.show()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.include.imgUserPhoto.id -> {
                val builder = AlertDialogHelper.getGenericBuilder(getString(R.string.w_dialog_close_session), getString(R.string.w_want_to_close_session), activity)
                builder.setPositiveButton("Sí") { _, _ ->
                    iterator.closeSession()
                    activity.router.presentLogIn()
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
            }
        }
    }

    override fun onHomeOptionsReady(list: MutableList<HomeOptions>) {
        binding.rcvInfractions.adapter = MenuOptionsAdapter(list, this)
    }

    override fun onError(msg: String) {
        activity.hideLoader()
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_SHORT)
    }

    override fun onClickOption(idOption: String) {
        when (idOption) {
            HO_ID_INFRACTION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RC_PERMISSION_LOCATION)
                } else if (InfringementPermissions.INSERT.permission in iterator.getAttributes()) {
                    /* Validate if gps is enable */
                    val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && Validator.isHighAccuracyEnable(activity)) {
                        SingletonInfraction.cleanSingleton()
                        activity.router.presentNewInfraction()
                    } else if (Validator.isMockLocationEnable(activity)) {
                        Snackbar.make(binding.root, getString(R.string.e_mock_location), Snackbar.LENGTH_SHORT).show()
                    } else {
                        activity.enableHighAccuracyGps()
                    }
                } else {
                    onError(getString(R.string.e_without_permissions))
                }
            }
            HO_ID_SEARCH -> {
                if (InfringementPermissions.SEARCH.permission in iterator.getAttributes())
                    activity.router.presentSearchInfraction()
                else
                    onError(getString(R.string.e_without_permissions))
            }
            HO_ID_VOUCHER -> PaymentsTransfer.printLastVoucher(activity, object : IPaymentsTransfer.TransactionListener {
                override fun onTxApproved(txInfo: TransactionInfo) {}
                override fun onTxFailed(retry: Boolean, message: String) {
                    SnackbarHelper.showErrorSnackBar(activity, message, Snackbar.LENGTH_SHORT)
                }
                override fun onCtlsDoubleTap() {}
                override fun onTxVoucherFailed(message: String) {}
                override fun onTxVoucherPrinted() {
                }
            })
            HO_ID_PREFERENCES -> activity.router.presentMyPreferences(Direction.FORDWARD)
            HO_ID_SETTINGS -> {
                val dialog = ReconfigureDialog()
                dialog.isCancelable = true
                dialog.listener = this
                dialog.show(activity.supportFragmentManager, ReconfigureDialog::class.java.simpleName)
            }
            HO_VALIDATE_ACCOUNT_CODI -> activity.router.presentBankAccountValidation()
            HO_SEND_DB -> {
                activity.showLoader(getString(R.string.l_sending_database))
                iterator.sendDatabase()
            }
        }
    }

    override fun onDatabaseSend() {
        activity.hideLoader()
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_send_db_success), Snackbar.LENGTH_SHORT)
    }

    override fun onPasswordConfirm(psd: String) {
        activity.showLoader(getString(R.string.l_config_terminal))
        iterator.reconfigureDevice(psd)
    }

    override fun onReconfigureFirestore() {
        val prefix = Application.prefs.loadData(R.string.sp_prefix, "")!!
        val idTownship = Application.prefs.loadData(R.string.sp_id_township, "")
        PaymentsTransfer.configDevice(idTownship.toString(), prefix, 2, 1, "13F0A294679546195AD092C4E5937A41",
                PTX_VOUCHER_TITLE, PTX_VOUCHER_ADDRESS_1, PTX_VOUCHER_ADDRESS_2)
        Handler(Looper.getMainLooper()).postDelayed({
            val loadKeyData = LoadKeyData(PTX_SERIAL_NUMBER, PTX_MERCHANT_ID, PTX_MAIN, PTX_PSW)
            PaymentsTransfer.loadKeyDevice(activity, loadKeyData, object : IPaymentsTransfer.LoadKeyListener {
                override fun onLoadKey(success: Boolean, value: String) {
                    if (!success) {
                        onError(value)
                    } else {
                        activity.hideLoader()
                        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_reconfigure_success), Snackbar.LENGTH_SHORT)
                    }
                }
            })
        }, 2000)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                MenuFragment().apply {
                    arguments = Bundle().apply { }
                }
    }

    override fun onLongClick(p0: View?): Boolean {
        when(p0?.id){
            binding.include.imgUserPhoto.id -> Alarms()
        }
        return true
    }
}