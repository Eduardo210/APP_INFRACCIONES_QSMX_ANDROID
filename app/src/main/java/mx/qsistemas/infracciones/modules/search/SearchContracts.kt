package mx.qsistemas.infracciones.modules.search


import android.view.View
import android.widget.ArrayAdapter

import mx.qsistemas.infracciones.helpers.activity_helper.Direction

import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.net.catalogs.InfractionSearch
import java.text.FieldPosition

class SearchContracts {
    interface Presenter {
        fun onError(msg: String)
        fun onResultSearch(listInfractions: MutableList<InfractionList.Results>)
        fun onResultInfractionById(infraction: InfractionSearch)
    }

    interface Iterator {
        fun getDocIdentAdapter(): ArrayAdapter<NewIdentDocument>
        fun doSearchByFilter(id: Int, filter: String)
        fun doSearchByIdInfraction(id:String)
    }
    interface OnInfractionClick{
        fun onPrintClick(view: View, position:Int)
        fun onPaymentClick(view: View, position: Int)
    }

    interface Router {
        fun presentHistoricalFragment(direction: Direction)
        fun presentSearchFragment(direction: Direction)
    }
}