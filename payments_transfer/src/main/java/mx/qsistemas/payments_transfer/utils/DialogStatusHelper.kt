package mx.qsistemas.payments_transfer.utils

import android.content.Context
import mx.qsistemas.payments_transfer.dialogs.StatusDialog

object DialogStatusHelper {

    private var dialog: StatusDialog? = null

    fun showDialog(context: Context, title: String) {
        if (dialog == null) {
            dialog = StatusDialog(context, title)
        } else {
            dialog!!.updateTitle(title)
        }
    }

    fun closeDialog() {
        try {
            if (dialog != null) {
                dialog?.dismiss()
                dialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}