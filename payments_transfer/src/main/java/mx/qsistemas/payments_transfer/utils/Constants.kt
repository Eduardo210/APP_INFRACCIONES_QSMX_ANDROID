package mx.qsistemas.payments_transfer.utils

/* AES Transformation Options */
const val AES_CBC_PKCS5 = "AES/CBC/PKCS5PADDING"

/* Selector components */
internal const val SELECTOR_KEY = "F40379AB9E0EC533F40379AB9E0EC533"
internal const val SELECTOR_NO_SERIE = "38383838383838382020202020202020"
internal const val SELECTOR_CRC = "7B75E6D7"

/* Payworks Result */
internal const val PWR_APROBADA = "A"
internal const val PWR_DECLINADA = "D"
internal const val PWR_RECHAZADA = "R"
internal const val PWR_SIN_RESPUESTA = "T"

/* Input Pin Flags */
internal val PIN_DIALOG_SHOW = 1
internal val PIN_DIALOG_DISMISS = 2
internal val PIN_SHOW = 3
internal val SETLAYOUT = 4
internal val GETLAYOUT = 5

/* Entry Mode for Banorte Transaction */
const val ENTRY_MODE_BAND = "MAGSTRIPE"
const val ENTRY_MODE_CHIP = "CHIP"
const val ENTRY_MODE_FALLBACK = "FALLBACK"

/* Mode of Processing the Transaction */
const val MODE_TX_PROD = "PRD"
const val MODE_TX_PROBE_AUTH_ALWAYS = "AUT"
const val MODE_TX_PROBE_FAIL_ALWAYS = "DEC"
const val MODE_TX_PROBE_RANDOM = "RND"

/* Quick Payment Services Flags */
const val TRANSACTION_NORMAL = "0"
const val TRANSACTION_QPS = "1"

/* EMV TAGS */
const val TAG_CARD_OWNER = 0x5F20
const val TAG_TRACK_2 = "57"
const val TAG_AID = 0x4F
const val TAG_ARQC = 0x9F26
const val TAG_APN = 0x9F12
const val TAG_AL = 0x50
const val TAG_TVR = 0x95
const val TAG_TSI = 0x9B
const val TAG_PAN_SEQ = 0x5F34

/* For pinpad interface versions */
const val PINPAD_INTERFACE_VERSION1 = 1
const val PINPAD_INTERFACE_VERSION2 = 2
const val PINPAD_INTERFACE_VERSION3 = 3
const val PINPAD_INTERFACE_DUKPT = 4

/* CoDi: Account Validation */
const val CODI_ACCOUNT_PENDENT = "Cuenta pendiente de verificar"
const val CODI_ACCOUNT_VERIFIED = "Cuenta verificada correctamente"
const val CODI_ACCOUNT_INVALID = "No fue posible realizar la verificación"
const val CODI_ACCOUNT_UNKNOWN = "Estatus desconocido"

/* Network: Request: Generic Flags */
const val FLAG_TRANS_APPROVE = "Aprobada"
const val FLAG_TRANS_DECLINE = "Declinada"
const val FLAG_TRANS_REJECTED = "Rechazada"
const val FLAG_TRANS_TIMEOUT = "Tiempo de espera\nfinalizado"

/* Network : Request : ProcessTx */
internal const val CMD_TRANS = "CMD_TRANS"
internal const val MERCHANT_ID = "MERCHANT_ID"
internal const val USER = "USER"
internal const val PASSWORD = "PASSWORD"
internal const val CONTROL_NUMBER = "CONTROL_NUMBER"
internal const val TERMINAL_ID = "TERMINAL_ID"
internal const val AMOUNT = "AMOUNT"
internal const val PAGO_MOVIL = "PAGO_MOVIL"
internal const val ENTRY_MODE = "ENTRY_MODE"
internal const val MODE = "MODE"
internal const val RESPONSE_LANGUAGE = "RESPONSE_LANGUAGE"
internal const val CUSTOMER_REF1 = "CUSTOMER_REF1"
internal const val CUSTOMER_REF2 = "CUSTOMER_REF2" // Obligatorio
internal const val CUSTOMER_REF3 = "CUSTOMER_REF3"
internal const val CUSTOMER_REF4 = "CUSTOMER_REF4"
internal const val CUSTOMER_REF5 = "CUSTOMER_REF5"  // Obligatorio
internal const val BANORTE_URL = "BANORTE_URL"
internal const val TRACK1 = "TRACK1"
internal const val TRACK2 = "TRACK2"
internal const val EMV_TAGS = "EMV_TAGS"