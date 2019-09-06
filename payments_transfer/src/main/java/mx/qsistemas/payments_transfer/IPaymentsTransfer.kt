package mx.qsistemas.payments_transfer

import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class IPaymentsTransfer {
    interface LoadKeyListener {
        fun onLoadKey(success: Boolean, value: String)
    }

    interface TransactionListener {
        fun onTxApproved(txInfo: TransactionInfo)
        fun onTxVoucherPrinted()
        fun onTxFailed(retry: Boolean, message: String)
        fun onCtlsDoubleTap()
        fun onTxVoucherFailed(message: String)
    }

    interface PrintListener {
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