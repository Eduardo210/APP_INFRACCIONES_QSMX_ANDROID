package mx.qsistemas.infracciones.modules.search

import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.helpers.activity_helper.ActivityHelper
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.modules.search.fr_historical.HistoricalFr
import mx.qsistemas.infracciones.modules.search.fr_search.SearchFr

class SearchRouter(val activity: ActivityHelper): SearchContracts.Router{
    override fun presentHistoricalFragment(direction: Direction) {
        activity.loadFragment(HistoricalFr.newInstance("",""), R.id.frame_search, direction, false)
    }

    override fun presentSearchFragment(direction: Direction) {
        activity.loadFragment(SearchFr.newInstance("",""), R.id.frame_search, direction, false )
    }

}