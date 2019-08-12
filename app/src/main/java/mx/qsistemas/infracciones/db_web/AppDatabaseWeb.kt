package mx.qsistemas.infracciones.db_web

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.qsistemas.infracciones.db_web.dao_web.*
import mx.qsistemas.infracciones.db_web.entities.*

private const val DB_NAME_WEB = "infracciones_web"
private const val DB_VERSION_WEB = 1

@Database(
        entities = [
            DriverAddressDriver::class,
            DriverDriverLicense::class,
            DriverDrivers::class,
            InfringementAddressInfringement::class,
            InfringementCapturelines::class,
            InfringementInfringements::class,
            InfringementPayorder::class,
            InfringementPicturesInfringement::class,
            InfringementRelcondonation::class,
            InfringementRelfractionInfringements::class,
            InfringementThirdImpound::class,
            PersonTownhall::class,
            VehicleVehicles::class
        ], version = DB_VERSION_WEB, exportSchema = false
)


abstract class AppDatabaseWeb : RoomDatabase() {

    /*Para la migración a web*/
    abstract fun infractionDaoWeb(): InfractionDaoWeb
    abstract fun personDaoWeb(): PersonDaoWeb
    abstract fun vehicleInfractionDaoWeb(): VehicleInfractionDaoWeb
    abstract fun addressPersonDaoWeb(): AddressPersonDaoWeb
    abstract fun addressInfringementDaoWeb(): AddressInfringementDaoWeb
    abstract fun infractionFractionDaoWeb(): InfractionFractionDaoWeb
    abstract fun personTownHallDaoWeb(): PersonTownHallDaoWeb
    abstract fun infractionEvidenceDaoWeb(): InfractionEvidenceDaoWeb
    abstract fun driverLicenseDaoWeb(): DriverDriverLicenseDaoWeb


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
