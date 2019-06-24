package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.widget.ArrayAdapter

class InfractionContracts {
    interface Presenter {
        fun initAdapters()
        fun onError(msg: String)
    }

    interface Iterator {
        fun getArticlesAdapter(): ArrayAdapter<String>
        fun getFractionAdapter(positionArticle: Int): ArrayAdapter<String>
        fun getRetainedDocAdapter(): ArrayAdapter<String>
        fun getDispositionAdapter(): ArrayAdapter<String>
        fun saveNewArticle(posArticle: Int, posFraction: Int)
    }
}