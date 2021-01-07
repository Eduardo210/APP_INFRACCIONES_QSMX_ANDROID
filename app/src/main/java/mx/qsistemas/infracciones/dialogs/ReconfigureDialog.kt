package mx.qsistemas.infracciones.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.DialogReconfigureBinding

class ReconfigureDialog : DialogFragment(), View.OnClickListener {

    var listener: ReconfigurationCallback? = null
    private lateinit var binding: DialogReconfigureBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_reconfigure, container, false)
        binding.dialogTitle.customDialogTitle.text = getString(R.string.t_reconfiguration)
        binding.btnAccept.setOnClickListener(this)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnAccept.id -> {
                if (binding.edtPswConfig.text.toString().isBlank()) {
                    binding.edtPswConfig.error = Application.getContext().getString(R.string.e_pss_incorrect)
                } else {
                    listener?.onPasswordConfirm(binding.edtPswConfig.text.toString())
                    dismiss()
                }
            }
        }
    }
}

interface ReconfigurationCallback {
    fun onPasswordConfirm(psd: String)
}