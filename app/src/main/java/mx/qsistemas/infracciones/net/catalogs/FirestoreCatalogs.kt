package mx.qsistemas.infracciones.net.catalogs

/* Collection Headers */
data class States(val id: Int = 0, val enable: Boolean = false, val value: String = "")

data class Townships(val id_town: Int = 0, val prefix: String = "PRX", val counter_prefix: Int = 0,
                     val enable: Boolean = false, val value: String = "", val id_state: Int = 0)


/* Remote Config Version */
data class RemoteVersion(val url: String, val version: String)