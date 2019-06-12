package mx.qsistemas.infracciones.db

import android.content.Context
import android.text.Editable
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.commonsware.cwac.saferoom.SafeHelperFactory
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.utils.Utils

private const val DB_NAME = "infracciones"
private const val DB_VERSION = 1

@Database(entities = [
    Articles::class,
    Ascription::class,
    Attribute::class,
    AuthorityIssues::class,
    Colour::class,
    Config::class,
    Disposition::class,
    IdentifierDocument::class,
    InfractionFraction::class,
    LicenseType::class,
    Module::class,
    NonWorkingDay::class,
    Person::class,
    PersonAccount::class,
    PersonAttibute::class,
    PersonTownship::class,
    RetainedDocument::class,
    State::class,
    SubmarkingVehicle::class,
    Syncronization::class,
    TownSepoMex::class,
    VehicleBrand::class,
    VehicleType::class
], version = DB_VERSION, exportSchema = false)


abstract class AppDatabase : RoomDatabase() {
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