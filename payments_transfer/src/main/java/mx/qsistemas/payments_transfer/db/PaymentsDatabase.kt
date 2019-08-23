package mx.qsistemas.payments_transfer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.qsistemas.payments_transfer.db.dao.ReversalDao
import mx.qsistemas.payments_transfer.db.entities.ReversalData

private const val PAYMENT_DB_NAME = "payments"
private const val PAYMENT_DB_VERSION = 1

@Database(entities = [ReversalData::class], version = PAYMENT_DB_VERSION, exportSchema = false)
abstract class PaymentsDatabase : RoomDatabase() {

    abstract fun reversalDao(): ReversalDao

    companion object {
        private var INSTANCE: PaymentsDatabase? = null

        @JvmStatic
        fun getInMemoryDatabase(context: Context): PaymentsDatabase? {
            if (INSTANCE == null) {
                synchronized(PaymentsDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context, PaymentsDatabase::class.java, PAYMENT_DB_NAME).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}