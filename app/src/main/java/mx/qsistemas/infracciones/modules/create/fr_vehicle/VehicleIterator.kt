package mx.qsistemas.infracciones.modules.create.fr_vehicle

import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.AuthorityIssues
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.GenericSubCatalog
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.utils.*

class VehicleIterator(val listener: VehicleContracts.Presenter) : VehicleContracts.Iterator {
    internal lateinit var brandList: MutableList<GenericCatalog>
    internal lateinit var subBrandList: MutableList<GenericSubCatalog>
    internal lateinit var typeVehicleList: MutableList<GenericCatalog>
    internal lateinit var colorList: MutableList<GenericCatalog>
    internal lateinit var identifierDocList: MutableList<GenericCatalog>
    internal lateinit var statesList: MutableList<States>
    internal lateinit var authorityIssuesList: MutableList<AuthorityIssues>

    override fun getBrandAdapter() {
        Application.firestore?.collection(FS_COL_BRANDS)?.whereEqualTo("is_active", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                brandList = mutableListOf()
                brandList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    brandList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onBrandReady(adapter)
            } else {
                brandList = mutableListOf()
                brandList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onBrandReady(adapter)
            }
        }
    }

    override fun getSubBrandAdapter(reference: DocumentReference?) {
        if (reference != null) {
            Application.firestore?.collection(FS_COL_MODELS)?.whereEqualTo("reference", reference)?.whereEqualTo("is_active", true)?.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(exception.message
                            ?: Application.getContext().getString(R.string.e_firestore_not_available))
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    subBrandList = mutableListOf()
                    subBrandList.add(GenericSubCatalog("Seleccionar...", reference, true))
                    val list = mutableListOf<String>()
                    list.add("Seleccionar...")
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericSubCatalog::class.java)!!
                        data.childReference = document.reference
                        list.add(data.value)
                        subBrandList.add(data)
                    }
                    val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                    adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                    listener.onSubBrandReady(adapter)
                }
            }
        } else {
            subBrandList = mutableListOf()
            subBrandList.add(GenericSubCatalog("Seleccionar...", reference, true))
            val list = mutableListOf<String>()
            list.add("Seleccionar...")
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onSubBrandReady(adapter)
        }
    }

    override fun getTypeAdapter() {
        Application.firestore?.collection(FS_COL_CLASS_TYPE)?.whereEqualTo("is_active", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                typeVehicleList = mutableListOf()
                typeVehicleList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    typeVehicleList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onTypeVehicleReady(adapter)
            } else {
                typeVehicleList = mutableListOf()
                typeVehicleList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onTypeVehicleReady(adapter)
            }
        }
    }

    override fun getColorAdapter() {
        Application.firestore?.collection(FS_COL_COLORS)?.whereEqualTo("is_active", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                colorList = mutableListOf()
                colorList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    colorList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onColorsReady(adapter)
            } else {
                colorList = mutableListOf()
                colorList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onColorsReady(adapter)
            }
        }
    }

    override fun getIdentifierDocAdapter() {
        Application.firestore?.collection(FS_COL_IDENTIF_DOC)?.whereEqualTo("is_active", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                identifierDocList = mutableListOf()
                identifierDocList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                for (document in snapshot.documents) {
                    val data = document.toObject(GenericCatalog::class.java)!!
                    data.documentReference = document.reference
                    list.add(data.value)
                    identifierDocList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onIdentifierDocReady(adapter)
            } else {
                identifierDocList = mutableListOf()
                identifierDocList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onIdentifierDocReady(adapter)
            }
        }
    }

    override fun getIssuedInAdapter() {
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("enable", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                statesList = mutableListOf()
                val list = mutableListOf<String>()
                snapshot.documents.forEach {
                    val data = it.toObject(States::class.java)!!
                    list.add(data.value)
                    statesList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onIssuedInReady(adapter)
            }
        }
    }

    override fun getTypeDocument(): ArrayAdapter<String> {
        authorityIssuesList = CatalogsAdapterManager.getAuthorityIssueList()
        val strings = mutableListOf<String>()
        authorityIssuesList.forEach {
            if (it.id == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.authority)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getPositionIdentifiedDoc(obj: GenericCatalog): Int {
        for (i in 0 until identifierDocList.size) {
            if (identifierDocList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionState(obj: States): Int {
        for (i in 0 until statesList.size) {
            if (statesList[i].id == obj.id) {
                return i
            }
        }
        return 0
    }

    override fun getPositionAuthority(obj: AuthorityIssues): Int {
        for (i in 0 until authorityIssuesList.size) {
            if (authorityIssuesList[i].id == obj.id) {
                return i
            }
        }
        return 0
    }

    override fun getPositionBrand(obj: GenericCatalog): Int {
        for (i in 0 until brandList.size) {
            if (brandList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionType(obj: GenericCatalog): Int {
        for (i in 0 until typeVehicleList.size) {
            if (typeVehicleList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }
}