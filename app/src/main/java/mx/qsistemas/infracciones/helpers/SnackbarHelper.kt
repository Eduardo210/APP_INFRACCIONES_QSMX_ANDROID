package mx.qsistemas.infracciones.helpers

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R

class SnackbarHelper {
    companion object {
        fun showErrorSnackBar(rootView: Activity, message: String, length: Int) {
            val parentView = rootView.findViewById<View>(android.R.id.content)
            val snack = Snackbar.make(parentView, message, length)
            val view = snack.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM
            params.setMargins(16, 0, 16, 0)
            view.layoutParams = params
            view.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.colorRed))
            snack.show()
        }

        fun showSuccessSnackBar(rootView: Activity, message: String, length: Int) {
            val parentView = rootView.findViewById<View>(android.R.id.content)
            val snack = Snackbar.make(parentView, message, length)
            val view = snack.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM
            params.setMargins(16, 0, 16, 0)
            view.layoutParams = params
            view.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.colorPrimary))
            snack.show()
        }
    }
}