package mx.qsistemas.infracciones.db

import android.content.Context
import android.text.Editable
import androidx.room.Room
import androidx.room.RoomDatabase
import com.commonsware.cwac.saferoom.SafeHelperFactory
import mx.qsistemas.incidencias.utils.Utils
import mx.qsistemas.infracciones.Application

private const val DB_NAME = "infracciones"
private const val DB_VERSION = 1

abstract class AppDatabase: RoomDatabase() {
    companion object {
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInMemoryDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = run {
                        val editable = Editable.Factory.getInstance().newEditable(Utils.getTokenDevice(Application.getContext()))
                        val factory = SafeHelperFactory.fromUser(editable)
                        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).openHelperFactory(factory)
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}