package mx.qsistemas.payments_transfer.dtos

data class Voucher(val noControl: String, val noCard: String, val caducity: String, val cardBrand: String,
                   val cardType: String, val bank: String, val authCode: String, val reference: String,
                   val amount: String, val cardBeneficiary: String, val date: String, val hour: String,
                   var AID: String, var TVR: String, var TSI: String, var APN: String,
                   val flagTrans: String, val needSign: Boolean, val afiliacion: String,
                   val terminalId: String)