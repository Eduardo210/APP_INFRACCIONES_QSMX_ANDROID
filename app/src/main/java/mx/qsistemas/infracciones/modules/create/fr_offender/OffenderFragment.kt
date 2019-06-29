package mx.qsistemas.infracciones.modules.create.fr_offender

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.FragmentOffenderBinding
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROBE_RANDOM
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROD

private const val ARG_IS_CREATION = "is_creation"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OffenderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OffenderFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OffenderFragment : Fragment(), OffenderContracts.Presenter, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener,
        OnClickListener, IPaymentsTransfer.TransactionListener {

    private var isCreation: Boolean = true
    private var isPaid: Boolean = false
    private var isTicketCopy: Boolean = false
    private val iterator = lazy { OffenderIterator(this) }
    private lateinit var binding: FragmentOffenderBinding
    private lateinit var activity: CreateInfractionActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as CreateInfractionActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isCreation = it.getBoolean(ARG_IS_CREATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offender, container, false)
        initAdapters()
        fillFields()
        if (!isCreation) {
            binding.textView.visibility = GONE
            binding.rdgReferralDeposit.visibility = GONE
            binding.lytOffender.root.visibility = VISIBLE
            binding.lytOffender.textView7.visibility = GONE
            binding.lytOffender.edtOffenderRfc.visibility = GONE
            binding.lytOffender.textView8.visibility = GONE
            binding.lytOffender.textView9.visibility = GONE
            binding.lytOffender.spnState.visibility = GONE
            binding.lytOffender.textView10.visibility = GONE
            binding.lytOffender.spnTownship.visibility = GONE
            binding.lytOffender.textView11.visibility = GONE
            binding.lytOffender.edtColony.visibility = GONE
            binding.lytOffender.textView12.visibility = GONE
            binding.lytOffender.edtOffenderNoExt.visibility = GONE
            binding.lytOffender.textView13.visibility = GONE
            binding.lytOffender.edtOffenderNoInt.visibility = GONE
            binding.lytOffender.textView14.visibility = GONE
            binding.lytOffender.textView15.visibility = GONE
            binding.lytOffender.edtOffenderLicenseNo.visibility = GONE
            binding.lytOffender.textView16.visibility = GONE
            binding.lytOffender.spnLicenseType.visibility = GONE
            binding.lytOffender.textView17.visibility = GONE
            binding.lytOffender.spnLicenseIssuedIn.visibility = GONE
        }
        return binding.root
    }

    override fun initAdapters() {
        /* Init listeners */
        binding.rdbAbsentYes.setOnCheckedChangeListener(this)
        binding.lytOffender.spnState.onItemSelectedListener = this
        binding.lytOffender.spnTownship.onItemSelectedListener = this
        binding.lytOffender.spnLicenseType.onItemSelectedListener = this
        binding.lytOffender.spnLicenseIssuedIn.onItemSelectedListener = this
        binding.btnPay.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.lytOffender.edtOffenderName.doOnTextChanged { text, start, count, after -> SingletonInfraction.nameOffender = text?.trim().toString().toUpperCase() }
        binding.lytOffender.edtOffenderFln.doOnTextChanged { text, start, count, after -> SingletonInfraction.lastFatherName = text?.trim().toString().toUpperCase() }
        binding.lytOffender.edtOffenderMln.doOnTextChanged { text, start, count, after -> SingletonInfraction.lastMotherName = text?.trim().toString().toUpperCase() }
        binding.lytOffender.edtOffenderRfc.doOnTextChanged { text, start, count, after -> SingletonInfraction.rfcOffenfer = text?.trim().toString().toUpperCase() }
        binding.lytOffender.edtColony.doOnTextChanged { text, start, count, after -> SingletonInfraction.colonyOffender = text?.trim().toString().toUpperCase() }
        binding.lytOffender.edtOffenderNoExt.doOnTextChanged { text, start, count, after -> SingletonInfraction.noExtOffender = text?.trim().toString().toUpperCase() }
        binding.lytOffender.edtOffenderNoInt.doOnTextChanged { text, start, count, after -> SingletonInfraction.noIntOffender = text?.trim().toString().toUpperCase() }
        binding.lytOffender.edtOffenderLicenseNo.doOnTextChanged { text, start, count, after -> SingletonInfraction.noLicenseOffender = text?.trim().toString().toUpperCase() }
        /* Init adapters */
        iterator.value.getStatesList()
        iterator.value.getStatesIssuedList()
        binding.lytOffender.spnLicenseType.adapter = iterator.value.getTypeLicenseAdapter()
    }

    override fun fillFields() {
        binding.rdbAbsentNo.isChecked = !SingletonInfraction.isPersonAbstent
        binding.lytOffender.edtOffenderName.setText(SingletonInfraction.nameOffender)
        binding.lytOffender.edtOffenderFln.setText(SingletonInfraction.lastFatherName)
        binding.lytOffender.edtOffenderMln.setText(SingletonInfraction.lastMotherName)
        binding.lytOffender.edtOffenderRfc.setText(SingletonInfraction.rfcOffenfer)
        binding.lytOffender.edtOffenderNoExt.setText(SingletonInfraction.noExtOffender)
        binding.lytOffender.edtOffenderNoInt.setText(SingletonInfraction.noIntOffender)
        binding.lytOffender.edtOffenderLicenseNo.setText(SingletonInfraction.noLicenseOffender)
        binding.lytOffender.spnLicenseType.setSelection(iterator.value.getPositionTypeLicense(SingletonInfraction.typeLicenseOffender))
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        SingletonInfraction.isPersonAbstent = p1
        if (p1) {
            binding.lytOffender.root.visibility = GONE
            binding.lytOffender.edtOffenderName.setText("QUIEN")
            binding.lytOffender.edtOffenderFln.setText("RESULTE")
            binding.lytOffender.edtOffenderMln.setText("RESPONSABLE")
        } else {
            binding.lytOffender.root.visibility = VISIBLE
            binding.lytOffender.edtOffenderName.setText("")
            binding.lytOffender.edtOffenderFln.setText("")
            binding.lytOffender.edtOffenderMln.setText("")
        }
    }

    override fun onStatesReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnState.adapter = adapter
        binding.lytOffender.spnState.setSelection(iterator.value.getPositionState(SingletonInfraction.stateOffender))
    }

    override fun onStatesIssuedReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnLicenseIssuedIn.adapter = adapter
        binding.lytOffender.spnLicenseIssuedIn.setSelection(iterator.value.getPositionStateLicense(SingletonInfraction.licenseIssuedInOffender))
    }

    override fun onTownshipsReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnTownship.adapter = adapter
        binding.lytOffender.spnTownship.setSelection(iterator.value.getPositionTownship(SingletonInfraction.townshipOffender))
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_LONG)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.lytOffender.spnState.id -> {
                SingletonInfraction.stateOffender = iterator.value.statesList[p2]
                iterator.value.getTownshipsList(binding.lytOffender.spnState.selectedItemPosition)
            }
            binding.lytOffender.spnTownship.id -> {
                SingletonInfraction.townshipOffender = iterator.value.townshipsList[p2]
            }
            binding.lytOffender.spnLicenseType.id -> {
                SingletonInfraction.typeLicenseOffender = iterator.value.licenseTypeList[p2]
            }
            binding.lytOffender.spnLicenseIssuedIn.id -> {
                SingletonInfraction.licenseIssuedInOffender = iterator.value.stateIssuedLicenseList[p2]
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            /*binding.btnPay.id -> {
                if (validFields()) {
                    if (isCreation) iterator.value.saveData(false) else iterator.value.updateData()
                    isPayment = true
                    PaymentsTransfer.runTransaction(activity, SingletonInfraction.subTotalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_RANDOM else MODE_TX_PROD, this)
                }
            }*/
            binding.btnSave.id -> {
                if (validFields()) {
                    if (isCreation) iterator.value.saveData(true) else iterator.value.updateData()
                }
            }
        }
    }

    override fun onDataSaved() {
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_data_saved), Snackbar.LENGTH_SHORT)
        if (!SingletonInfraction.isPersonAbstent) {
            Handler().postDelayed({
                var builder = AlertDialogHelper.getGenericBuilder(
                        getString(R.string.w_dialog_title_payment), getString(R.string.w_want_to_pay), activity
                )
                builder.setPositiveButton("Aceptar") { _, _ ->
                    PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_RANDOM else MODE_TX_PROD, this)
                }
                builder.setNegativeButton("Cancelar") { _, _ ->
                    iterator.value.printTicket(activity)
                }
                builder.show()
            }, 2000)
        } else {
            iterator.value.printTicket(activity)
        }
    }

    override fun onDataUpdated() {
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_person_update), Snackbar.LENGTH_SHORT)
    }

    override fun validFields(): Boolean {
        var isValid = true
        if (!SingletonInfraction.isPersonAbstent) {
            when {
                SingletonInfraction.nameOffender.isEmpty() -> {
                    isValid = false
                    onError(getString(R.string.e_name_offender))
                }
                SingletonInfraction.lastFatherName.isEmpty() -> {
                    isValid = false
                    onError(getString(R.string.e_last_father_offender))
                }
                SingletonInfraction.lastMotherName.isEmpty() -> {
                    isValid = false
                    onError(getString(R.string.e_last_mother_offender))
                }
                else -> {
                    if (isDirectionAnswered()) {
                        when {
                            SingletonInfraction.stateOffender.id == 0 -> {
                                isValid = false
                                onError(getString(R.string.e_state_offender))
                            }
                            SingletonInfraction.townshipOffender.id_town == 0 -> {
                                isValid = false
                                onError(getString(R.string.e_township_offender))
                            }
                            SingletonInfraction.colonyOffender.isEmpty() -> {
                                isValid = false
                                onError(getString(R.string.e_colony_offender))
                            }
                            SingletonInfraction.noExtOffender.isEmpty() -> {
                                isValid = false
                                onError(getString(R.string.e_no_ext_offender))
                            }
                        }
                    }
                    if (isLicenseAnswered()) {
                        when {
                            SingletonInfraction.noLicenseOffender.isEmpty() -> {
                                isValid = false
                                onError(getString(R.string.e_no_license_offender))
                            }
                            SingletonInfraction.typeLicenseOffender.id == 0 -> {
                                isValid = false
                                onError(getString(R.string.e_type_license_offender))
                            }
                            SingletonInfraction.licenseIssuedInOffender.id == 0 -> {
                                isValid = false
                                onError(getString(R.string.e_issued_license_offender))
                            }
                        }
                    }
                }
            }
        }
        return isValid
    }

    override fun isDirectionAnswered(): Boolean {
        var isAnswered = false
        when {
            SingletonInfraction.stateOffender.id != 0 -> isAnswered = true
            SingletonInfraction.townshipOffender.id_town != 0 -> isAnswered = true
            SingletonInfraction.colonyOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.noExtOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.noIntOffender.isNotEmpty() -> isAnswered = true
        }
        return isAnswered
    }

    override fun isLicenseAnswered(): Boolean {
        var isAnswered = false
        when {
            SingletonInfraction.noLicenseOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.licenseIssuedInOffender.id != 0 -> isAnswered = true
            SingletonInfraction.typeLicenseOffender.id != 0 -> isAnswered = true
        }
        return isAnswered
    }

    override fun onTxApproved(txInfo: TransactionInfo) {
        isPaid = true
        iterator.value.savePayment(txInfo)
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_infraction_pay), Snackbar.LENGTH_SHORT)
    }

    override fun onTxVoucherPrinted() {
        if (isPaid) {
            activity.finish()
        } else {
            var builder = AlertDialogHelper.getGenericBuilder(
                    getString(R.string.w_dialog_title_payment_failed), getString(R.string.w_reintent_transaction), activity
            )
            builder.setPositiveButton("Aceptar") { _, _ ->
                PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_RANDOM else MODE_TX_PROD, this)
            }
            builder.setNegativeButton("Cancelar") { _, _ ->
                iterator.value.printTicket(activity)
            }
            builder.show()
        }
    }

    override fun onTxFailed(message: String) {
        isPaid = false
    }

    override fun onTxVoucherFailer(message: String) {
        SnackbarHelper.showErrorSnackBar(activity, message, Snackbar.LENGTH_SHORT)
    }

    override fun onTicketPrinted() {
        if (!isTicketCopy) {
            var builder = AlertDialogHelper.getGenericBuilder(
                    getString(R.string.w_dialog_title_reprint_ticket), getString(R.string.w_want_to_reprint_ticket), activity
            )
            builder.setPositiveButton("Aceptar") { _, _ ->
                isTicketCopy = true
                iterator.value.printTicket(activity)
            }
            builder.setNegativeButton("Cancelar") { _, _ ->
                activity.finish()
            }
            builder.show()
        } else {
            activity.finish()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param isCreation Parameter 1.
         * @return A new instance of fragment OffenderFragment.
         */
        @JvmStatic
        fun newInstance(isCreation: Boolean) =
                OffenderFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_IS_CREATION, isCreation)
                    }
                }
    }

}
