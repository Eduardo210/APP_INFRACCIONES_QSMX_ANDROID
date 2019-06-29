package mx.qsistemas.infracciones.modules.search

import mx.qsistemas.infracciones.net.catalogs.InfractionList

class HistoricalConstracts {
    interface Presenter {
        fun onError(msg:String)
        fun onResultSearch(listInfractions: MutableList<InfractionList.Results>)

    }
    interface Iterator{
        fun doSearch()
    }
}