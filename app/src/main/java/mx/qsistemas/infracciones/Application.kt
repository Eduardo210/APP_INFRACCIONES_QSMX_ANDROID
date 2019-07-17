package mx.qsistemas.infracciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.StrictMode
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import io.fabric.sdk.android.Fabric
import mx.qsistemas.infracciones.db.AppDatabase
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
        var prefs: Preferences? = null
        var firestore: FirebaseFirestore? = null
        var remoteConfig: FirebaseRemoteConfig? = null
        var firebaseAnalytics: FirebaseAnalytics? = null
        var firebaseStorage: FirebaseStorage ?= null

        fun getContext(): Context {
            return instance!!.applicationContext
        }

    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(getContext())
        prefs = Preferences(getContext())
        m_database = AppDatabase.getInMemoryDatabase(getContext())
        initializeFirebaseComponents()
        /* Initialize Payments Library */
        PaymentsTransfer.initialize(getContext())
        /* Granted permission to access to firebaseStorage*/
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        /* Initialize crashlytics */
        Fabric.with(this, Crashlytics())
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(getContext())
        }
        /* Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library*/
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
        /* Initialize Firebase Firestore */
        val settings = FirebaseFirestoreSettings.Builder().setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED).setPersistenceEnabled(true).build()
        firestore = FirebaseFirestore.getInstance()
        firestore?.firestoreSettings = settings
        /* Initialize Firebase Analytics Events */
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        /* Initialize Firebase Storage */
        firebaseStorage = FirebaseStorage.getInstance()
        /* Initialize Firebase Remote Config */
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1000 * 60 * 5)
                .build()
        remoteConfig?.setConfigSettings(configSettings)
        remoteConfig?.setDefaults(R.xml.remote_config_defaults)
        remoteConfig?.fetchAndActivate()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d(TAG, "Config params updated: $updated")
            } else {
                Log.e(TAG, "Fetch failed")
            }
        }
        /* Get Firebase push notification token */
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
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