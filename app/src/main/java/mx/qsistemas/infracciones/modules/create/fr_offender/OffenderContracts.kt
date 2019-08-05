package mx.qsistemas.infracciones.modules.create.fr_offender

import android.app.Activity
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import mx.qsistemas.infracciones.db.entities.LicenseType
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.GenericSubCatalog
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class OffenderContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun onError(msg: String)
        fun onStatesReady(adapter: ArrayAdapter<String>)
        fun onTownshipsReady(adapter: ArrayAdapter<String>)
        fun onZipCodesReady(adapter: ArrayAdapter<String>)
        fun onColoniesReady(adapter: ArrayAdapter<String>)
        fun onStatesIssuedReady(adapter: ArrayAdapter<String>)
        fun validFields(): Boolean
        fun isDirectionAnswered(): Boolean
        fun isLicenseAnswered(): Boolean
        fun onDataSaved()
        fun onDataDuplicate()
        fun onDataUpdated(idPerson: Long)
        fun onTicketPrinted()
        fun onResultSavePayment(msg: String, flag: Boolean)
    }

    interface Iterator {
        fun getStatesList()
        fun getTownshipsList(reference: DocumentReference?)
        fun getZipCodesList(reference: DocumentReference?)
        fun getColoniesList(reference: DocumentReference?)
        fun getTypeLicenseAdapter(): ArrayAdapter<String>
        fun getStatesIssuedList()
        fun searchZipCodeLocal(zipCode: String): GenericSubCatalog
        fun getPositionColony(obj: GenericSubCatalog): Int
        fun getPositionTypeLicense(obj: LicenseType): Int
        fun getPositionStateLicense(obj: GenericCatalog): Int
        fun saveData(notify: Boolean)
        fun savePayment(info: TransactionInfo)
        fun updateData()
        fun printTicket(activity: Activity)
        fun reprintVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener)
        fun savePaymentToService(idInfraction: String, txInfo: TransactionInfo, amount: String, discount: String, totalPayment: String, idPerson: Long)
    }
}