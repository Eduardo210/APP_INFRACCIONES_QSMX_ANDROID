package mx.qsistemas.infracciones.modules.login

import com.google.gson.Gson
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.alarm.Alarms
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.catalogs.DownloadCatalogs
import mx.qsistemas.infracciones.utils.FS_COL_TERMINALS
import mx.qsistemas.infracciones.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*

class LogInIterator(private val listener: LogInContracts.Presenter) : LogInContracts.Iterator {

    override fun registerAlarm() {
        Alarms()
    }

    override fun downloadCatalogs() {
        val lastSynch = Application.prefs?.loadData(R.string.sp_last_synch, "01/01/2000")!!
        NetworkApi().getNetworkService().downloadCatalogs(lastSynch).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val data = Gson().fromJson(response.body(), DownloadCatalogs::class.java)
                    processCatalogs(data)
                    Application.prefs?.saveData(R.string.sp_last_synch, SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date()))
                    val imei = Utils.getImeiDevice(Application.getContext())
                    Application.firestore?.collection(FS_COL_TERMINALS)?.document(imei)?.update("last_synch", Date())
                    listener.onCatalogsDownloaded()
                } else {
                    listener.onError(Application.getContext().getString(R.string.e_other_problem_internet))
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                listener.onError(t.message ?: "")
            }
        })
    }

    override fun login(user: String, psd: String) {

        Application.prefs?.saveDataBool(R.string.sp_has_session, true)
        listener.onLoginSuccessful()
    }

    private fun processCatalogs(data: DownloadCatalogs){

    }
}