package mx.qsistemas.infracciones.modules.search


import android.app.Activity
import android.view.View
import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.db_web.entities.InfractionItem
import mx.qsistemas.infracciones.db_web.entities.InfringementData
import mx.qsistemas.infracciones.helpers.activity_helper.Direction
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.result_web.detail_result.DetailResult
import mx.qsistemas.infracciones.net.result_web.search_result.DataItem
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class SearchContracts {
    interface Presenter {
        fun onError(msg: String)
        fun onResultSearch(listInfractions: MutableList<DataItem>)
        fun onResultSearchOffLine(listInfractions: MutableList<InfractionItem>)
        fun onResultInfractionById(infraction: DetailResult, origin: Int)
        suspend fun onResultInfractionByIdOffline(infraction: InfringementData,origin: Int)
        fun onResultSavePayment(msg: String, flag: Boolean)
        fun onTicketPrinted()
        fun onIdentifierDocReady(adapter: ArrayAdapter<String>)
    }

    interface Iterator {
        //fun getDocIdentAdapter(): ArrayAdapter<NewIdentDocument>
        fun doSearchByFilter(filter: String)
        suspend fun doSearchByFilterOffLine(filter: String)
        fun doSearchByIdInfraction(id: String, origin: Int)
        suspend fun doSearchByIdInfractionOffLine(id: String, origin: Int)
        fun savePaymentToService(idInfraction: String, txInfo: TransactionInfo, amount: String, discount: String, totalPayment: String, idPerson: Long)
        fun printTicket(activity: Activity)
        fun getIdentifierDocAdapter()
        fun getPositionIdentifiedDoc(obj: GenericCatalog): Int
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