package mx.qsistemas.infracciones.modules.main

import android.util.Log
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.net.NetworkApi
import mx.qsistemas.infracciones.net.request_web.ValidateTokenRequest
import mx.qsistemas.infracciones.net.result_web.GenericResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection


class MainIterator(private val presenter: MainContracts.Presenter) : MainContracts.Iterator {

    override fun validateSession() {
        val request = ValidateTokenRequest(Application.prefs?.loadData(R.string.sp_access_token, "")!!)
        NetworkApi().getNetworkService().verifyToken(request).enqueue(object : Callback<GenericResult> {
            override fun onResponse(call: Call<GenericResult>, response: Response<GenericResult>) {
                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Application.prefs?.saveData(R.string.sp_access_token, "")
                    Application.prefs?.saveDataInt(R.string.sp_id_officer, 0)
                    Application.prefs?.saveData(R.string.sp_person_name, "")
                    Application.prefs?.saveData(R.string.sp_person_f_last_name, "")
                    Application.prefs?.saveData(R.string.sp_person_m_last_name, "")
                    Application.prefs?.saveData(R.string.sp_person_photo_url, "")
                    Application.prefs?.saveDataBool(R.string.sp_has_session, false)
                    presenter.onSessionClosed()
                }
                Log.e(this.javaClass.simpleName, response.code().toString())
            }

            override fun onFailure(call: Call<GenericResult>, t: Throwable) {
                Log.e(this.javaClass.simpleName, t.message)
            }
        })
    }
}