package mx.qsistemas.infracciones.modules.create.fr_offender

import android.widget.ArrayAdapter

class OffenderContracts {
    interface Presenter {
        fun initAdapters()
        fun onError(msg: String)
        fun onStatesReady(adapter: ArrayAdapter<String>)
        fun onTownshipsReady(adapter: ArrayAdapter<String>)
    }

    interface Iterator {
        fun getStatesList()
        fun getTownshipsList(posState: Int)
        fun getTypeLicenseAdapter(): ArrayAdapter<String>
    }
}