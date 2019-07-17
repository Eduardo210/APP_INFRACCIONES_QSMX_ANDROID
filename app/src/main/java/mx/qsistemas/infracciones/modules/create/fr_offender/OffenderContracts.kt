package mx.qsistemas.infracciones.modules.create.fr_offender

import android.app.Activity
import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.db.entities.LicenseType
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class OffenderContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun onError(msg: String)
        fun onStatesReady(adapter: ArrayAdapter<String>)
        fun onStatesIssuedReady(adapter: ArrayAdapter<String>)
        fun onTownshipsReady(adapter: ArrayAdapter<String>)
        fun validFields(): Boolean
        fun isDirectionAnswered(): Boolean
        fun isLicenseAnswered(): Boolean
        fun onDataSaved()
        fun onDataDuplicate()
        fun onDataUpdated(idPerson: Long)
        fun onTicketPrinted()
        fun onResultSavePayment(msg: String, flag: Boolean)
    }

    interface Iterator {
        fun getStatesList()
        fun getStatesIssuedList()
        fun getTownshipsList(posState: Int)
        fun getTypeLicenseAdapter(): ArrayAdapter<String>
        fun getPositionState(obj: States): Int
        fun getPositionStateLicense(obj: States): Int
        fun getPositionTownship(obj: Townships): Int
        fun getPositionTypeLicense(obj: LicenseType): Int
        fun saveData(notify: Boolean)
        fun savePayment(info: TransactionInfo)
        fun updateData()
        fun printTicket(activity: Activity)
        fun reprintVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener)
        fun savePaymentToService(idInfraction: String, txInfo: TransactionInfo, amount: String, discount: String, totalPayment: String, idPerson: Long)
    }
}