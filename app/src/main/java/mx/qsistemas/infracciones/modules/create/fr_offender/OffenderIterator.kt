package mx.qsistemas.infracciones.modules.create.fr_offender

import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.LicenseType
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.net.catalogs.Townships
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.FS_COL_STATES
import mx.qsistemas.infracciones.utils.FS_COL_TOWNSHIPS

class OffenderIterator(val listener: OffenderContracts.Presenter) : OffenderContracts.Iterator {
    internal lateinit var statesList: MutableList<States>
    internal lateinit var townshipsList: MutableList<Townships>
    internal lateinit var licenseTypeList: MutableList<LicenseType>

    override fun getStatesList() {
        Application.firestore?.collection(FS_COL_STATES)?.whereEqualTo("enable", true)?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                listener?.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val list = mutableListOf<String>()
                statesList = mutableListOf()
                for (document in snapshot.documents) {
                    val data = document.toObject(States::class.java)!!
                    list.add(data.value)
                    statesList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onStatesReady(adapter)
            }
        }
    }

    override fun getTownshipsList(posState: Int) {
        SingletonInfraction.idStateOffender = statesList[posState].id
        Application.firestore?.collection(FS_COL_TOWNSHIPS)?.whereEqualTo("id_state", SingletonInfraction.idStateOffender)?.whereEqualTo("enable", true)?.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                listener?.onError(exception.message
                        ?: Application.getContext().getString(R.string.e_firestore_not_available))
            }
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                val list = mutableListOf<String>()
                townshipsList = mutableListOf()
                for (document in querySnapshot.documents) {
                    val data = document.toObject(Townships::class.java)!!
                    list.add(data.value)
                    townshipsList.add(data)
                }
                val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                listener.onTownshipsReady(adapter)
            }
        }
    }

    override fun getTypeLicenseAdapter(): ArrayAdapter<String> {
        licenseTypeList = CatalogsAdapterManager.getLicenseTypeList()
        val strings = mutableListOf<String>()
        licenseTypeList.forEach {
            if (it.id == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.license_type)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }
}