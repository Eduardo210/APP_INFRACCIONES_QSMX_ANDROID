package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.location.Geocoder
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.net.catalogs.*
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.*
import java.util.*

class InfractionIterator(val listener: InfractionContracts.Presenter) : InfractionContracts.Iterator {
    internal lateinit var zipCodesList: MutableList<GenericSubCatalog>
    internal lateinit var coloniesList: MutableList<GenericSubCatalog>
    internal lateinit var articlesList: MutableList<Articles>
    internal lateinit var fractionList: MutableList<Fractions>
    internal lateinit var retainedDocList: MutableList<GenericCatalog>
    internal lateinit var dispositionList: MutableList<GenericCatalog>

    override fun getZipCodes() {
        Application.firestore?.collection(FS_COL_TERMINALS)?.document(Utils.getImeiDevice(Application.getContext()))?.get()?.addOnSuccessListener {
            if (it == null) {
                listener.onError(Application.getContext().getString(R.string.e_firestore_not_available))
            } else {
                val cityReference = it["city"] as DocumentReference
                Application.firestore?.collection(FS_COL_ZIP_CODES)?.whereEqualTo("reference", cityReference)?.whereEqualTo("is_active", true)?.addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        listener.onError(exception.message
                                ?: Application.getContext().getString(R.string.e_firestore_not_available))
                    }
                    zipCodesList = mutableListOf()
                    zipCodesList.add(GenericSubCatalog("Selecciona...", cityReference, true))
                    val list = mutableListOf<String>()
                    list.add("Selecciona...")
                    if (snapshot != null && !snapshot.isEmpty) {
                        for (document in snapshot.documents) {
                            val data = document.toObject(GenericSubCatalog::class.java)!!
                            data.childReference = document.reference
                            data.value = document.id
                            list.add(data.value)
                            zipCodesList.add(data)
                        }
                    }
                    val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                    adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                    listener.onZipCodesReady(adapter)
                }
            }
        }
    }

    override fun getColonies(reference: DocumentReference?) {
        Application.firestore?.collection(FS_COL_COLONIES)?.whereEqualTo("reference", reference)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            coloniesList = mutableListOf()
            coloniesList.add(GenericSubCatalog("Selecciona...", reference, true))
            val list = mutableListOf<String>()
            list.add("Selecciona...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericSubCatalog::class.java)!!
                    data.childReference = document.reference
                    list.add(data.value)
                    coloniesList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onColoniesReady(adapter)
        }
    }

    override fun getArticlesAdapter() {
        Application.firestore?.collection(FS_COL_ARTICLES)?.whereEqualTo("is_active", true)?.orderBy("number", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            articlesList = mutableListOf()
            articlesList.add(Articles("Selecciona...", "Selecciona...", true))
            val list = mutableListOf<String>()
            list.add("Selecciona...")
            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val data = document.toObject(Articles::class.java)!!
                    data.documentReference = document.reference
                    list.add("Art. ${data.number}")
                    articlesList.add(data)
                }
            }
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onArticlesReady(adapter)
        }
    }

    override fun getFractionAdapter(reference: DocumentReference?) {
        Application.firestore?.collection(FS_COL_FRACTIONS)?.whereEqualTo("reference", reference)?.whereEqualTo("is_active", true)?.orderBy("number", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
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
        Application.firestore?.collection(FS_COL_INSURED_DOC)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
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
        Application.firestore?.collection(FS_COL_CRANES)?.whereEqualTo("is_active", true)?.orderBy("value", Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, exception ->
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

    override fun saveTownship() {
        Application.firestore?.collection(FS_COL_CITIES)?.document(SingletonInfraction.zipCodeInfraction.reference!!.id)?.get()?.addOnSuccessListener { townshipSnapshot ->
            if (townshipSnapshot == null) {
                Log.e(this.javaClass.simpleName, Application.getContext().getString(R.string.e_firestore_not_available))
            } else {
                val township = townshipSnapshot.toObject(Townships::class.java) ?: Townships()
                SingletonInfraction.townshipInfraction = township
                SingletonInfraction.townshipInfraction.childReference = townshipSnapshot.reference
                Application.firestore?.collection(FS_COL_STATES)?.document(township.reference!!.id)?.get()?.addOnSuccessListener {
                    if (it == null) {
                        Log.e(this.javaClass.simpleName, Application.getContext().getString(R.string.e_firestore_not_available))
                    } else {
                        val state = it.toObject(GenericCatalog::class.java) ?: GenericCatalog()
                        SingletonInfraction.stateInfraction = state
                        SingletonInfraction.stateInfraction.documentReference = it.reference
                    }
                }
            }
        }
    }

    override fun getPositionZipCode(obj: GenericSubCatalog): Int {
        for (i in 0 until zipCodesList.size) {
            if (zipCodesList[i].childReference == obj.childReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionColony(obj: GenericSubCatalog): Int {
        for (i in 0 until coloniesList.size) {
            if (coloniesList[i].childReference == obj.childReference) {
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
            }
        }
    }
}