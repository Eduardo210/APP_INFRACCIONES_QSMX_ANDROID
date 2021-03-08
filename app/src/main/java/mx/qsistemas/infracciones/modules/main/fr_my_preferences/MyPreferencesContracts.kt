package mx.qsistemas.infracciones.modules.main.fr_my_preferences

import android.widget.ArrayAdapter

class MyPreferencesContracts {
    interface Presenter {
        fun onError(msg: String)
        fun onPreferencesSaved()
        fun onArticlesLoad(adapter: ArrayAdapter<String>)
        fun onFractionsLoad(adapter: ArrayAdapter<String>)
    }

    interface Iterator {
        //fun getPostalCodesAdapter(): ArrayAdapter<String>
        //fun getColoniesAdapter(postalCode: String): ArrayAdapter<String>
        fun getArticlesAdapter()
        fun getFractionAdapter(articlePosition: Int)
        fun saveDefaultDirection(colony: String, street: String, street1: String, street2: String)
        fun saveDefaultMotivation(idArticle: String, idFraction: String, motivation: String)
        fun getPositionArticle(): Int
        fun getPositionFraction(): Int
        //fun getPositionZipCode(): Int
        //fun getPositionColony(): Int
    }
}