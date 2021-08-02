package mx.qsistemas.infracciones.db_web

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.qsistemas.infracciones.db_web.dao_web.*
import mx.qsistemas.infracciones.db_web.dao_web.firebase_replica.CityDao
import mx.qsistemas.infracciones.db_web.dao_web.firebase_replica.ColonyDao
import mx.qsistemas.infracciones.db_web.dao_web.firebase_replica.ZipCodeDao
import mx.qsistemas.infracciones.db_web.entities.*
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.City
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes

private const val DB_NAME_WEB = "infracciones_web"
private const val DB_VERSION_WEB = 1

@Database(
        entities = [
            City::class,
            Colony::class,
            ZipCodes::class,
            DriverAddressDriver::class,
            DriverDriverLicense::class,
            DriverDrivers::class,
            ElectronicBill::class,
            InfringementAddressInfringement::class,
            InfringementCapturelines::class,
            InfringementInfringements::class,
            InfringementPayorder::class,
            InfringementPicturesInfringement::class,
            InfringementRelfractionInfringements::class,
            PersonTownhall::class,
            Permissions::class,
            VehicleVehicles::class,
        ], version = DB_VERSION_WEB, exportSchema = false
)


abstract class AppDatabaseWeb : RoomDatabase() {

    abstract fun cityDao(): CityDao
    abstract fun zipCodeDao(): ZipCodeDao
    abstract fun colonyDao(): ColonyDao

    /*Para la migración a web*/
    abstract fun infractionDaoWeb(): InfractionDaoWeb
    abstract fun electronicBillDaoWeb(): ElectronicBillDaoWeb
    abstract fun personDaoWeb(): PersonDaoWeb
    abstract fun vehicleInfractionDaoWeb(): VehicleInfractionDaoWeb
    abstract fun addressPersonDaoWeb(): AddressPersonDaoWeb
    abstract fun addressInfringementDaoWeb(): AddressInfringementDaoWeb
    abstract fun infractionFractionDaoWeb(): InfractionFractionDaoWeb
    abstract fun personTownHallDaoWeb(): PersonTownHallDaoWeb
    abstract fun infractionEvidenceDaoWeb(): InfractionEvidenceDaoWeb
    abstract fun driverLicenseDaoWeb(): DriverDriverLicenseDaoWeb
    abstract fun pictureInfractionDaoWeb(): PictureInfractionDaoWeb
    abstract fun captureLineDaoWeb(): CaptureLineDaoWeb
    abstract fun payorderDaoWeb(): InfractionPayorderDaoWeb
    abstract fun permissionsDao(): PermissionsDaoWeb

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
