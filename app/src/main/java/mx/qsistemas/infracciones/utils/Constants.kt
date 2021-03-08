package mx.qsistemas.infracciones.utils

/* BLACK BOX KEY */
const val BBOX_KEY = "Stzvy`OoS7Dc6wYy"

/* AES Transformation Options */
const val AES_CBC_PKCS5 = "AES/CBC/PKCS5PADDING"
const val DES_ECB_PKCS5 = "DESede/ECB/PKCS5Padding"
const val DES_KEY = "CF2E3EF25C90EB567243ADFACD4AA868"

/* BANORTE CONTANTS */
const val PTX_SERIAL_NUMBER = "88888888"
const val PTX_MERCHANT_ID = "7455440"
const val PTX_MAIN = "devs"
const val PTX_PSW = "QueT_@.-2019"
const val PTX_VOUCHER_TITLE = "Quetzalcóatl Sistemas S.A. de C.V."
const val PTX_VOUCHER_ADDRESS_1 = "Cto. Puericultores 18, Ciudad Satélite"
const val PTX_VOUCHER_ADDRESS_2 = "Naucalpan de Juárez, Edo. de México CP 53100"

/* PERMISSION REQUEST CODES */
const val RC_PERMISSION_LOCATION = 1
const val RC_PERMISSION_CAMERA = 2
const val RC_PERMISSION_AUDIO_RECORD = 3
const val RC_PERMISSION_CAMERA_VIDEO = 4
const val RC_PERMISSION_STORAGE = 5

/* TYPE PAYMENT INFRINGEMENT */
const val PAYMENT_CREDIT_CARD = 1
const val PAYMENT_CODI = 10

/* ALARMS TIMING */
const val ALARM_SEND_REPORT_TIME = 1000 * 60 * 15L // 15 min
const val ALARM_SEND_GEOS_TIME = 1000 * 60 * 15L // 15 min
const val ALARM_SYNC_CATALOGS_TIME = 1000 * 60 * 15L // 15 min

/* EXTRA's INTENTS */
const val EXTRA_OPTION_INFRACTION = "extra_option_infraction"
const val EXTRA_MODE_CODI = "extra_mode_codi"

/* INTENT REQUEST CODES */
const val RC_INTENT_CAMERA_EV1 = 11
const val RC_INTENT_CAMERA_EV2 = 12
const val RC_INTENT_VIDEO = 13
const val RC_CODI_PAYMENT = 14

/* MAPS CONSTANTS */
const val MIN_ACCURACY = 1.0
const val MAX_ACCURACY = 100.0
const val TIME_OUT_OF_GEO = 1000 * 60L

/* FIRESTORE COLLECTIONS */
const val FS_COL_APP_VERSIONS = "app_versions"
const val FS_COL_ARTICLES = "articles"
const val FS_COL_BRANDS = "brands"
const val FS_COL_CITIES = "cities"
const val FS_COL_CLASS_TYPE = "class_type"
const val FS_COL_COLONIES = "colonies"
const val FS_COL_COLORS = "colors"
const val FS_COL_CRANES = "cranes"
const val FS_COL_TYPE_DOC = "document_type"
const val FS_COL_TYPE_LIC = "license_type"
const val FS_COL_FRACTIONS = "fractions"
const val FS_COL_IDENTIF_DOC = "identifier_document"
const val FS_COL_INSURED_DOC = "insured_document"
const val FS_COL_MODELS = "sub_brands"
const val FS_COL_TERMINALS = "terminals"
const val FS_COL_STATES = "state"
const val FS_COL_ZIP_CODES = "zip_codes"
const val FS_COL_HOLIDAYS = "holidays"
const val FS_COL_INFRINGEMENT_APP_OPTIONS = "infringement_app_options"

/* FIRESTORE DOCUMENTS */
const val FS_DOC_VERSION = "qsistemas"

/* FIREBASE FUNCTIONS */
const val FF_CIPHER_DATA = "cipherData"
const val FF_ZIP_CODES = "zipCodes"
const val FF_COLONIES = "colonies"

