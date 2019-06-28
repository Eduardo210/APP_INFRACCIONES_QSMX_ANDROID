package mx.qsistemas.infracciones.modules.search

import android.util.Log
import android.widget.ArrayAdapter
import com.google.gson.Gson
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.IdentifierDocument
import mx.qsistemas.infracciones.db.managers.CatalogsAdapterManager
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import mx.qsistemas.infracciones.net.catalogs.InfractionSearch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import kotlin.collections.ArrayList


class SearchIterator(private val listener: SearchContracts.Presenter) : SearchContracts.Iterator {
    internal lateinit var identifierDocList: MutableList<IdentifierDocument>


    override fun getDocIdentAdapter(): ArrayAdapter<NewIdentDocument> {
        identifierDocList = CatalogsAdapterManager.getIdentifierDocList()
        val newFilterDocList: MutableList<NewIdentDocument> = ArrayList()

        identifierDocList.forEach { document ->
            val newDoc = NewIdentDocument()

            if (document.id == 0) {
                newDoc.id = document.id
                newDoc.value = "Seleccionar ..."

            } else {

                newDoc.id = document.id
                newDoc.value = document.document
            }
            newFilterDocList.add(newDoc)

        }
        val adapter = ArrayAdapter(Application.getContext(), R.layout.custom_spinner_item, newFilterDocList)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        return adapter
    }

    override fun doSearchByFilter(id: Int, filter: String) {
        val rootObject = JSONObject()
        rootObject.put("username", "InfraMobile")
        rootObject.put("password", "CF2E3EF25C90EB567243ADFACD4AA868")
        if (id == 0) {
            rootObject.put("Folio", filter)
            rootObject.put("IdDocumentoIdentificador", 0)
            rootObject.put("NumeroDocumentoIdentificador", "")
        } else {
            rootObject.put("Folio", "")
            rootObject.put("IdDocumentoIdentificador", id)
            rootObject.put("NumeroDocumentoIdentificador", filter)
        }
        Log.d("JSON-SEARCH", rootObject.toString())
        NetworkApi().getNetworkService().doSearchByFilter(rootObject.toString()).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = Gson().fromJson(response.body(), InfractionList::class.java)
                    if (data.flag) {
                        listener.onResultSearch(data.results)
                    } else {
                        listener.onError(data.message)
                    }
                    Log.d("SEARCH ---->>>>>", data.toString())

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("SEARCH ---->>>>>", t.message.toString())
                listener.onError(t.message ?: "")
            }

        })
    }

    override fun doSearchByIdInfraction(id: String) {
        val rootObject = JSONObject()
        rootObject.put("IdInfraccion", id)
        rootObject.put("username", "InfraMobile")
        rootObject.put("password", "CF2E3EF25C90EB567243ADFACD4AA868")
        Log.d("JSON-SEARCH", rootObject.toString())
        NetworkApi().getNetworkService().doSearchByIdInfraction(rootObject.toString()).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = Gson().fromJson(response.body(), InfractionSearch::class.java)
                    listener.onResultInfractionById(data)
                    Log.d("SEARCH_BY_ID", data.toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("SEARCH_BY_ID", t.message.toString())
                listener.onError(t.message ?: "")
            }
        })
    }
}
class NewIdentDocument {
    var id: Int = 0
    var value: String = ""

    override fun toString(): String {
        return value
    }
}
