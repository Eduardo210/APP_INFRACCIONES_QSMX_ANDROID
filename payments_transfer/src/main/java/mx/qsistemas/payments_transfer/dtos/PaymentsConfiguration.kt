package mx.qsistemas.payments_transfer.dtos

data class LoadKeyData(var serialNumber: String = "", var merchantId: String = "", var user: String = "",
                       var psw: String = "")

data class TransactionInfo(var aid: String, var apn: String, var al: String, var arqc: String,
                           val authorization: String, val entryType: String,
                           val maskedPan: String, val txDate: String, val txTime: String,
                           val affiliation: String, val expirationDate: String,
                           val brandCard: String, val typeCard: String, val bank: String,
                           val reference: String, val amount: String, var tvr: String,
                           var tsi: String, val noControl: String, val cardOwner: String,
                           val typeTx: String, val flagTransaction: String, val needSign: Boolean)