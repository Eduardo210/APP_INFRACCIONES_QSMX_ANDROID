package mx.qsistemas.infracciones.utils

/* BANORTE CONTANTS */
const val PTX_SERIAL_NUMBER = "88888888"
const val PTX_MERCHANT_ID = "7455440"
const val PTX_USER = "a7455440"
const val PTX_PSW = "quet5440"

/* PERMISSION REQUEST CODES */
const val RC_PERMISSION_LOCATION = 1
const val RC_PERMISSION_CAMERA = 2
const val RC_PERMISSION_AUDIO_RECORD = 3
const val RC_PERMISSION_CAMERA_VIDEO = 4
const val RC_PERMISSION_STORAGE = 5

/* ALARMS TIMING */
const val ALARM_SEND_REPORT_TIME = 1000 * 60 * 15L // 30 min
const val ALARM_SEND_GEOS_TIME = 1000 * 60 * 15L // 15 min

/* EXTRA's INTENTS */
const val EXTRA_OPTION_INFRACTION = "extra_option_infraction"

/* INTENT REQUEST CODES */
const val RC_INTENT_CAMERA_EV1 = 11
const val RC_INTENT_CAMERA_EV2 = 12
const val RC_INTENT_VIDEO = 13

/* MAPS CONSTANTS */
const val MIN_ACCURACY = 1.0
const val MAX_ACCURACY = 100.0
const val TIME_OUT_OF_GEO = 1000 * 60L

/* FIRESTORE COLLECTIONS */
const val FS_COL_ARTICLES = "articles"
const val FS_COL_BRANDS = "brands"
const val FS_COL_CITIES = "cities"
const val FS_COL_CLASS_TYPE = "class_type"
const val FS_COL_COLONIES = "colonies"
const val FS_COL_COLORS = "colors"
const val FS_COL_CRANES = "cranes"
const val FS_COL_TYPE_DOC = "document_type"
const val FS_COL_FRACTIONS = "fractions"
const val FS_COL_IDENTIF_DOC = "identifier_document"
const val FS_COL_INSURED_DOC = "insured_document"
const val FS_COL_MODELS = "models"
const val FS_COL_TERMINALS = "terminals"
const val FS_COL_STATES = "state"
const val FS_COL_ZIP_CODES = "zip_codes"
const val FS_COL_LICENSE_TYPE = "license_type"

/* JOB CHANNELS */
const val CHANNEL_ID_REPORT = "infractionChannelId"
const val CHANNEL_ID_IMAGES = "imageChannelId"

/* NOTIFICATION ID's */
const val NOTIF_SEND_REPORTS = 0x1
const val NOTIF_SEND_GEOS = 0x2
const val NOTIF_SEND_IMAGES = 0x3

/* Firebase Push Data Tokens */
const val FCM_TOKEN_OPERATION = "id_operation"

/* Firebase Push Operations */
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

