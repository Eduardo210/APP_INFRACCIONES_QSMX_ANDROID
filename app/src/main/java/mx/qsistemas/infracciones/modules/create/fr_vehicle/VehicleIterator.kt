package mx.qsistemas.infracciones.modules.create.fr_vehicle

import android.widget.ArrayAdapter
import android.widget.RemoteViewsService
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.GenericSubCatalog
import mx.qsistemas.infracciones.utils.*

class VehicleIterator(val listener: VehicleContracts.Presenter) : VehicleContracts.Iterator {
    internal lateinit var brandList: MutableList<GenericCatalog>
    internal lateinit var subBrandList: MutableList<GenericSubCatalog>
    internal lateinit var typeVehicleList: MutableList<GenericCatalog>
    internal lateinit var colorList: MutableList<GenericCatalog>
    internal lateinit var identifierDocList: MutableList<GenericCatalog>
    internal lateinit var statesList: MutableList<GenericCatalog>
    internal lateinit var authorityIssuesList: MutableList<GenericCatalog>
    internal lateinit var typeServiceList: MutableList<GenericCatalog>

    override fun getBrandAdapter() {
        Application.firestore.collection(FS_COL_BRANDS).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
                }
                brandList = mutableListOf()
                brandList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericCatalog::class.java)!!
                        data.documentReference = document.reference
                        list.add(data.value)
                        brandList.add(data)
                    }

                }
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onBrandReady(adapter)
            }
    }

    override fun getSubBrandAdapter(reference: DocumentReference?) {
        if (reference != null) {
            Application.firestore.collection(FS_COL_MODELS)
                .whereEqualTo("reference", reference)
                .whereEqualTo("is_active", true)
                .orderBy("value", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        listener.onError(
                            exception.message
                                ?: Application.getContext()
                                    .getString(R.string.e_firestore_not_available)
                        )
                    }
                    subBrandList = mutableListOf()
                    subBrandList.add(GenericSubCatalog("Seleccionar...", reference, true))
                    val list = mutableListOf<String>()
                    list.add("Seleccionar...")
                    if (snapshot != null && !snapshot.isEmpty) {
                        for (document in snapshot.documents) {
                            val data = document.toObject(GenericSubCatalog::class.java)!!
                            data.childReference = document.reference
                            list.add(data.value)
                            subBrandList.add(data)
                        }
                        subBrandList.add(GenericSubCatalog("Otra...", null, true))
                        list.add("Otra...")
                    }
                    val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                    adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                    listener.onSubBrandReady(adapter)
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
        Application.firestore.collection(FS_COL_CLASS_TYPE).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
                }
                typeVehicleList = mutableListOf()
                typeVehicleList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericCatalog::class.java)!!
                        data.documentReference = document.reference
                        list.add(data.value)
                        typeVehicleList.add(data)
                    }
                }
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onTypeVehicleReady(adapter)
            }
    }

    override fun getColorAdapter() {
        Application.firestore.collection(FS_COL_COLORS).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
                }
                colorList = mutableListOf()
                colorList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericCatalog::class.java)!!
                        data.documentReference = document.reference
                        list.add(data.value)
                        colorList.add(data)
                    }
//                    colorList.add(GenericCatalog("Otro...", true, null))
//                    list.add("Otro...")
                }
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onColorsReady(adapter)
            }
    }

    override fun getIdentifierDocAdapter() {
        Application.firestore.collection(FS_COL_IDENTIF_DOC).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
                }
                identifierDocList = mutableListOf()
                identifierDocList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericCatalog::class.java)!!
                        data.documentReference = document.reference
                        list.add(data.value)
                        identifierDocList.add(data)
                    }
                }
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onIdentifierDocReady(adapter)
            }
    }

    override fun getIssuedInAdapter() {
        Application.firestore.collection(FS_COL_STATES).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
                }
                statesList = mutableListOf()
                statesList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericCatalog::class.java)!!
                        data.documentReference = document.reference
                        list.add(data.value)
                        statesList.add(data)
                    }
                }
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onIssuedInReady(adapter)
            }
    }

    override fun getTypeDocument() {
        Application.firestore.collection(FS_COL_TYPE_DOC).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
                }
                authorityIssuesList = mutableListOf()
                authorityIssuesList.add(GenericCatalog("Seleccionar...", true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericCatalog::class.java)!!
                        data.documentReference = document.reference
                        list.add(data.value)
                        authorityIssuesList.add(data)
                    }
                }
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onTypeDocReady(adapter)
            }
    }

    override fun getTypeService() {
        Application.firestore.collection(FS_COL_BRANDS).whereEqualTo("is_active", true)
            .orderBy("value", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    listener.onError(
                        exception.message
                            ?: Application.getContext()
                                .getString(R.string.e_firestore_not_available)
                    )
                }
                typeServiceList = mutableListOf()
                typeServiceList.add(GenericCatalog("Seleccionar...", true))
                typeServiceList.add(GenericCatalog("Público",true))
                typeServiceList.add(GenericCatalog("Particular",true))
                val list = mutableListOf<String>()
                list.add("Seleccionar...")
                list.add("Público")
                list.add("Particular")
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val data = document.toObject(GenericCatalog::class.java)!!
                        data.documentReference = document.reference
//                    list.add(data.value)
//                        typeServiceList.add(data)
                    }
                }
                val adapter =
                    ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onTypeServiceReady(adapter)
            }
    }

    override fun getPositionIdentifiedDoc(obj: GenericCatalog): Int {
        for (i in 0 until identifierDocList.size) {
            if (identifierDocList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionState(obj: GenericCatalog): Int {
        for (i in 0 until statesList.size) {
            if (statesList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionAuthority(obj: GenericCatalog): Int {
        for (i in 0 until authorityIssuesList.size) {
            if (authorityIssuesList[i].documentReference == obj.documentReference) {
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

    override fun getPositionSubBrand(obj: GenericSubCatalog): Int {
        for (i in 0 until subBrandList.size) {
            if (subBrandList[i].childReference == obj.childReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionColor(obj: GenericCatalog): Int {
        for (i in 0 until colorList.size) {
            if (colorList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }

    override fun getPositionTypeService(obj: GenericCatalog): Int {
        for (i in 0 until typeServiceList.size) {
            if (typeServiceList[i].documentReference == obj.documentReference) {
                return i
            }
        }
        return 0
    }
}