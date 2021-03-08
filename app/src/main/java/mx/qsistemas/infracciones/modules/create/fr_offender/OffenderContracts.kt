package mx.qsistemas.infracciones.modules.create.fr_offender

import android.app.Activity
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.City
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.payments_transfer.IPaymentsTransfer
import mx.qsistemas.payments_transfer.dtos.TransactionInfo

class OffenderContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun onError(msg: String)
        fun onStatesReady(adapter: ArrayAdapter<String>)
        fun onTownshipsReady(adapter: ArrayAdapter<String>)
        /*fun onZipCodesReady(adapter: ArrayAdapter<String>)
        fun onColoniesReady(adapter: ArrayAdapter<String>)*/
        fun onStatesIssuedReady(adapter: ArrayAdapter<String>)
        fun onTypeLicenseReady(adapter: ArrayAdapter<String>)
        fun validFields(): Boolean
        fun isDirectionAnswered(): Boolean
        fun isLicenseAnswered(): Boolean
        fun onDataSaved()
        fun onTicketPrinted()

    }

    interface Iterator {
        fun getStatesList()
        fun getTownshipsList(reference: DocumentReference?)
        //fun getZipCodesList(reference: String)
        //fun getColoniesList(reference: String)
        fun getTypeLicenseAdapter()
        fun getStatesIssuedList()
        fun getHolidays()
        fun getPositionState(obj: GenericCatalog): Int
        fun getPositionTownship(obj: City): Int
        /*fun getPositionZipCode(obj: ZipCodes): Int
        fun getPositionColony(obj: Colony): Int*/
        fun getPositionTypeLicense(obj: GenericCatalog): Int
        fun getPositionStateLicense(obj: GenericCatalog): Int
        fun saveData(notify: Boolean)
        fun savePayment(info: TransactionInfo)
        fun printTicket(activity: Activity)
        fun reprintVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener)
    }
}