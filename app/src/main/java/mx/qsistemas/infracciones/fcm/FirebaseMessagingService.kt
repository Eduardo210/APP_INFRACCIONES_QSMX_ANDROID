package mx.qsistemas.infracciones.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        Application.prefs?.saveData(R.string.sp_firebase_token_push, token ?: "")
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        Log.d(this.javaClass.simpleName, "Message Received: ${p0?.data}")
    }
}