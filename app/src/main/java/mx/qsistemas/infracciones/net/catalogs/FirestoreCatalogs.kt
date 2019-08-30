package mx.qsistemas.infracciones.net.catalogs

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentReference

/* Generic Headers */
@Keep
data class GenericCatalog(var value: String = "", val is_active: Boolean = true, var documentReference: DocumentReference? = null)

@Keep
data class GenericSubCatalog(var value: String = "", val reference: DocumentReference? = null, val is_active: Boolean = true,
                             var childReference: DocumentReference? = null)

@Keep
data class Holidays(var key: String = "", val data: DocumentReference? = null, var description: String = "",
                    var reference: DocumentReference? = null)

/* Collection Headers */
@Keep
data class Townships(val prefix: String = "PRX", val counter_prefix: Int = 0, val value: String = "", val reference: DocumentReference? = null, val is_active: Boolean = true,
                     var childReference: DocumentReference? = null, var uma_rate: Float = 0F, var surcharges_rate: Float = 0F, val discount: HashMap<String, ArrayList<Int>> = hashMapOf(),
                     val footer: HashMap<String, String> = hashMapOf(), val headers: HashMap<String, String> = hashMapOf())

@Keep
data class Articles(val number: String = "", val description: String = "", val is_active: Boolean = true, var documentReference: DocumentReference? = null)

@Keep
data class Fractions(val number: String = "", val description: String = "", val is_active: Boolean = true, val reference: DocumentReference? = null, val uma: Int = 0,
                     var childReference: DocumentReference? = null)

/* Remote Config Version */
@Keep
data class RemoteVersion(val url: String, val version: String)