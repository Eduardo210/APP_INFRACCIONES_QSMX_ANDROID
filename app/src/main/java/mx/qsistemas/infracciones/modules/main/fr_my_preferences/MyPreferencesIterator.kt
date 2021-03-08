package mx.qsistemas.infracciones.modules.main.fr_my_preferences

import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.Colony
import mx.qsistemas.infracciones.db_web.entities.firebase_replica.ZipCodes
import mx.qsistemas.infracciones.db_web.managers.CatalogsFirebaseManager
import mx.qsistemas.infracciones.net.catalogs.Articles
import mx.qsistemas.infracciones.net.catalogs.Fractions
import mx.qsistemas.infracciones.utils.FS_COL_ARTICLES
import mx.qsistemas.infracciones.utils.FS_COL_FRACTIONS
import mx.qsistemas.infracciones.utils.FS_COL_STATES

class MyPreferencesIterator(private val listener: MyPreferencesContracts.Presenter) : MyPreferencesContracts.Iterator {

    internal lateinit var postalCodesList: MutableList<ZipCodes>
    internal lateinit var coloniesList: MutableList<Colony>
    internal lateinit var articlesList: MutableList<Articles>
    internal lateinit var fractionList: MutableList<Fractions>

    /*override fun getPostalCodesAdapter(): ArrayAdapter<String> {
        postalCodesList = CatalogsFirebaseManager.getZipCodesByCityId("%${Application.prefs.loadData(R.string.sp_id_township, "")!!}%")
        val strings = mutableListOf<String>()
        postalCodesList.add(0, ZipCodes(0, "", "Selecciona...", Application.prefs.loadData(R.string.sp_id_township, "")!!, true))
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
    }*/

    override fun getArticlesAdapter() {
        articlesList = mutableListOf()
        val stateRef = Application.firestore.collection(FS_COL_STATES).document(Application.prefs.loadData(R.string.sp_id_state, "")!!)
        Application.firestore.collection(FS_COL_ARTICLES).whereArrayContains("states", stateRef).whereEqualTo("is_active", true)
                .orderBy("number").get().addOnSuccessListener { querySnapshot ->
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        querySnapshot.documents.forEach { document ->
                            val article = document.toObject(Articles::class.java)!!
                            article.documentReference = document.reference
                            articlesList.add(article)
                        }
                    }
                    val list = mutableListOf<String>()
                    articlesList.add(0, Articles("Selecciona...", "", true, null))
                    articlesList.forEach { article ->
                        if (article.documentReference == null) list.add("Selecciona...") else list.add("Artículo ${article.number}")
                    }
                    val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                    adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                    listener.onArticlesLoad(adapter)
                }.addOnFailureListener { exception ->
                    Log.e(Application.TAG, exception.toString())
                    listener.onError(Application.getContext().getString(R.string.e_firestore_not_available))
                }
    }

    override fun getFractionAdapter(articlePosition: Int) {
        fractionList = mutableListOf()
        val list = mutableListOf<String>()
        if (articlesList[articlePosition].documentReference != null) {
            Application.firestore.collection(FS_COL_FRACTIONS).whereEqualTo("reference", articlesList[articlePosition].documentReference)
                    .whereEqualTo("is_active", true).orderBy("number").get().addOnSuccessListener { querySnapshot ->
                        if (querySnapshot != null && !querySnapshot.isEmpty) {
                            querySnapshot.documents.forEach { document ->
                                val fraction = document.toObject(Fractions::class.java)!!
                                fraction.childReference = document.reference
                                fractionList.add(fraction)
                            }
                        }
                        fractionList.add(0, Fractions("Selecciona...", "", true, articlesList[articlePosition].documentReference))
                        fractionList.forEach { article ->
                            if (article.childReference == null) list.add("Selecciona...") else list.add("Fracción ${article.number}")
                        }
                        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
                        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                        listener.onFractionsLoad(adapter)
                    }.addOnFailureListener { exception ->
                        Log.e(Application.TAG, exception.toString())
                        listener.onError(Application.getContext().getString(R.string.e_firestore_not_available))
                    }
        } else {
            list.add("Selecciona...")
            val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, list)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            listener.onFractionsLoad(adapter)
        }
    }

    override fun saveDefaultMotivation(idArticle: String, idFraction: String, motivation: String) {
        Application.prefs.saveData(R.string.sp_default_article, idArticle)
        Application.prefs.saveData(R.string.sp_default_fraction, idFraction)
        Application.prefs.saveData(R.string.sp_default_motivation, motivation)
    }

    override fun saveDefaultDirection(colony: String, street: String, street1: String, street2: String) {
        //Application.prefs.saveData(R.string.sp_default_zip_code, zipCode)
        Application.prefs.saveData(R.string.sp_default_colony, colony)
        Application.prefs.saveData(R.string.sp_default_street, street)
        Application.prefs.saveData(R.string.sp_default_street1, street1)
        Application.prefs.saveData(R.string.sp_default_street2, street2)
        listener.onPreferencesSaved()
    }

    override fun getPositionArticle(): Int {
        val articleRef = Application.prefs.loadData(R.string.sp_default_article, "")!!
        articlesList.forEachIndexed { index, articles ->
            if (articles.documentReference?.id == articleRef)
                return index
        }
        return 0
    }

    override fun getPositionFraction(): Int {
        val idFraction = Application.prefs.loadData(R.string.sp_default_fraction, "")!!
        fractionList.forEachIndexed { index, fraction ->
            if (fraction.childReference?.id == idFraction)
                return index
        }
        return 0
    }

    /*override fun getPositionZipCode(): Int {
        val postalCodeKey = Application.prefs.loadData(R.string.sp_default_zip_code, "")!!
        postalCodesList.forEachIndexed { index, zipCode ->
            if (zipCode.key == postalCodeKey)
                return index
        }
        return 0
    }

    override fun getPositionColony(): Int {
        val colonyKey = Application.prefs.loadData(R.string.sp_default_colony, "")!!
        coloniesList.forEachIndexed { index, colony ->
            if (colony.key == colonyKey)
                return index
        }
        return 0
    }*/
}