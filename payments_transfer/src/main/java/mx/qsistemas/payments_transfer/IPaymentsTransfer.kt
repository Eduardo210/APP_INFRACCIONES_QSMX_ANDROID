package mx.qsistemas.payments_transfer

import com.basewin.aidl.OnPrinterListener
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class IPaymentsTransfer {
    interface LoadKeyListener {
        fun onLoadKey(success: Boolean, value: String)
    }

    interface TransactionListener {
        fun onTxApproved(txInfo: TransactionInfo)
        fun onTxVoucherPrinted()
        fun onTxFailed(message: String)
        fun onTxVoucherFailer(message: String)
    }

    interface PrintListener{
        fun onError(var1: Int, var2: String)
        fun onFinish()
        fun onStart()
    }

    interface ScanCardListener {
        fun onError(var1: Int, var2: String)
        fun onSuccess(idCard: String)
    }

    interface ScanCodeListener {
        fun onError(var1: Int, var2: String)
        fun onSuccess(result: String)
    }
}