package mx.qsistemas.infracciones.modules.create.fr_offender

import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.db.entities.LicenseType
import mx.qsistemas.infracciones.net.catalogs.States
import mx.qsistemas.infracciones.net.catalogs.Townships

class OffenderContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun onError(msg: String)
        fun onStatesReady(adapter: ArrayAdapter<String>)
        fun onTownshipsReady(adapter: ArrayAdapter<String>)
        fun validFields(): Boolean
        fun isDirectionAnswered(): Boolean
        fun isLicenseAnswered(): Boolean
        fun onDataSaved()
        fun onDataUpdated()
    }

    interface Iterator {
        fun getStatesList()
        fun getTownshipsList(posState: Int)
        fun getTypeLicenseAdapter(): ArrayAdapter<String>
        fun getPositionState(obj: States): Int
        fun getPositionTownship(obj: Townships): Int
        fun getPositionTypeLicense(obj: LicenseType): Int
        fun saveData(notify: Boolean)
        fun updateData()
    }
}