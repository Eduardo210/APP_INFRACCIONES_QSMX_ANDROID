package mx.qsistemas.infracciones.modules.main.fr_infraction_history

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentInfractionListBinding
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.main.MainActivity
import mx.qsistemas.infracciones.net.FirebaseEvents
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.RC_PERMISSION_LOCATION
import mx.qsistemas.infracciones.utils.Validator
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class InfractionListFr : Fragment(), View.OnClickListener {

    private lateinit var activity: MainActivity
    private lateinit var binding: FragmentInfractionListBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_infraction_list, container, false)
        //binding.rcvInfractions.layoutManager = GridLayoutManager(activity, 1)
        binding.btnAddInfraction.setOnClickListener(this)
        binding.btnSearchInfraction.setOnClickListener(this)
        binding.btnPrintVoucher.setOnClickListener(this)
        binding.include.imgSearchInfraction.setOnClickListener(this)
        val name = Application.prefs?.loadData(R.string.sp_person_name, "")
        val lastName = Application.prefs?.loadData(R.string.sp_person_f_last_name, "")
        val mLastName = Application.prefs?.loadData(R.string.sp_person_m_last_name, "")
        binding.include.txtNameDashboard.text = "$name $lastName $mLastName"
        Picasso.get().load(Application.prefs?.loadData(R.string.sp_person_photo_url, "")).error(R.drawable.ic_add_photo).into(binding.include.imgUserPhoto)
        return binding.root
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnAddInfraction.id -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RC_PERMISSION_LOCATION)
                } else {
                    /* Validate if gps is enable */
                    val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && Validator.isHighAccuracyEnable(activity)) {
                        FirebaseEvents.registerInfractionStarted()
                        SingletonInfraction.cleanSingleton()
                        activity.router.value.presentNewInfraction()
                    } else if (Validator.isMockLocationEnable(activity)) {
                        Snackbar.make(binding.root, getString(R.string.e_mock_location), Snackbar.LENGTH_SHORT).show()
                    } else {
                        activity.enableHighAccuracyGps()
                    }
                }
            }
            binding.btnSearchInfraction.id -> {
                activity.router.value.presentSearchInfraction()
            }
            binding.btnPrintVoucher.id -> {
                PaymentsTransfer.printLastVoucher(activity, object : IPaymentsTransfer.TransactionListener {
                    override fun onTxApproved(txInfo: TransactionInfo) {}
                    override fun onTxFailed(message: String) {
                        SnackbarHelper.showErrorSnackBar(activity, message, Snackbar.LENGTH_SHORT)
                    }

                    override fun onTxVoucherFailer(message: String) {}
                    override fun onTxVoucherPrinted() {
                        FirebaseEvents.registerReprintVoucher()
                    }
                })
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                InfractionListFr().apply {
                    arguments = Bundle().apply { }
                }
    }
}