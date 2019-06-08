package mx.qsistemas.incidencias.utils

/* SHARED PREFERENCES FLAGS */
const val SP_QUETZ = "SP_QUETZ"

/* PERMISSION REQUEST CODES */
const val RC_PERMISSION_LOCATION = 1
const val RC_PERMISSION_CAMERA = 2
const val RC_PERMISSION_AUDIO_RECORD = 3
const val RC_PERMISSION_CAMERA_VIDEO = 4
const val RC_PERMISSION_STORAGE = 5

/* ALARMS TIMING */
const val ALARM_SEND_REPORT_TIME = 1000 * 60 * 30L // 30 min
const val ALARM_SEND_GEOS_TIME = 1000 * 60 * 15L // 15 min

/* INTENT REQUEST CODES */
const val RC_INTENT_CAMERA = 11
const val RC_INTENT_VIDEO = 12

/* EXTRA's INTENTS */
const val EXTRA_OPTION_INCIDENCE = "extra_option_incidence"
const val EXTRA_FOLIO_INCIDENCE = "extra_folio_incidence"
const val EXTRA_CRIME_ID = "extra_crime_id"
const val EXTRA_CRIME_NAME = "extra_crime_name"

/* MAPS CONSTANTS */
const val MAX_ACCURACY = 100.0
const val MIN_ACCURACY = 10.0
const val TIME_OUT_OF_GEO = 1000 * 60L

/* INCIDENCE TAB's */
const val TAB_ALL = 0
const val TAB_SEND = 1
const val TAB_PENDING = 2

/* TIME PERIOD ID's */
const val ID_DAILY = 1
const val ID_WEEKLY = 2
const val ID_MONTHLY = 3

/* WEB SERVICE HEADER MAPPING */
const val HEADER_FOLIO = "h_folio"

/* FIRESTORE COLLECTIONS */
const val FS_COL_CATALOG = "catalogs"
const val FS_COL_SECTORS = "sectors"

/* FIRESTORE DOCUMENTS */
const val FS_DOC_INCIDENCE = "incidence"

/* CLOUD FUNCTIONS */
const val CF_GENERATE_STREAM_TOKEN = "generateStreamToken"

/* STREAM */
const val RTMP_BASE_URL = "rtmp://10.10.31.87/LiveApp/"

/* JOB CHANNELS */
const val CHANNEL_ID = "channelId"

/* NOTIFICATION ID's */
const val NOTIF_SEND_REPORTS = 0x1
const val NOTIF_SEND_GEOS = 0x2