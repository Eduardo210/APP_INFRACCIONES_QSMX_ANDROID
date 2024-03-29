package mx.qsistemas.infracciones.helpers

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import mx.qsistemas.infracciones.R

class AlertDialogHelper {

    companion object {
        fun getGenericBuilder(title: String, message: String, activity: Activity): AlertDialog.Builder {
            val builder = AlertDialog.Builder(activity)
            builder.setCancelable(false)
            // Display a message on alert dialog
            builder.setMessage(message)
            val view = activity.layoutInflater.inflate(R.layout.dialog_title, null)
            view.setBackgroundResource(R.drawable.title_gradient)
            val titleText = view.findViewById(R.id.custom_dialog_title) as TextView
            titleText.text = title
            builder.setCustomTitle(view)
            return builder
        }

        fun getErrorBuilder(title: String, message: String, activity: Activity): AlertDialog.Builder {
            val builder = AlertDialog.Builder(activity)
            builder.setCancelable(false)
            // Display a message on alert dialog
            builder.setMessage(message)
            val view = activity.layoutInflater.inflate(R.layout.dialog_title, null)
            view.setBackgroundResource(R.drawable.title_gradient_error)
            val titleText = view.findViewById(R.id.custom_dialog_title) as TextView
            titleText.text = title
            builder.setCustomTitle(view)
            return builder
        }
    }
}