package mx.qsistemas.infracciones.db_web

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.qsistemas.infracciones.db.entities.*

private const val DB_NAME_WEB = "infracciones_web"
private const val DB_VERSION_WEB = 1

@Database(
    entities = [
        Address::class,
        AddressInfringement::class,
        AddressPerson::class,
        Articles::class,
        Ascription::class,
        Attribute::class,
        AuthorityIssues::class,
        Colour::class,
        Config::class,
        Disposition::class,
        Infraction::class,
        InfractionEvidence::class,
        IdentifierDocument::class,
        InfractionFraction::class,
        LicenseType::class,
        Module::class,
        NonWorkingDay::class,
        PaymentInfringement::class,
        PaymentInfringementCard::class,
        Person::class,
        PersonAccount::class,
        PersonAttibute::class,
        PersonInfringement::class,
        PersonTownship::class,
        RetainedDocument::class,
        State::class,
        SubmarkingVehicle::class,
        Syncronization::class,
        TownSepoMex::class,
        TrafficViolationFraction::class,
        VehicleBrand::class,
        VehicleType::class,
        VehicleInfraction::class
    ], version = DB_VERSION_WEB, exportSchema = false
)


abstract class AppDatabaseWeb : RoomDatabase() {


    companion object {
        private var INSTANCE: AppDatabaseWeb? = null

        @JvmStatic
        fun getInMemoryDatabase(context: Context): AppDatabaseWeb? {
            if (INSTANCE == null) {
                synchronized(AppDatabaseWeb::class.java) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabaseWeb::class.java, DB_NAME_WEB).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
