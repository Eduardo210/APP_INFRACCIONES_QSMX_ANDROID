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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.fabric.sdk.android.Fabric
import mx.qsistemas.incidencias.utils.Preferences

class Application : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        val TAG = "Infracciones"
        private var instance: Application? = null
        /*private var m_database: AppDatabase? = null*/
        var prefs: Preferences? = null
        var firestore: FirebaseFirestore? = null
        var remoteConfig: FirebaseRemoteConfig? = null

        fun getContext(): Context {
            return instance!!.applicationContext
        }

        /*fun getAppDatabase(): AppDatabase {
            return m_database!!
        }*/
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(getContext())
        prefs = Preferences(getContext())
        /*m_database = AppDatabase.getInMemoryDatabase(getContext())*/
        initializeFirebaseComponents()
        /* Initialize Payments Library */
        //PaymentsTransfer.initialize(getContext())
        /* Granted permission to access to storage*/
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        /* Initialize crashlytics */
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        } else {
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
        /* Initialize Firebase Remote Config */
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1800)
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
        })
    }
}