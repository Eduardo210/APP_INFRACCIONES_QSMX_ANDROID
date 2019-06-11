package mx.qsistemas.incidencias.utils

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
const val FS_COL_STATES = "states"
const val FS_COL_TOWNSHIPS = "townships"
const val FS_COL_TERMINALS = "terminals"

/* JOB CHANNELS */
const val CHANNEL_ID = "channelId"

/* NOTIFICATION ID's */
const val NOTIF_SEND_REPORTS = 0x1
const val NOTIF_SEND_GEOS = 0x2