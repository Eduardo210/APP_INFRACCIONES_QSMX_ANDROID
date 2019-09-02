package mx.qsistemas.infracciones.modules.create.fr_offender

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.databinding.FragmentOffenderBinding
import mx.qsistemas.infracciones.dialogs.DetailPaymentCallback
import mx.qsistemas.infracciones.dialogs.DetailPaymentDialog
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.modules.search.fr_search.TOKEN_INFRACTION
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonInfraction.tokenInfraction
import mx.qsistemas.infracciones.utils.FS_COL_CITIES
import mx.qsistemas.infracciones.utils.Utils
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROBE_AUTH_ALWAYS
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROD
import org.apache.commons.lang.time.DateUtils
import java.text.SimpleDateFormat
import java.util.*

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
        OnClickListener, IPaymentsTransfer.TransactionListener, DetailPaymentCallback {
    private var isCreation: Boolean = true

    private val CURRENT_DATE = Date()
    private var isPaid: Boolean = false

    private var amountToPay = "0"
    private var discountPayment = "0"
    private var totalAmount = "0"

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
            binding.lytOffender.edtOffenderRfc.visibility = GONE
            binding.lytOffender.spnState.visibility = GONE
            binding.lytOffender.spnTownship.visibility = GONE
            binding.lytOffender.spnZipCode.visibility = GONE
            binding.lytOffender.spnColony.visibility = GONE
            binding.lytOffender.edtStreet.visibility = GONE
            binding.lytOffender.edtOffenderNoExt.visibility = GONE
            binding.lytOffender.edtOffenderNoInt.visibility = GONE
            binding.lytOffender.edtOffenderLicenseNo.visibility = GONE
            binding.lytOffender.spnLicenseType.visibility = GONE
            binding.lytOffender.textView7.visibility = GONE
            binding.lytOffender.textView8.visibility = GONE
            binding.lytOffender.textView9.visibility = GONE
            binding.lytOffender.textView10.visibility = GONE
            binding.lytOffender.textView12.visibility = GONE
            binding.lytOffender.textView13.visibility = GONE
            binding.lytOffender.textView14.visibility = GONE
            binding.lytOffender.textView15.visibility = GONE
            binding.lytOffender.textView16.visibility = GONE
            binding.lytOffender.textView17.visibility = GONE
            binding.lytOffender.textView18.visibility = GONE
            binding.lytOffender.textView19.visibility = GONE
            binding.lytOffender.textView20.visibility = GONE
            binding.lytOffender.spnLicenseIssuedIn.visibility = GONE
        }
        return binding.root
    }

    override fun initAdapters() {
        /* Init listeners */
        binding.rdbAbsentYes.setOnCheckedChangeListener(this)
        binding.lytOffender.spnState.onItemSelectedListener = this
        binding.lytOffender.spnTownship.onItemSelectedListener = this
        binding.lytOffender.spnZipCode.onItemSelectedListener = this
        binding.lytOffender.spnColony.onItemSelectedListener = this
        binding.lytOffender.spnLicenseType.onItemSelectedListener = this
        binding.lytOffender.spnLicenseIssuedIn.onItemSelectedListener = this
        binding.btnPay.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.lytOffender.edtOffenderName.doOnTextChanged { text, start, count, after -> SingletonInfraction.nameOffender = text?.trim().toString() }
        binding.lytOffender.edtOffenderFln.doOnTextChanged { text, start, count, after -> SingletonInfraction.lastFatherName = text?.trim().toString() }
        binding.lytOffender.edtOffenderMln.doOnTextChanged { text, start, count, after -> SingletonInfraction.lastMotherName = text?.trim().toString() }
        binding.lytOffender.edtOffenderRfc.doOnTextChanged { text, start, count, after -> SingletonInfraction.rfcOffenfer = text?.trim().toString() }
        binding.lytOffender.edtStreet.doOnTextChanged { text, start, count, after -> SingletonInfraction.streetOffender = text?.trim().toString() }
        binding.lytOffender.edtOffenderNoExt.doOnTextChanged { text, start, count, after -> SingletonInfraction.noExtOffender = text?.trim().toString() }
        binding.lytOffender.edtOffenderNoInt.doOnTextChanged { text, start, count, after -> SingletonInfraction.noIntOffender = text?.trim().toString() }
        binding.lytOffender.edtOffenderLicenseNo.doOnTextChanged { text, start, count, after -> SingletonInfraction.noLicenseOffender = text?.trim().toString() }
        /* Init adapters */
        iterator.value.getStatesList()
        iterator.value.getStatesIssuedList()
        iterator.value.getTypeLicenseAdapter()
        iterator.value.getHolidays()
    }

    override fun fillFields() {
        binding.rdbAbsentNo.isChecked = !SingletonInfraction.isPersonAbstent
        binding.lytOffender.edtOffenderName.setText(SingletonInfraction.nameOffender)
        binding.lytOffender.edtOffenderFln.setText(SingletonInfraction.lastFatherName)
        binding.lytOffender.edtOffenderMln.setText(SingletonInfraction.lastMotherName)
        binding.lytOffender.edtOffenderRfc.setText(SingletonInfraction.rfcOffenfer)
        binding.lytOffender.edtStreet.setText(SingletonInfraction.streetOffender)
        binding.lytOffender.edtOffenderNoExt.setText(SingletonInfraction.noExtOffender)
        binding.lytOffender.edtOffenderNoInt.setText(SingletonInfraction.noIntOffender)
        binding.lytOffender.edtOffenderLicenseNo.setText(SingletonInfraction.noLicenseOffender)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        SingletonInfraction.isPersonAbstent = p1
        if (p1) {
            binding.lytOffender.root.visibility = GONE
            binding.lytOffender.edtOffenderName.setText("Quien")
            binding.lytOffender.edtOffenderFln.setText("Resulte")
            binding.lytOffender.edtOffenderMln.setText("Responsable")
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

    override fun onTownshipsReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnTownship.adapter = adapter
        binding.lytOffender.spnTownship.setSelection(iterator.value.getPositionTownship(SingletonInfraction.townshipOffender))
    }

    override fun onZipCodesReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnZipCode.adapter = adapter
        binding.lytOffender.spnZipCode.setSelection(iterator.value.getPositionZipCode(SingletonInfraction.zipCodeOffender))
    }

    override fun onStatesIssuedReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnLicenseIssuedIn.adapter = adapter
        binding.lytOffender.spnLicenseIssuedIn.setSelection(iterator.value.getPositionStateLicense(SingletonInfraction.licenseIssuedInOffender))
    }

    override fun onColoniesReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnColony.adapter = adapter
        binding.lytOffender.spnColony.setSelection(iterator.value.getPositionColony(SingletonInfraction.colonnyInfraction))
    }

    override fun onTypeLicenseReady(adapter: ArrayAdapter<String>) {
        binding.lytOffender.spnLicenseType.adapter = adapter
        binding.lytOffender.spnLicenseType.setSelection(iterator.value.getPositionTypeLicense(SingletonInfraction.typeLicenseOffender))
    }

    override fun onError(msg: String) {
        SnackbarHelper.showErrorSnackBar(activity, msg, Snackbar.LENGTH_LONG)
        if (msg == getString(R.string.pt_e_print_other_error)) {
            val builder = AlertDialogHelper.getGenericBuilder(
                    getString(R.string.w_dialog_title_print_ticket), getString(R.string.w_options_reprint), activity
            )
            builder.setPositiveButton("Boleta") { _, _ ->
                activity.showLoader(getString(R.string.l_preparing_printer))
                iterator.value.printTicket(activity)
            }
            builder.show()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            binding.lytOffender.spnState.id -> {
                SingletonInfraction.stateOffender = iterator.value.statesList[p2]
                iterator.value.getTownshipsList(SingletonInfraction.stateOffender.documentReference)
            }
            binding.lytOffender.spnTownship.id -> {
                SingletonInfraction.townshipOffender = iterator.value.townshipList[p2]
                iterator.value.getZipCodesList(SingletonInfraction.townshipOffender.childReference)
            }
            binding.lytOffender.spnZipCode.id -> {
                SingletonInfraction.zipCodeOffender = iterator.value.zipCodesList[p2]
                iterator.value.getColoniesList(SingletonInfraction.zipCodeOffender.childReference)
            }
            binding.lytOffender.spnColony.id -> {
                SingletonInfraction.colonyOffender = iterator.value.coloniesList[p2]
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
            binding.btnSave.id -> {
                if (validFields()) {
                    val builder = AlertDialogHelper.getGenericBuilder(
                            getString(R.string.w_dialog_title), getString(R.string.w_verify_printer), activity
                    )
                    builder.setPositiveButton("Aceptar") { _, _ ->
                        if (isCreation) {
                            iterator.value.saveData(true)
                        } else {
                            tokenInfraction = TOKEN_INFRACTION
                            iterator.value.updateData()
                        }
                    }
                    builder.show()
                }
            }
        }
    }

    override fun onDataSaved() {
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_data_saved), Snackbar.LENGTH_SHORT)
        if (!SingletonInfraction.isPersonAbstent) {
            Handler().postDelayed({
                val dialog = DetailPaymentDialog()
                dialog.listener = this
                dialog.isCancelable = false
                dialog.show(activity.supportFragmentManager, DetailPaymentDialog::class.java.simpleName)
            }, 2000)
        } else {
            activity.showLoader(getString(R.string.l_preparing_printer))
            iterator.value.printTicket(activity)
        }
    }

    override fun onAcceptPayment() {
        PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
    }

    override fun onDeclinePayment() {
        activity.showLoader(getString(R.string.l_preparing_printer))
        iterator.value.printTicket(activity)
    }

    override fun onDataDuplicate() {
        if (!SingletonInfraction.isPersonAbstent) {
            Handler().postDelayed({
                val dialog = DetailPaymentDialog()
                dialog.listener = this
                dialog.isCancelable = false
                dialog.show(activity.supportFragmentManager, DetailPaymentDialog::class.java.simpleName)
            }, 2000)
        } else {
            activity.showLoader(getString(R.string.l_preparing_printer))
            iterator.value.printTicket(activity)
        }
    }

    override fun onDataUpdated() {
        activity.showLoader(getString(R.string.l_preparing_amout))
        Application.firestore?.collection(FS_COL_CITIES)?.document(Application.prefs?.loadData(R.string.sp_id_township, "")!!)?.get()?.addOnSuccessListener { townshipSnapshot ->
            if (townshipSnapshot == null) {
                Log.e(this.javaClass.simpleName, Application.getContext().getString(R.string.e_firestore_not_available))
                onError(Application.getContext().getString(R.string.e_firestore_not_available))
                activity.hideLoader()
            } else {
                val township = townshipSnapshot.toObject(Townships::class.java) ?: Townships()
                var hasSurcharges = false
                township.discount.toSortedMap(reverseOrder()).entries.forEachIndexed { index, mutableEntry ->
                    val expDate = SimpleDateFormat("yyyy-MM-dd").parse(Utils.getFutureWorkingDay(mutableEntry.value[mutableEntry.value.size - 1], iterator.value.holidayList))
                    val actualDate = Date()
                    if (actualDate.before(expDate) || DateUtils.isSameDay(actualDate, expDate)) {
                        hasSurcharges = false
                    } else {
                        hasSurcharges = true
                    }
                    // Get last day of validity
                    /*val discount = totalImport * mutableEntry.key.replace("%", "").toFloat() / 100
                    val total = totalImport - discount
                    val codeCaptureLine = Utils.generateCaptureLine(newFolio.replace("-", ""), expDate, "%.2f".format(total), "2")
                    captureLineList.add(InfringementCapturelines(0, codeCaptureLine, SimpleDateFormat("dd/MM/yyyy").format(dateFormat.parse(expDate)),
                            "%.2f".format(total).toFloat(), "Bancaria", index + 1, SingletonInfraction.idNewInfraction.toString(), mutableEntry.key))*/
                }
            }
        }
        var compare_date: Int
        var haveToPay: Boolean = true
        val expDate50: Date? = SingletonInfraction.captureLineii
        val expDateFull: Date? = SingletonInfraction.captureLineiii

        compare_date = CURRENT_DATE.compareTo(expDate50)//expDate50.compareTo(CURRENT_DATE)
        totalAmount = SingletonInfraction.amountCaptureLineiii.toString()
        if (compare_date <= 0) { //Si hoy es menor o igual a la fecha limite
            //Tiene el descuento del 50%
            amountToPay = "%.2f".format(SingletonInfraction.amountCaptureLineii)
            discountPayment = (SingletonInfraction.amountCaptureLineiii - SingletonInfraction.amountCaptureLineii).toString()
        } else {
            //No tiene descuento
            discountPayment = "0"
            compare_date = CURRENT_DATE.compareTo(expDateFull)//expDateFull.compareTo(CURRENT_DATE)
            if (compare_date <= 0) {
                amountToPay = "%.2f".format(SingletonInfraction.amountCaptureLineiii)
            } else {
                haveToPay = false
            }
        }
        SingletonInfraction.totalInfraction = amountToPay.replace(",", ".")
        //TODO: llenar los datos coorrespondietnes para los datos del pago en server
        if (haveToPay) {
            PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
        } else {
            SnackbarHelper.showErrorSnackBar(activity, "La infracción cuenta con recargos. Pagar en ventanilla", Snackbar.LENGTH_LONG)
        }
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
                            SingletonInfraction.stateOffender.documentReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_state_offender))
                                1
                            }
                            SingletonInfraction.townshipOffender.childReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_township_offender))
                            }
                            SingletonInfraction.zipCodeOffender.childReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_zip_code))
                            }
                            SingletonInfraction.colonyOffender.childReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_colony_offender))
                            }
                            SingletonInfraction.streetOffender.isEmpty() -> {
                                isValid = false
                                onError(getString(R.string.e_street_offender))
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
                            SingletonInfraction.typeLicenseOffender.documentReference == null -> {
                                isValid = false
                                onError(getString(R.string.e_type_license_offender))
                            }
                            SingletonInfraction.licenseIssuedInOffender.documentReference == null -> {
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
            SingletonInfraction.stateOffender.documentReference != null -> isAnswered = true
            SingletonInfraction.townshipOffender.childReference != null -> isAnswered = true
            SingletonInfraction.zipCodeOffender.childReference != null -> isAnswered = true
            SingletonInfraction.colonyOffender.childReference != null -> isAnswered = true
            SingletonInfraction.streetOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.noExtOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.noIntOffender.isNotEmpty() -> isAnswered = true
        }
        return isAnswered
    }

    override fun isLicenseAnswered(): Boolean {
        var isAnswered = false
        when {
            SingletonInfraction.noLicenseOffender.isNotEmpty() -> isAnswered = true
            SingletonInfraction.licenseIssuedInOffender.documentReference != null -> isAnswered = true
            SingletonInfraction.typeLicenseOffender.documentReference != null -> isAnswered = true
        }
        return isAnswered
    }

    override fun onTxApproved(txInfo: TransactionInfo) {
        isPaid = true
        if (isCreation) {
            iterator.value.savePayment(txInfo)
            SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_infraction_pay), Snackbar.LENGTH_SHORT)
        } else {
            iterator.value.savePaymentToService(tokenInfraction, SingletonInfraction.folioInfraction, txInfo, totalAmount, discountPayment, "", SingletonInfraction.totalInfraction)
        }
    }

    override fun onTxVoucherPrinted() {
        if (isPaid) {
            if (isCreation) {
                activity.showLoader(getString(R.string.l_preparing_printer))
                iterator.value.printTicket(activity)
            } else {
                SingletonInfraction.cleanSingleton()
                Alarms()
                activity.finish()
            }
        }
    }

    override fun onTxFailed(retry: Boolean, message: String) {
        isPaid = false
        if (retry) {
            if (Looper.myLooper() == null)
                Looper.prepare()
            val builder = AlertDialogHelper.getErrorBuilder(
                    message.toUpperCase(), getString(R.string.w_reintent_transaction), activity
            )
            builder.setPositiveButton("Sí") { _, _ ->
                PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
            }
            builder.setNegativeButton("No") { _, _ ->
                activity.runOnUiThread {
                    activity.showLoader(getString(R.string.l_preparing_printer))
                    iterator.value.printTicket(activity)
                }
            }
            builder.show()
            if (Looper.myLooper() != null)
                Looper.loop()
        } else {
            if (isCreation) {
                activity.showLoader(getString(R.string.l_preparing_printer))
                iterator.value.printTicket(activity)
            }
        }
    }

    override fun onCtlsDoubleTap() {
        activity.runOnUiThread {
            activity.showLoader(getString(R.string.l_waiting_confirm))
            Handler().postDelayed({ PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this) }, 3500)
        }
    }

    override fun onTxVoucherFailer(message: String) {
        SnackbarHelper.showErrorSnackBar(activity, message, Snackbar.LENGTH_SHORT)
        val builder = AlertDialogHelper.getGenericBuilder(
                getString(R.string.w_dialog_title_print_ticket), getString(R.string.w_options_reprint), activity
        )
        builder.setPositiveButton("Boleta") { _, _ ->
            activity.showLoader(getString(R.string.l_preparing_printer))
            iterator.value.printTicket(activity)
        }
        if (isPaid) {
            builder.setNegativeButton("Voucher Banc.") { _, _ ->
                iterator.value.reprintVoucher(activity, this)
            }
        }
        builder.show()
    }

    override fun onTicketPrinted() {
        if (!isTicketCopy) {
            val builder = AlertDialogHelper.getGenericBuilder(
                    getString(R.string.w_dialog_title_reprint_ticket), getString(R.string.w_want_to_reprint_ticket), activity
            )
            builder.setPositiveButton("Sí") { _, _ ->
                isTicketCopy = true
                activity.showLoader(getString(R.string.l_preparing_printer))
                iterator.value.printTicket(activity)
            }
            builder.show()
        } else {
            SingletonInfraction.cleanSingleton()
            Alarms()
            activity.finish()
        }
    }

    override fun onResultSavePayment(msg: String, flag: Boolean) {
        activity.hideLoader()
        if (flag) {
            SnackbarHelper.showSuccessSnackBar(activity, "El pago se guardó satisfactoriamente.", Snackbar.LENGTH_SHORT)
        } else {
            SnackbarHelper.showErrorSnackBar(activity, "Error al guardar datos de pago en servidor.", Snackbar.LENGTH_SHORT)
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
