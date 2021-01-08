package mx.qsistemas.infracciones.modules.main.fr_my_preferences

import android.widget.ArrayAdapter

class MyPreferencesContracts {
    interface Presenter {
        fun onPreferencesSaved()
    }

    interface Iterator {
        fun getPostalCodesAdapter(): ArrayAdapter<String>
        fun getColoniesAdapter(postalCode: String): ArrayAdapter<String>
        /*fun getArticlesAdapter(): ArrayAdapter<String>
        fun getFractionAdapter(positionArticle: Int): ArrayAdapter<String>*/
        fun saveDefaultDirection(zipCode: String, colony: String, street: String, stree1: String, street2: String)
        /*fun saveDefaultMotivation(idArticle: Int, idFraction: Int, motivation: String)
        fun getPositionArticle(): Int
        fun getPositionFraction(): Int*/
    }
}