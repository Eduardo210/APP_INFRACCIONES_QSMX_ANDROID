package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.location.Geocoder
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.Application.Companion.TAG
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.net.catalogs.Articles
import mx.qsistemas.infracciones.net.catalogs.Fractions
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.Cities
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.*
import java.util.*

class InfractionIterator(val listener: InfractionContracts.Presenter) : InfractionContracts.Iterator {
    internal lateinit var zipCodesList: MutableList<ZipCodes>
    internal lateinit var coloniesList: MutableList<Colony>
    internal lateinit var articlesList: MutableList<Articles>
    internal lateinit var fractionList: MutableList<Fractions>
    internal lateinit var retainedDocList: MutableList<GenericCatalog>
    internal lateinit var dispositionList: MutableList<GenericCatalog>

    override fun getZipCodes() {
        zipCodesList = CatalogsFirebaseManager.getZipCodesByCityId("%${Application.prefs.loadData(R.string.sp_id_township, "")!!}%")
        zipCodesList.add(0, ZipCodes(0, "", "Selecciona...", "", true))
        val list = mutableListOf<String>()
        zipCodesList.forEach {
            list.add(it.value)
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        listener.onZipCodesReady(adapter)
    }

    override fun getColonies(reference: String) {
        coloniesList = CatalogsFirebaseManager.getColoniesByZipCode(if (reference.isBlank()) 0 else reference.toInt())
        val list = mutableListOf<String>()
        coloniesList.add(0, Colony(0, "", "Selecciona...", reference, true))
        coloniesList.forEach {
            list.add(it.value)
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        listener.onColoniesReady(adapter)
    }

    override fun getArticlesAdapter() {
//        Application.firestore.collection(FS_COL_ARTICLES).whereEqualTo("is_active", true).orderBy("number", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
//            if (exception != null) {
//                listener.onError(exception.message
//                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
//            }
//            articlesList = mutableListOf()
//            articlesList.add(Articles("Selecciona...", "Selecciona...", true))
//            val list = mutableListOf<String>()
//            list.add("Selecciona...")
//            if (snapshot != null && !snapshot.isEmpty) {
//                for (document in snapshot.documents) {
//                    val data = document.toObject(Articles::class.java)!!
//                    data.documentReference = document.reference
//                    list.add("Art. ${data.number}")
//                    articlesList.add(data)
//                }
//            }
//            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
//            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
//            listener.onArticlesReady(adapter)
//        }

        articlesList = mutableListOf()
        val stateRef = Application.firestore.collection(FS_COL_STATES).document(Application.prefs.loadData(R.string.sp_id_state, "")!!)
        Application.firestore.collection(FS_COL_ARTICLES).whereArrayContains("states", stateRef).whereEqualTo("is_active", true)
            .orderBy("number").get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    querySnapshot.documents.forEach { document ->
                        val article = document.toObject(Articles::class.java)!!
                        article.documentReference = document.reference
                        articlesList.add(article)
                        Log.i("articulo", article.toString())
                    }
                }
                val list = mutableListOf<String>()
                articlesList.add(0, Articles("Selecciona...", "", true, null))
                articlesList.forEach { article ->
                    if (article.documentReference == null) list.add("Selecciona...") else list.add("Artículo ${article.number}")
                    Log.i("art", article.documentReference.toString())
                }

                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onArticlesReady(adapter)
            }.addOnFailureListener { exception ->
                Log.e(Application.TAG, exception.toString())
                listener.onError(Application.getContext().getString(R.string.e_firestore_not_available))
            }
    }

