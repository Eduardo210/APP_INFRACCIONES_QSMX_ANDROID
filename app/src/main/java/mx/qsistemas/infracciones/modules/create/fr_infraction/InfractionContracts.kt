package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog

class InfractionContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun startLocationListener()
        fun onAddressLocated(colony: String, street: String, betweenStreet: String, andStreet: String)
        fun onAddressEmpty(msg: String)
        fun validFields(): Boolean
        fun onError(msg: String)
        fun onZipCodesReady(adapter: ArrayAdapter<String>)
        fun onColoniesReady(adapter: ArrayAdapter<String>)
        fun onArticlesReady(adapter: ArrayAdapter<String>)
        fun onFractionsReady(adapter: ArrayAdapter<String>)
        fun onRetainedDocReady(adapter: ArrayAdapter<String>)
        fun onDispositionReady(adapter: ArrayAdapter<String>)
    }

    interface Iterator {
        fun getZipCodes()
        fun getColonies(reference: String)
        fun getArticlesAdapter()
        fun getFractionAdapter(reference: DocumentReference?)
        fun getRetainedDocAdapter()
        fun getDispositionAdapter()
        fun saveNewArticle(posArticle: Int, posFraction: Int)
        fun saveTownship()
        fun getPositionZipCode(obj: ZipCodes): Int
        fun getPositionColony(obj: Colony): Int
        fun getPositionRetainedDoc(obj: GenericCatalog): Int
        fun getPositionDisposition(obj: GenericCatalog): Int
        fun getAddressFromCoordinates(lat: Double, lon: Double)

    }
}