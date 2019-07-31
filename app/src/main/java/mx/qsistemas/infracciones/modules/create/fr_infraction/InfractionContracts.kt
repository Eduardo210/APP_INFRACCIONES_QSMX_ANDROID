package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import mx.qsistemas.infracciones.db.entities.Disposition
import mx.qsistemas.infracciones.db.entities.RetainedDocument

class InfractionContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun startLocationListener()
        fun onAddressLocated(colony: String, street: String, betweenStreet: String, andStreet: String)
        fun validFields(): Boolean
        fun onError(msg: String)
        fun onZipCodesReady(adapter: ArrayAdapter<String>)
        fun onColoniesReady(adapter: ArrayAdapter<String>)
    }

    interface Iterator {
        fun getZipCodes()
        fun getColonies(reference: DocumentReference?)
        fun getArticlesAdapter(): ArrayAdapter<String>
        fun getFractionAdapter(positionArticle: Int): ArrayAdapter<String>
        fun getRetainedDocAdapter(): ArrayAdapter<String>
        fun getDispositionAdapter(): ArrayAdapter<String>
        fun saveNewArticle(posArticle: Int, posFraction: Int)
        fun getPositionRetainedDoc(obj: RetainedDocument): Int
        fun getPositionDisposition(obj: Disposition): Int
        fun getAddressFromCoordinates(lat: Double, lon: Double)
    }
}