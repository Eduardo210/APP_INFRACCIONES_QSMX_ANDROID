package mx.qsistemas.infracciones.modules.search

import android.view.View
import mx.qsistemas.infracciones.modules.search.adapters.SearchAdapter

class SearchContracts {
    interface Presenter{
        fun onError(msg:String)
        fun onResultSearch()
    }
    interface Iterator{
        fun selectAdapterReports(position: Int, listener: OnIncidenceDetailClick): SearchAdapter
    }
    interface OnIncidenceDetailClick{
        fun onDetailClick(view: View, position: Int)
    }
    interface Router {
        fun PresentSearchActivity()
    }
}