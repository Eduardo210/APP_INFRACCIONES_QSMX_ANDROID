package mx.qsistemas.infracciones.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_title.view.*
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.DialogDetailPaymentBinding
import mx.qsistemas.infracciones.singletons.SingletonInfraction


class DetailPaymentDialog : DialogFragment() {

    var listener: DetailPaymentCallback? = null
    var showDeclineOption: Boolean = true
    private lateinit var binding: DialogDetailPaymentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_detail_payment, container, false)
        binding.dialogTitle.custom_dialog_title.text = getString(R.string.w_dialog_title_payment)
        binding.btnAccept.setOnClickListener {
            listener?.onAcceptPayment()
            dismiss()
        }
        if (showDeclineOption) {
            binding.btnCancel.setOnClickListener {
                listener?.onDeclinePayment()
                dismiss()
            }
        } else {
            binding.btnCancel.visibility = View.GONE
        }
        binding.txtNameOffender.text = SingletonInfraction.nameOffender + " " + SingletonInfraction.lastFatherName + " " + SingletonInfraction.lastMotherName
        var totalUmas = 0
        SingletonInfraction.motivationList.forEach {
            totalUmas += it.fraction.uma
        }
        binding.txtTotalUmas.text = totalUmas.toString()
        binding.txtSubtotalPrice.text = SingletonInfraction.subTotalInfraction
        binding.txtTotalDiscount.text = if (SingletonInfraction.discountInfraction.isNotBlank()) SingletonInfraction.discountInfraction else "S/N"
        binding.txtTotalSurcharges.text = if (SingletonInfraction.surchargesInfraction.isNotBlank()) SingletonInfraction.surchargesInfraction else "S/N"
        binding.txtTotalPrice.text = SingletonInfraction.totalInfraction
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }
}

interface DetailPaymentCallback {
    fun onDeclinePayment()
    fun onAcceptPayment()
}