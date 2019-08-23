package mx.qsistemas.payments_transfer.db.manager

import android.annotation.SuppressLint
import android.os.AsyncTask
import mx.qsistemas.payments_transfer.PaymentsTransfer
import mx.qsistemas.payments_transfer.db.entities.ReversalData
import java.util.concurrent.Executors

@SuppressLint("StaticFieldLeak")
object ReversalManager {

    fun saveTransaction(data: ReversalData) {
        Executors.newSingleThreadExecutor().execute {
            PaymentsTransfer.database?.reversalDao()?.saveData(data)
        }
    }

    fun getTransactionsToReverse(): MutableList<ReversalData> {
        return object : AsyncTask<Void, Void, MutableList<ReversalData>>() {
            override fun doInBackground(vararg params: Void?): MutableList<ReversalData> {
                return PaymentsTransfer.database?.reversalDao()?.selectNotComplete()
                        ?: mutableListOf()
            }
        }.execute().get()
    }

    fun finishTransaction(noControl: String) {
        Executors.newSingleThreadExecutor().execute {
            PaymentsTransfer.database?.reversalDao()?.updateData(noControl)
        }
    }
}