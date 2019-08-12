package mx.qsistemas.infracciones.modules.search


import android.app.Activity
import android.view.View
import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.db.entities.InfractionLocal
import mx.qsistemas.infracciones.db_web.entities.InfractionItemList
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.net.catalogs.InfractionSearch
import mx.qsistemas.infracciones.singletons.SingletonTicket
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class SearchContracts {
    interface Presenter {
        fun onError(msg: String)
        fun onResultSearch(listInfractions: MutableList<InfractionList.Results>)
        fun onResultSearchOffLine(listInfractions: MutableList<InfractionItemList>)
        fun onResultInfractionById(infraction: InfractionSearch, origin: Int)
        fun onResultInfractionByIdOffline(infraction: InfractionLocal, infra_fracc: MutableList<SingletonTicket.ArticleFraction> ,origin: Int)
        fun onResultSavePayment(msg: String, flag: Boolean)
        fun onTicketPrinted()
    }

    interface Iterator {
        fun getDocIdentAdapter(): ArrayAdapter<NewIdentDocument>
        fun doSearchByFilter(id: Int, filter: String)
        suspend fun doSearchByFilterOffLine(id: Int, filter: String)
        fun doSearchByIdInfraction(id: String, origin: Int)
        fun doSearchByIdInfractionOffLine(id: String, origin: Int)
        fun savePaymentToService(idInfraction: String, txInfo: TransactionInfo, amount: String, discount: String, totalPayment: String, idPerson: Long)
        fun printTicket(activity: Activity)
    }

    interface OnInfractionClick {
        fun onPrintClick(view: View, position: Int, origin: Int)
        fun onPaymentClick(view: View, position: Int, origin: Int)
    }

    interface Router {
        fun presentHistoricalFragment(direction: Direction)
        fun presentSearchFragment(direction: Direction)
    }
}