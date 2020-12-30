package mx.qsistemas.infracciones.fcm

import android.net.Uri
import android.os.Environment
import android.util.Log
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.utils.*
import java.io.File

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Application.prefs.saveData(R.string.sp_firebase_token_push, token)
        val map = hashMapOf("push_token" to token)
        val imei = Utils.getImeiDevice(Application.getContext())
        Application.firestore.collection(FS_COL_TERMINALS).document(imei).set(map, SetOptions.merge()).addOnCompleteListener { t2 ->
            if (!t2.isSuccessful) {
                Log.e(this.javaClass.simpleName, "Push Notification Token Not Registered")
            }
        }
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d(this.javaClass.simpleName, "Message Received: ${p0.data}")
        val idOperation = p0.data?.get(FCM_TOKEN_OPERATION)?.toInt() ?: -1
        when (idOperation) {
            OP_SEND_DATABASE or OP_SEND_DATABASE_WEB -> uploadDatabase(idOperation)
            else -> ""
        }
    }

    private fun uploadDatabase(option: Int) {
        val sd = Environment.getExternalStorageDirectory()
        val imei = Utils.getImeiDevice(Application.getContext())
        val storageReference = if (option == OP_SEND_DATABASE) {
            Application.firebaseStorage.reference.child("databases/$imei.db")
        } else {
            Application.firebaseStorage.reference.child("databases/${imei}_web.db")
        }
        if (sd.canWrite()) {
            val dbPath = if (option == OP_SEND_DATABASE) {
                Application.m_database.openHelper.writableDatabase?.path ?: ""
            } else {
                Application.m_database_web.openHelper.writableDatabase?.path ?: ""
            }
            val dbFile = File(dbPath)
            if (dbFile.exists()) {
                storageReference.putFile(Uri.fromFile(dbFile)).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.e(this.javaClass.simpleName, "Error Uploading Database")
                        return@addOnCompleteListener
                    }
                }
            }
        }
    }
}