    override fun getFractionAdapter(reference: DocumentReference?) {
        Application.firestore.collection(FS_COL_FRACTIONS).whereEqualTo("reference", reference).whereEqualTo("is_active", true).orderBy("number", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            fractionList = mutableListOf()
            fractionList.add(Fractions("Selecciona...", "Selecciona...", true, reference, 0))
            val list = mutableListOf<String>()
            list.add("Selecciona...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(Fractions::class.java)!!
                    data.childReference = document.reference
                    list.add("Fr. ${data.number}")
                    fractionList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onFractionsReady(adapter)
        }
    }

    override fun getRetainedDocAdapter() {
        Application.firestore.collection(FS_COL_INSURED_DOC).whereEqualTo("is_active", true).orderBy("value", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            retainedDocList = mutableListOf()
            retainedDocList.add(GenericCatalog("Selecciona...", true))
            val list = mutableListOf<String>()
            list.add("Selecciona...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    retainedDocList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onRetainedDocReady(adapter)
        }
    }

    override fun getDispositionAdapter() {
        Application.firestore.collection(FS_COL_CRANES).whereEqualTo("is_active", true).orderBy("value", Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            dispositionList = mutableListOf()
            dispositionList.add(GenericCatalog("Selecciona...", true))
            val list = mutableListOf<String>()
            list.add("Selecciona...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    dispositionList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onDispositionReady(adapter)
        }
    }

    override fun saveNewArticle(posArticle: Int, posFraction: Int) {
        SingletonInfraction.motivationList.add(0, SingletonInfraction.DtoMotivation(articlesList[posArticle],
                fractionList[posFraction], ""))

    }

//    override fun getPositionFraction(): Int {
//        val idFraction = Application.prefs.loadData(R.string.sp_default_fraction, "")!!
//        fractionList.forEachIndexed { index, fraction ->
//            if (fraction.childReference?.id == idFraction)
//                return index
//        }
//        return 0
//    }

    override fun saveTownship() {
        val cityReference = Application.prefs.loadData(R.string.sp_id_township, "")!!
        Application.firestore.collection(FS_COL_CITIES).document(cityReference).get().addOnSuccessListener { townshipSnapshot ->
            if (townshipSnapshot == null) {
                Log.e(TAG, Application.getContext().getString(R.string.e_firestore_not_available))
            } else {
                val township = townshipSnapshot.toObject(Cities::class.java) ?: Cities()
                SingletonInfraction.townshipInfraction = township
                SingletonInfraction.townshipInfraction.childReference = townshipSnapshot.reference
                Application.firestore.collection(FS_COL_STATES).document(township.reference!!.id).get().addOnSuccessListener {
                    if (it == null) {
                        Log.e(TAG, Application.getContext().getString(R.string.e_firestore_not_available))
                    } else {
                        val state = it.toObject(GenericCatalog::class.java) ?: GenericCatalog()
                        SingletonInfraction.stateInfraction = state
                        SingletonInfraction.stateInfraction.documentReference = it.reference
                    }
                }
            }
        }
    }

    override fun getPositionZipCode(obj: ZipCodes): Int {
        for (i in 0 until zipCodesList.size) {
            if (zipCodesList[i].key == obj.key) {
                return i
            }
        }
        return 0
    }

    override fun getPositionColony(obj: Colony): Int {
        for (i in 0 until coloniesList.size) {
            if (coloniesList[i].key == obj.key) {
                return i
            }
        }
        return 0
    }

    override fun getPositionRetainedDoc(obj: GenericCatalog): Int {
        for (i in 0 until retainedDocList.size) {
            if (retainedDocList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionDisposition(obj: GenericCatalog): Int {
        for (i in 0 until dispositionList.size) {
            if (dispositionList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getAddressFromCoordinates(lat: Double, lon: Double) {
        if (Validator.isNetworkEnable(Application.getContext())) {
            val geocoder = Geocoder(Application.getContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses.isNotEmpty()) {
                listener.onAddressLocated(addresses[0].subLocality, addresses[0].subThoroughfare, addresses[0].locality, "")
            }else{
//                listener.onAddressEmpty("Error al obtener dirección automática.")
                Log.w("LocationInfra","No address returned!" )
            }

        }
    }
}