package mx.qsistemas.infracciones.modules.main.fr_my_preferences

import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.Articles
import mx.qsistemas.infracciones.db.entities.InfractionFraction
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager

class MyPreferencesIterator(private val listener: MyPreferencesContracts.Presenter) : MyPreferencesContracts.Iterator {

    internal lateinit var postalCodesList: MutableList<ZipCodes>
    internal lateinit var coloniesList: MutableList<Colony>
    internal lateinit var articlesList: MutableList<Articles>
    internal lateinit var fractionList: MutableList<InfractionFraction>

    override fun getPostalCodesAdapter(): ArrayAdapter<String> {
        postalCodesList = CatalogsFirebaseManager.getZipCodesByCityId("%${Application.prefs.loadData(R.string.sp_id_township, "")!!}%")
        val strings = mutableListOf<String>()
        postalCodesList.add(0, ZipCodes(0,"","Seleccionar...",Application.prefs.loadData(R.string.sp_id_township, "")!!,true))
        postalCodesList.forEach { strings.add(it.value) }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getColoniesAdapter(postalCode: String): ArrayAdapter<String> {
        coloniesList = CatalogsFirebaseManager.getColoniesByZipCode(if (postalCode.isBlank()) 0 else postalCode.toInt())
        val list = mutableListOf<String>()
        coloniesList.add(0, Colony(0, "", "Selecciona...", postalCode, true))
        coloniesList.forEach {
            list.add(it.value)
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    /*override fun getArticlesAdapter(): ArrayAdapter<String> {
        articlesList = CatalogsAdapterManager.getArticlesList()
        val strings = mutableListOf<String>()
        articlesList.forEach {
            if (it.id == 0L) {
                strings.add("Seleccionar..")
            } else {
                strings.add("Artículo ${it.article}")
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getFractionAdapter(positionArticle: Int): ArrayAdapter<String> {
        fractionList = CatalogsAdapterManager.getFractionsList(articlesList[positionArticle].id.toInt())
        val strings = mutableListOf<String>()
        fractionList.forEach {
            if (it.id == 0L) {
                strings.add("Seleccionar..")
            } else {
                strings.add("Fracción ${it.fraccion}")
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun saveDefaultMotivation(idArticle: Int, idFraction: Int, motivation: String) {
        Application.prefs.saveData(R.string.sp_default_article, idArticle)
        Application.prefs.saveData(R.string.sp_default_fraction, idFraction)
        Application.prefs.saveData(R.string.sp_default_motivation, motivation)
    }*/

    override fun saveDefaultDirection(zipCode: String, colony: String, street: String, street1: String, street2: String) {
        Application.prefs.saveData(R.string.sp_default_zip_code, zipCode)
        Application.prefs.saveData(R.string.sp_default_colony, colony)
        Application.prefs.saveData(R.string.sp_default_street, street)
        Application.prefs.saveData(R.string.sp_default_street1, street1)
        Application.prefs.saveData(R.string.sp_default_street2, street2)
        listener.onPreferencesSaved()
    }

    /*override fun getPositionArticle(): Int {
        val idArticle = Application.prefs?.loadDataInt(R.string.sp_default_article) ?: 0
        articlesList.forEachIndexed { index, articles ->
            if (articles.id == idArticle.toLong())
                return index
        }
        return 0
    }

    override fun getPositionFraction(): Int {
        val idFraction = Application.prefs?.loadDataInt(R.string.sp_default_fraction) ?: 0
        fractionList.forEachIndexed { index, infractionFraction ->
            if (infractionFraction.id == idFraction.toLong())
                return index
        }
        return 0
    }*/
}