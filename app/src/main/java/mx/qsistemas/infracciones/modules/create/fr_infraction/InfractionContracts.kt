package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.db.entities.Disposition
import mx.qsistemas.infracciones.db.entities.RetainedDocument

class InfractionContracts {
    interface Presenter {
        fun initAdapters()
        fun fillFields()
        fun validFields(): Boolean
        fun onError(msg: String)
    }

    interface Iterator {
        fun getArticlesAdapter(): ArrayAdapter<String>
        fun getFractionAdapter(positionArticle: Int): ArrayAdapter<String>
        fun getRetainedDocAdapter(): ArrayAdapter<String>
        fun getDispositionAdapter(): ArrayAdapter<String>
        fun saveNewArticle(posArticle: Int, posFraction: Int)
        fun getPositionRetainedDoc(obj: RetainedDocument): Int
        fun getPositionDisposition(obj: Disposition): Int
    }
}