/* HOME OPTIONS ID's */
const val HO_ID_INFRACTION = "bZ80dNvSRGTngb4k6zPf"
const val HO_ID_SEARCH = "W91OktCNx2Fv6huIhPgg"
const val HO_ID_VOUCHER = "arFkOHdf6AIN2xB559IF"
const val HO_ID_PREFERENCES = "IJqD1N4WSwmLqKqlZpBr"
const val HO_ID_SETTINGS = "axvJ8IQSgJQ5O3ECpCm1"
const val HO_VALIDATE_ACCOUNT_CODI = "aVU6XSJeiABrS87GgSy0"
const val HO_SEND_DB = "GoMrqvLBCL9uESaMRA6M"

/* JOB CHANNELS */
const val CHANNEL_ID_REPORT = "infractionChannelId"
const val CHANNEL_ID_IMAGES = "imageChannelId"
const val CHANNEL_ID_CATALOGS = "catalogsChannelId"
const val CHANNEL_ID_CODI = "codiChannelId"

/* NOTIFICATION ID's */
const val NOTIF_SEND_REPORTS = 0x1
const val NOTIF_SEND_GEOS = 0x2
const val NOTIF_SEND_IMAGES = 0x3
const val NOTIF_DOWNLOAD_CATALOGS = 0x4

/* FIREBASE PUSH DATA TOKENS */
const val FCM_TOKEN_OPERATION = "id_operation"

/* FIERBASE PUSH OPERATIONS */
const val OP_SEND_DATABASE = 1
const val OP_SEND_DATABASE_WEB = 2

/* Event Names */
const val EVENT_INFRACTION_STARTED = "infraction_started"
const val EVENT_INFRACTION_FINISHED = "infraction_finished"
const val EVENT_REPRINT_VOUCHER = "reprint_voucher"

/* Event Bundle Items */
const val EV_PERSON_NAME = "person_name"
const val EV_DEVICE_PREFIX = "device_prefix"
const val EV_DEVICE_IMEI = "device_imei"
const val EV_TOWNSHIP_NAME = "township_name"

/* CoDi: ACCOUNT VALIDATION FLAGS */
const val CODI_ACCOUNT_PENDENT = 0
const val CODI_ACCOUNT_VERIFIED = 1
const val CODI_ACCOUNT_INVALID = 3

/* CoDi: TYPE OF CATEGORIES */
const val CATEGORY_CLABE = 40
const val CATEGORY_DEBIT_CARD = 3
const val CATEGORY_CELLPHONE = 10

/* CoDi: MODE OF OPERATIONS SCREEN */
const val MODE_ACCOUNT_VALIDATION = 1
const val MODE_GENERATE_QR = 2

/* CoDi: TYPES OF PAYMENTS */
const val CODI_TYPE_PRESENTIAL = 19
const val CODI_TYPE_NON_PRESENTIAL = 20
const val CODI_TYPE_RECURRENTS = 21
const val CODI_TYPE_THIRD_PERSON = 22

/* CoDi: CONCEPT OF PAYMENT */
const val PAYMENT_CONCEPT = "Infraccion de transito" // NUNCA COLOCAR ACENTOS EN EL CONCEPTO

/* CoDi: STATUS OF CHARGE NOTIFICATION */
const val CM_ACREDITED = 1
const val CM_DECLINED = 2
const val CM_DEVOLUTION_CHARGE_NOT_ACCEPTED = 3
const val CM_DEVOLUTION_CHARGE_ACCEPTED = 6
const val CM_ISSUER_PROBLEM = 21
const val CM_BENEFICIARY_PROBLEM = 22
const val CM_ADMINISTRATOR_PROBLEM = 23
const val CM_ISSUER_CANCELED = 24
const val CM_DEVOLUTION_ERROR_ISSUER_CHARGE_NOT_ACCEPTED = 31
const val CM_DEVOLUTION_ERROR_BENEFICIARY_CHARGE_NOT_ACCEPTED = 32
const val CM_DEVOLUTION_ERROR_ADMINISTRATOR_CHARGE_NOT_ACCEPTED = 33
const val CM_DEVOLUTION_ERROR_ISSUER_CHARGE_ACCEPTED = 62
const val CM_DEVOLUTION_ERROR_BENEFICIARY_CHARGE_ACCEPTED = 63

/* CoDi:  TYPE OF PHONE NUMBER REQUEST */
const val PHONE_NUMBER_REGISTER = 0
const val PHONE_NUMBER_PAYMENT = 1