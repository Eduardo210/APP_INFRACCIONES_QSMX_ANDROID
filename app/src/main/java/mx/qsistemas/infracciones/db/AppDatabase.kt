package mx.qsistemas.infracciones.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.qsistemas.infracciones.db.dao.*
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.db_web.dao_web.PermissionsDaoWeb

private const val DB_NAME = "infracciones"
private const val DB_VERSION = 1

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
    ], version = DB_VERSION, exportSchema = false
)


abstract class AppDatabase : RoomDatabase() {

    abstract fun addressDao(): AddressDao
    abstract fun addressInfringementDao(): AddressInfringementDao
    abstract fun addressPersonDao(): AddressPersonDao
    abstract fun articlesDao(): ArticlesDao
    abstract fun ascriptionDao(): AscriptionDao
    abstract fun attributeDao(): AttributeDao
    abstract fun authorityIssuesDao(): AuthorityIssuesDao
    abstract fun colourDao(): ColourDao
    abstract fun configDao(): ConfigDao
    abstract fun dispositionDao(): DispositionDao
    abstract fun identifierDocumentDao(): IdentifierDocumentDao
    abstract fun infractionDao(): InfractionDao
    abstract fun infractionFractionDao(): InfractionFractionDao
    abstract fun infractionEvidenceDao(): InfractionEvidenceDao
    abstract fun licenseTypeDao(): LicenseTypeDao
    abstract fun moduleDao(): ModuleDao
    abstract fun nonWorkingDayDao(): NonWorkingDayDao
    abstract fun personAccountDao(): PersonAccountDao
    abstract fun personAttributeDao(): PersonAttributeDao
    abstract fun personDao(): PersonDao
    abstract fun personInfringementDao(): PersonInfringementDao
    abstract fun personTownshipDao(): PersonTownshipDao
    abstract fun paymentInfringementDao(): PaymentInfringementDao
    abstract fun paymentInfringementCardDao(): PaymentInfringementCardDao
    abstract fun retainedDocumentDao(): RetainedDocumentDao
    abstract fun stateDao(): StateDao
    abstract fun submarkingVehicleDao(): SubmarkingVehicleDao
    abstract fun synchronizationDao(): SyncronizationDao
    abstract fun townSepomexDao(): TownSepomexDao
    abstract fun trafficViolationDao(): TrafficViolationDao
    abstract fun vehicleBrandDao(): VehicleBrandDao
    abstract fun vehicleInfractionDao(): VehicleInfractionDao
    abstract fun vehicleTypeDao(): VehicleTypeDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInMemoryDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
