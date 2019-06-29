package mx.qsistemas.infracciones.utils

/* PERMISSION REQUEST CODES */
const val RC_PERMISSION_LOCATION = 1
const val RC_PERMISSION_CAMERA = 2
const val RC_PERMISSION_AUDIO_RECORD = 3
const val RC_PERMISSION_CAMERA_VIDEO = 4
const val RC_PERMISSION_STORAGE = 5

/* ALARMS TIMING */
const val ALARM_SEND_REPORT_TIME = 1000 * 60 * 30L // 30 min
const val ALARM_SEND_GEOS_TIME = 1000 * 60 * 15L // 15 min

/* EXTRA's INTENTS */
const val EXTRA_OPTION_INFRACTION = "extra_option_infraction"

/* INTENT REQUEST CODES */
const val RC_INTENT_CAMERA_EV1 = 11
const val RC_INTENT_CAMERA_EV2 = 12
const val RC_INTENT_VIDEO = 13

/* MAPS CONSTANTS */
const val MAX_ACCURACY = 100.0
const val TIME_OUT_OF_GEO = 1000 * 60L

/* FIRESTORE COLLECTIONS */
const val FS_COL_STATES = "states"
const val FS_COL_TOWNSHIPS = "townships"
const val FS_COL_TERMINALS = "terminals"

/* JOB CHANNELS */
const val CHANNEL_ID = "channelId"

/* NOTIFICATION ID's */
const val NOTIF_SEND_REPORTS = 0x1
const val NOTIF_SEND_GEOS = 0x2