package mx.qsistemas.infracciones.helpers

import android.app.Activity
import android.view.Gravity
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R

class SnackbarHelper {
    companion object {
        fun showErrorSnackBar(rootView: Activity, message: String, length: Int) {
            val snack = Snackbar.make(rootView.window.decorView, message, length)
            val view = snack.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM
            view.layoutParams = params
            view.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.colorRedTransparent))
            snack.show()
        }

        fun showSuccessSnackBar(rootView: Activity, message: String, length: Int) {
            val snack = Snackbar.make(rootView.window.decorView, message, length)
            val view = snack.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM
            view.layoutParams = params
            view.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.colorPrimaryTransparent))
            snack.show()
        }
    }
}