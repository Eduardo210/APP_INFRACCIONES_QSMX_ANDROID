package mx.qsistemas.infracciones.helpers

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import androidx.core.content.ContextCompat
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R

class AlertDialogHelper {

    companion object {
        fun getGenericBuilder(title: String, message: String, activity: Activity): AlertDialog.Builder {
            val builder = AlertDialog.Builder(activity)
            builder.setCancelable(false)
            // Display a message on alert dialog
            builder.setMessage(message)
            val view = activity.layoutInflater.inflate(R.layout.dialog_title, null)
            view.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.colorPrimary))
            val titleText = view.findViewById(R.id.custom_dialog_title) as TextView
            titleText.text = title
            builder.setCustomTitle(view)
            return builder
        }
    }
}