package mx.qsistemas.infracciones.net.catalogs

import com.google.firebase.firestore.DocumentReference

/* Generic Headers */
data class GenericCatalog(var value: String = "", val is_active: Boolean = true, var documentReference: DocumentReference? = null)

data class GenericSubCatalog(var value: String = "", val reference: DocumentReference? = null, val is_active: Boolean = true,
                             var childReference: DocumentReference? = null)

data class Holidays(var key: String = "", val data: DocumentReference? = null, var description: String= "",
                             var reference: DocumentReference? = null)

/* Collection Headers */
data class Townships(val prefix: String = "PRX", val counter_prefix: Int = 0, val value: String = "", val reference: DocumentReference? = null, val is_active: Boolean = true,
                     var childReference: DocumentReference? = null, var uma_rate: Float =1F)

data class Articles(val number: String = "", val description: String = "", val is_active: Boolean = true, var documentReference: DocumentReference? = null)

data class Fractions(val number: String = "", val description: String = "", val is_active: Boolean = true, val reference: DocumentReference? = null, val uma: Int = 0,
                     var childReference: DocumentReference? = null)

/* Remote Config Version */
data class RemoteVersion(val url: String, val version: String)