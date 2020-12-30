package mx.qsistemas.infracciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.StrictMode
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import mx.qsistemas.infracciones.BuildConfig.FIREBASE_ID
import mx.qsistemas.infracciones.db.AppDatabase
import mx.qsistemas.infracciones.db_web.AppDatabaseWeb
import mx.qsistemas.infracciones.utils.FS_COL_TERMINALS
import mx.qsistemas.infracciones.utils.Preferences
import mx.qsistemas.infracciones.utils.Utils
import mx.qsistemas.payments_transfer.PaymentsTransfer

class Application : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        val TAG = "Infracciones"
        private var instance: Application? = null
        var m_database: AppDatabase? = null
        var m_database_web: AppDatabaseWeb? = null
        var prefs: Preferences? = null
        var firestore: FirebaseFirestore? = null
        var firebaseFunctions: FirebaseFunctions? = null
        var firebaseStorage: FirebaseStorage? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }

    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(getContext())
        prefs = Preferences(getContext())
        m_database = AppDatabase.getInMemoryDatabase(getContext())
        m_database_web = AppDatabaseWeb.getInMemoryDatabase(getContext())
        initializeFirebaseComponents()
        /* Initialize Payments Library */
        PaymentsTransfer.initialize(getContext())
        /* Granted permission to access to firebaseStorage*/
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        /* Initialize Stetho */
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(getContext())
        }
        /* Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(BuildConfig.APPLICATION_ID, name, importance).apply {
                description = descriptionText
            }
            /* Register the channel with the system */
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initializeFirebaseComponents() {
        // Manually configure Firebase Options
        val options = if (BuildConfig.DEBUG) {
            FirebaseOptions.Builder()
                    .setProjectId("dei-lucrii-qa")
                    .setApplicationId("1:167497196333:android:$FIREBASE_ID")
                    //.setApiKey("AIzaSyDZhHDvMk6fFFi0YTS6ngoND5po0KNiV_4")
                    .setDatabaseUrl("https://dei-lucrii-qa.firebaseio.com")
                    .setStorageBucket("dei-lucrii-qa.appspot.com")
                    .setGcmSenderId("167497196333")
                    .build()
        } else {
            FirebaseOptions.Builder()
                    .setProjectId("dei-lucrii-prod")
                    .setApplicationId("1:732222665509:android:$FIREBASE_ID")
                    //.setApiKey("AIzaSyCl0zsMmGQ0dRFg0I3Uevx4Zp810YPtCxc")
                    .setDatabaseUrl("https://dei-lucrii-prod.firebaseio.com")
                    .setStorageBucket("dei-lucrii-prod.appspot.com")
                    .setGcmSenderId("51886383811")
                    .build()
        }
        FirebaseApp.initializeApp(getContext(), options, "default")
        val default = FirebaseApp.getInstance("default")
        /* Initialize Firebase Firestore */
        val settings = FirebaseFirestoreSettings.Builder().setCacheSizeBytes(100 * 1024 * 1024).setPersistenceEnabled(true).build()
        firestore = FirebaseFirestore.getInstance(default)
        firestore?.firestoreSettings = settings
        /* Initialize Firebase Storage */
        firebaseStorage = FirebaseStorage.getInstance(default)
        /* Initialize Firebase Functions */
        firebaseFunctions = FirebaseFunctions.getInstance(default)
        /* Get Firebase push notification token */
        FirebaseInstanceId.getInstance(default).instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(this.javaClass.simpleName, "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }
            // Get new Instance ID token
            val token = task.result?.token
            prefs?.saveData(R.string.sp_firebase_token_push, token ?: "")
            /* Register Firebase Push Token Into Firestore */
            val map = hashMapOf("push_token" to token)
            val imei = Utils.getImeiDevice(getContext())
            firestore?.collection(FS_COL_TERMINALS)?.document(imei)?.set(map, SetOptions.merge())?.addOnCompleteListener { t2 ->
                if (!t2.isSuccessful) {
                    Log.e(this.javaClass.simpleName, "Push Notification Token Not Registered")
                }
            }
        })
    }
}