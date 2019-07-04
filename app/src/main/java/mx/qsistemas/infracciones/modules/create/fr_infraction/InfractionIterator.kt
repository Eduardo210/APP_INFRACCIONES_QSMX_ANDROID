package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.location.Geocoder
import android.widget.ArrayAdapter
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.Articles
import mx.qsistemas.infracciones.db.entities.Disposition
import mx.qsistemas.infracciones.db.entities.InfractionFraction
import mx.qsistemas.infracciones.db.entities.RetainedDocument
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.singletons.SingletonInfraction
import mx.qsistemas.infracciones.utils.Validator
import java.util.*

class InfractionIterator(val listener: InfractionContracts.Presenter) : InfractionContracts.Iterator {
    internal lateinit var articlesList: MutableList<Articles>
    internal lateinit var fractionList: MutableList<InfractionFraction>
    internal lateinit var retainedDocList: MutableList<RetainedDocument>
    internal lateinit var dispositionList: MutableList<Disposition>

    override fun getArticlesAdapter(): ArrayAdapter<String> {
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

    override fun getRetainedDocAdapter(): ArrayAdapter<String> {
        retainedDocList = CatalogsAdapterManager.getRetainedDocList()
        val strings = mutableListOf<String>()
        retainedDocList.forEach {
            if (it.id == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.document)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun getDispositionAdapter(): ArrayAdapter<String> {
        dispositionList = CatalogsAdapterManager.getDispositionList()
        val strings = mutableListOf<String>()
        dispositionList.forEach {
            if (it.id == 0) {
                strings.add("Seleccionar..")
            } else {
                strings.add(it.disposition)
            }
        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, strings)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun saveNewArticle(posArticle: Int, posFraction: Int) {
        SingletonInfraction.motivationList.add(0, SingletonInfraction.DtoMotivation(articlesList[posArticle],
                fractionList[posFraction], ""))
    }

    override fun getPositionRetainedDoc(obj: RetainedDocument): Int {
        for (i in 0 until retainedDocList.size) {
            if (retainedDocList[i].id == obj.id) {
                return i
            }
        }
        return 0
    }

    override fun getPositionDisposition(obj: Disposition): Int {
        for (i in 0 until dispositionList.size) {
            if (dispositionList[i].id == obj.id) {
                return i
            }
        }
        return 0
    }

    override fun getAddressFromCoordinates(lat: Double, lon: Double) {
        if (Validator.isNetworkEnable(Application.getContext())) {
            val geocoder = Geocoder(Application.getContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses.isNotEmpty()) {
                listener.onAddressLocated(addresses[0].subLocality, addresses[0].subThoroughfare, addresses[0].locality, "")
            }
        }
    }
}