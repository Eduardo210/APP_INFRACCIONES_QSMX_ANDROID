package mx.qsistemas.infracciones.modules.search

import mx.qsistemas.infracciones.db.entities.IdentifierDocument
import mx.qsistemas.infracciones.db.managers.SearchManager
import mx.qsistemas.infracciones.modules.search.adapters.ResumeInfraItem
import mx.qsistemas.infracciones.modules.search.adapters.SearchAdapter


class SearchIterator(private val listener: SearchContracts.Presenter) : SearchContracts.Iterator {

    lateinit var adapter: SearchAdapter
    var infractions = mutableListOf<ResumeInfraItem>()
    override fun selectAdapterReports(position: Int, listener: SearchContracts.OnIncidenceDetailClick): SearchAdapter {
        /* when(position){
             //TAB_ALL -> infractions = SearchManager.getFilterDocIdent()
         }*/
        adapter = SearchAdapter(infractions, listener)
        return adapter
    }


}