package mx.qsistemas.infracciones.net.catalogs

/* Collection Headers */
data class States(val id: Int = 0, val enable: Boolean = false, val value: String = "")

data class Townships(val id_town: Int = 0, val prefix: String = "PRX", val counter_prefix: Int = 0,
                     val enable: Boolean = false, val value: String = "", val id_state: Int = 0)

data class GenericCatalog(val value: String = "", val is_active: Boolean = false)
data class GenericSubCatalog(val value: String="", val reference:String="", val is_active: Boolean)

data class Articles(val number:String = "", val description:String="", val is_active:Boolean)

data class Fractions(val number: String= "", val description: String="", val is_active: Boolean, val reference:String = "", val uma: Int)

/* Remote Config Version */
data class RemoteVersion(val url: String, val version: String)