package mx.qsistemas.infracciones.modules.create.fr_payer

import android.app.Activity
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class PayerContracts {
    interface Presenter {
        fun onError(msg: String)
        fun validFields(): Boolean
        fun onDataSaved()
        fun onDataDuplicate()
        fun onDataUpdated()
        fun onTicketPrinted()
        fun onPaymentSaved()
    }

    interface Iterator {
        fun getHolidays()
        fun saveData()
        fun updatePayerData()
        fun savePayment(info: TransactionInfo)
        fun printTicket(activity: Activity)
        fun reprintVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener)
        fun savePaymentToService(tokenInfraction: String, folioInfraction: String, txInfo: TransactionInfo, amount: String,
                                 discount: String, surcharges: String, totalPayment: String)
    }
}