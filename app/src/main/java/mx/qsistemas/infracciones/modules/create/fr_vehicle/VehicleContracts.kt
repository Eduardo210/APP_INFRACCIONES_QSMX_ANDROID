package mx.qsistemas.infracciones.modules.create.fr_vehicle

import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentReference
import mx.qsistemas.infracciones.net.catalogs.GenericCatalog
import mx.qsistemas.infracciones.net.catalogs.GenericSubCatalog

class VehicleContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun takePhoto(requestCode: Int)
        fun onError(msg: String)
        fun onBrandReady(adapter: ArrayAdapter<String>)
        fun onSubBrandReady(adapter: ArrayAdapter<String>)
        fun onIdentifierDocReady(adapter: ArrayAdapter<String>)
        fun onIssuedInReady(adapter: ArrayAdapter<String>)
        fun onTypeDocReady(adapter: ArrayAdapter<String>)
        fun onColorsReady(adapter: ArrayAdapter<String>)
        fun onTypeVehicleReady(adapter: ArrayAdapter<String>)
        fun onTypeServiceReady(adapter: ArrayAdapter<String>)
        fun validFields(): Boolean
    }

    interface Iterator {
        fun getBrandAdapter()
        fun getSubBrandAdapter(reference: DocumentReference?)
        fun getTypeAdapter()
        fun getColorAdapter()
        fun getIdentifierDocAdapter()
        fun getIssuedInAdapter()
        fun getTypeDocument()
        fun getTypeService()
        fun getPositionIdentifiedDoc(obj: GenericCatalog): Int
        fun getPositionState(obj: GenericCatalog): Int
        fun getPositionAuthority(obj: GenericCatalog): Int
        fun getPositionBrand(obj: GenericCatalog): Int
        fun getPositionSubBrand(obj: GenericSubCatalog): Int
        fun getPositionType(obj: GenericCatalog): Int
        fun getPositionColor(obj: GenericCatalog): Int
        fun getPositionTypeService(obj: GenericCatalog) : Int
    }
}