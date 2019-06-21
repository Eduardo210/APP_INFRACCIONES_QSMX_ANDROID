package mx.qsistemas.infracciones.modules.create.fr_vehicle

import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.*
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.utils.FS_COL_STATES

class VehicleIterator(val listener: VehicleContracts.Presenter) : VehicleContracts.Iterator {
    internal lateinit var brandList: MutableList<VehicleBrand>
    internal lateinit var subBrandList: MutableList<SubmarkingVehicle>
    internal lateinit var typeVehicleList: MutableList<VehicleType>
    internal lateinit var colorList: MutableList<Colour>
    internal lateinit var identifierDocList: MutableList<IdentifierDocument>
    internal lateinit var statesList: MutableList<States>
    internal lateinit var authorityIssuesList: MutableList<AuthorityIssues>

    override fun getBrandAdapter(): ArrayAdapter<String> {
        brandList = CatalogsAdapterManager.getBrandList()
        val strings = mutableListOf<String>()
        brandList.forEach {
            if (it.id == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.vehicle_brand)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getSubBrandAdapter(idBrand: Int): ArrayAdapter<String> {
        subBrandList = CatalogsAdapterManager.getSubBrandList(idBrand)
        val strings = mutableListOf<String>()
        subBrandList.forEach {
            if (idBrand == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.subbrand)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getYearAdapter(): ArrayAdapter<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTypeAdapter(): ArrayAdapter<String> {
        typeVehicleList = CatalogsAdapterManager.getTypeVehicleList()
        val strings = mutableListOf<String>()
        typeVehicleList.forEach {
            if (it.id == 0L) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.type_string)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getColorAdapter(): ArrayAdapter<String> {
        colorList = CatalogsAdapterManager.getColorList()
        val strings = mutableListOf<String>()
        colorList.forEach {
            if (it.id == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.colour)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getIdentifierDocAdapter(): ArrayAdapter<String> {
        identifierDocList = CatalogsAdapterManager.getIdentifierDocList()
        val strings = mutableListOf<String>()
        identifierDocList.forEach {
            if (it.id == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.document)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getIssuedInAdapter() {
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("enable", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener?.onError(exception.message
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
}