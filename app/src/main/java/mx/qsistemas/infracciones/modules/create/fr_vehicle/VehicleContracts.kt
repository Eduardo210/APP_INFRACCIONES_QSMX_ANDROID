package mx.qsistemas.infracciones.modules.create.fr_vehicle

import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.db.entities.AuthorityIssues
import mx.qsistemas.infracciones.db.entities.IdentifierDocument
import mx.qsistemas.infracciones.db.entities.VehicleBrand
import mx.qsistemas.infracciones.db.entities.VehicleType
import mx.qsistemas.infracciones.net.catalogs.States

class VehicleContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun takePhoto(requestCode: Int)
        fun onError(msg: String)
        fun onIssuedInReady(adapter: ArrayAdapter<String>)
        fun validFields(): Boolean
    }

    interface Iterator {
        fun getBrandAdapter(): ArrayAdapter<String>
        fun getSubBrandAdapter(idBrand: Int): ArrayAdapter<String>
        fun getTypeAdapter(): ArrayAdapter<String>
        fun getColorAdapter(): ArrayAdapter<String>
        fun getIdentifierDocAdapter(): ArrayAdapter<String>
        fun getIssuedInAdapter()
        fun getTypeDocument(): ArrayAdapter<String>
        fun getPositionIdentifiedDoc(obj: IdentifierDocument): Int
        fun getPositionState(obj: States): Int
        fun getPositionAuthority(obj: AuthorityIssues): Int
        fun getPositionBrand(obj: VehicleBrand): Int
        fun getPositionType(obj: VehicleType): Int
    }
}