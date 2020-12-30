package mx.qsistemas.infracciones.modules.create.fr_payer


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.databinding.FragmentPayerBinding
import mx.qsistemas.infracciones.dialogs.DetailPaymentCallback
import mx.qsistemas.infracciones.dialogs.DetailPaymentDialog
import mx.qsistemas.infracciones.helpers.AlertDialogHelper
import mx.qsistemas.infracciones.helpers.SnackbarHelper
import mx.qsistemas.infracciones.modules.create.CreateInfractionActivity
import mx.qsistemas.infracciones.modules.search.fr_search.TOKEN_INFRACTION
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.net.result_web.detail_result.NewCaptureLines
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.singletons.SingletonInfraction.tokenInfraction
import mx.qsistemas.infracciones.utils.FS_COL_CITIES
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROBE_AUTH_ALWAYS
import mx.qsistemas.payments_transfer.utils.MODE_TX_PROD
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * Use the [PayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_IS_CREATION = "is_creation"

class PayerFragment : Fragment(), PayerContracts.Presenter, View.OnClickListener, IPaymentsTransfer.TransactionListener,
        DetailPaymentCallback, RadioGroup.OnCheckedChangeListener {

    private val format = SimpleDateFormat("yyyy-MM-dd")
    private val CURRENT_DATE = format.parse(format.format(Date()))
    private var isCreation: Boolean = true
    private var isPaid: Boolean = false
    private var isTicketCopy: Boolean = false

    private val iterator = lazy { PayerIterator(this) }
    private lateinit var binding: FragmentPayerBinding
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payer, container, false)
        initViews()
        iterator.value.getHolidays()
        return binding.root
    }

    private fun initViews() {
        binding.edtPayerName.doOnTextChanged { text, _, _, _ -> SingletonInfraction.payerName = text.toString().trim() }
        binding.edtPayerFln.doOnTextChanged { text, _, _, _ -> SingletonInfraction.payerLastName = text.toString().trim() }
        binding.edtPayerMln.doOnTextChanged { text, _, _, _ -> SingletonInfraction.payerMotherLastName = text.toString().trim() }
        binding.edtTaxDenomination.doOnTextChanged { text, _, _, _ -> SingletonInfraction.payerTaxDenomination = text.toString().trim() }
        binding.edtPayerRfc.doOnTextChanged { text, _, _, _ -> SingletonInfraction.payerRfc = text.toString() }
        binding.edtPayerEmail.doOnTextChanged { text, _, _, _ -> SingletonInfraction.payerEmail = text.toString().trim() }
        binding.btnDeleteData.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        /* Set Adapters */
        binding.edtPayerName.setText(if (SingletonInfraction.payerName.isNotBlank()) SingletonInfraction.payerName else SingletonInfraction.nameOffender)
        binding.edtPayerFln.setText(if (SingletonInfraction.payerLastName.isNotBlank()) SingletonInfraction.payerLastName else SingletonInfraction.lastFatherName)
        binding.edtPayerMln.setText(if (SingletonInfraction.payerMotherLastName.isNotBlank()) SingletonInfraction.payerMotherLastName else SingletonInfraction.lastMotherName)
        binding.edtTaxDenomination.setText(SingletonInfraction.payerTaxDenomination)
        binding.edtPayerRfc.setText(SingletonInfraction.payerRfc)
        binding.edtPayerEmail.setText(SingletonInfraction.payerEmail)
        binding.rdgGenerateBill.setOnCheckedChangeListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnDeleteData.id -> {
                binding.edtPayerName.setText("")
                binding.edtPayerFln.setText("")
                binding.edtPayerMln.setText("")
                binding.edtTaxDenomination.setText("")
                binding.edtPayerRfc.setText("")
                binding.edtPayerEmail.setText("")
            }
            binding.btnSave.id -> {
                if (validFields()) {
                    val builder = AlertDialogHelper.getGenericBuilder(
                            getString(R.string.w_dialog_title), getString(R.string.w_verify_printer), activity
                    )
                    builder.setPositiveButton("Aceptar") { _, _ ->
                        if (isCreation) {
                            iterator.value.saveData()
                        } else {
                            activity.showLoader(getString(R.string.l_upload_person))
                            tokenInfraction = TOKEN_INFRACTION
                            iterator.value.updatePayerData()
                        }
                    }
                    builder.show()
                }
            }
        }
    }

    override fun onCheckedChanged(p0: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rdb_generate_yes -> {
                dataBilling(true)
                SingletonInfraction.isGenerateBill = true
            }

            R.id.rdb_generate_no -> {
                dataBilling(false)
                SingletonInfraction.isGenerateBill = false
            }
        }
    }

    private fun dataBilling(show: Boolean) {
        if (show) {
            binding.constrainedBill.visibility = View.VISIBLE
        } else {
            binding.constrainedBill.visibility = View.GONE
        }
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

    override fun validFields(): Boolean {
        var isValid = true
        when {
            SingletonInfraction.payerName.isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_payer_name))
            }
            SingletonInfraction.payerLastName.isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_payer_last_name))
            }
            SingletonInfraction.payerMotherLastName.isEmpty() -> {
                isValid = false
                onError(getString(R.string.e_payer_mother_last_name))
            }
            SingletonInfraction.isGenerateBill -> {
                when {
                    SingletonInfraction.payerTaxDenomination.isEmpty() -> {
                        isValid = false
                        onError(getString(R.string.e_payer_tax_denomination))
                    }
                    SingletonInfraction.payerRfc.isEmpty() -> {
                        isValid = false
                        onError(getString(R.string.e_payer_rfc))
                    }
                    SingletonInfraction.payerEmail.isEmpty() -> {
                        isValid = false
                        onError(getString(R.string.e_payer_email))
                    }
                }
            }
        }
        return isValid
    }

    override fun onDataSaved() {
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_data_saved), Snackbar.LENGTH_SHORT)
        Handler().postDelayed({
            val dialog = DetailPaymentDialog()
            dialog.listener = this
            dialog.showDeclineOption = false
            dialog.isCancelable = false
            dialog.show(activity.supportFragmentManager, DetailPaymentDialog::class.java.simpleName)
        }, 2000)
    }

    override fun onDataDuplicate() {
        Handler().postDelayed({
            val dialog = DetailPaymentDialog()
            dialog.listener = this
            dialog.showDeclineOption = false
            dialog.isCancelable = false
            dialog.show(activity.supportFragmentManager, DetailPaymentDialog::class.java.simpleName)
        }, 2000)
    }

    override fun onDataUpdated() {
        activity.showLoader(getString(R.string.l_preparing_amout))
        val dialog = DetailPaymentDialog()
        dialog.listener = this
        dialog.showDeclineOption = false
        dialog.isCancelable = false
        var compareDate: Int
        var captureSelected = NewCaptureLines()
        val newCaptureLines = mutableListOf<NewCaptureLines>()
        /* Step 1. Create new list of capture lines with dates */
        SingletonInfraction.captureLines.forEach {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd")
            val newDate = originalFormat.parse(it?.date)
            newCaptureLines.add(NewCaptureLines(it?.amount, it?.key, it?.order, newDate, it?.discount_label))
        }
        newCaptureLines.sortBy { captureLinesItem -> captureLinesItem.date }
        run loop@{
            newCaptureLines.forEach { cDate ->
                compareDate = CURRENT_DATE.compareTo(cDate.date)
                //  0 comes when two date are same,
                //  1 comes when date1 is higher then date2
                // -1 comes when date1 is lower then date2
                if (compareDate <= 0) { //Si hoy es menor o igual a la fecha límite
                    captureSelected = cDate
                    return@loop
                }
            }
        }
        if (captureSelected.amount.isNullOrEmpty()) {
            captureSelected = newCaptureLines[newCaptureLines.lastIndex]
            //Hacer operación para calcular los recargos
            Application.firestore.collection(FS_COL_CITIES).document(Application.prefs.loadData(R.string.sp_id_township, "")!!).get().addOnSuccessListener { townshipSnapshot ->
                if (townshipSnapshot == null) {
                    Log.e(this.javaClass.simpleName, Application.getContext().getString(R.string.e_firestore_not_available))
                    onError(Application.getContext().getString(R.string.e_firestore_not_available))
                    activity.hideLoader()
                } else {
                    val township = townshipSnapshot.toObject(Townships::class.java) ?: Townships()
                    val diff = Date().time - captureSelected.date?.time!!
                    val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
                    SingletonInfraction.surchargesInfraction = "%.2f".format(days * township.surcharges_rate)
                    SingletonInfraction.totalInfraction = "%.2f".format(SingletonInfraction.subTotalInfraction.toFloat() + SingletonInfraction.surchargesInfraction.toFloat()).replace(",", ".")
                    dialog.show(activity.supportFragmentManager, DetailPaymentDialog::class.java.simpleName)
                }
            }
        } else {
            SingletonInfraction.discountInfraction = "%.2f".format(SingletonInfraction.subTotalInfraction.toFloat() - captureSelected.amount!!.toFloat())
            SingletonInfraction.totalInfraction = "%.2f".format(captureSelected.amount!!.toFloat())
            dialog.show(activity.supportFragmentManager, DetailPaymentDialog::class.java.simpleName)
        }
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

    override fun onPaymentSaved() {
        activity.hideLoader()
        SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_payment_saved), Snackbar.LENGTH_SHORT)
    }

    override fun onTxApproved(txInfo: TransactionInfo) {
        isPaid = true
        if (isCreation) {
            iterator.value.savePayment(txInfo)
            SnackbarHelper.showSuccessSnackBar(activity, getString(R.string.s_infraction_pay), Snackbar.LENGTH_SHORT)
        } else {
            val dis_ = if (SingletonInfraction.discountInfraction.isNotBlank()) SingletonInfraction.discountInfraction else "0.0"
            val surcharges = if (SingletonInfraction.surchargesInfraction.isNotBlank()) SingletonInfraction.surchargesInfraction else "0.0"
            iterator.value.savePaymentToService(tokenInfraction, SingletonInfraction.folioInfraction, txInfo, SingletonInfraction.subTotalInfraction, dis_,
                    surcharges, SingletonInfraction.totalInfraction)
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

    override fun onTxVoucherFailed(message: String) {
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

    override fun onDeclinePayment() {
        activity.showLoader(getString(R.string.l_preparing_printer))
        iterator.value.printTicket(activity)
    }

    override fun onAcceptPayment() {
        PaymentsTransfer.runTransaction(activity, SingletonInfraction.totalInfraction, if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else MODE_TX_PROD, this)
    }

    companion object {
        @JvmStatic
        fun newInstance(isCreation: Boolean) =
                PayerFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_IS_CREATION, isCreation)
                    }
                }
    }
}
