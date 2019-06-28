package mx.qsistemas.infracciones.modules.create.fr_vehicle

import android.widget.ArrayAdapter

class VehicleContracts {
    interface Presenter {
        fun initAdapters()
        fun onError(msg: String)
        fun onIssuedInReady(adapter: ArrayAdapter<String>)

    }

    interface Iterator {
        fun getBrandAdapter(): ArrayAdapter<String>
        fun getSubBrandAdapter(idBrand: Int): ArrayAdapter<String>
        fun getYearAdapter(): ArrayAdapter<String>
        fun getTypeAdapter(): ArrayAdapter<String>
        fun getColorAdapter(): ArrayAdapter<String>
        fun getIdentifierDocAdapter(): ArrayAdapter<String>
        fun getIssuedInAdapter()
        fun getTypeDocument(): ArrayAdapter<String>
    }
}