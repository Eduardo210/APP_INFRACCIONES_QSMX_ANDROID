package mx.qsistemas.infracciones.net

import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

open class NetworkApi {

    fun getNetworkService(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val customClient = OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
        /* Enable interceptor only in debug mode */
        if (BuildConfig.DEBUG) {
            customClient.addInterceptor(interceptor)
        }
        val clientBuilder = customClient.build()
        val builder = Retrofit.Builder().baseUrl(Application.remoteConfig?.getString(Application.getContext().getString(R.string.rc_ip_address)))
                .addConverterFactory(SimpleXmlConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder).build()
        return builder.create(ApiService::class.java)
    }

    interface ApiService {
        @GET("ws/mobile/qsistemas/LoginInicial.asmx/Envio_Catalogos_Infracciones")
        fun downloadCatalogs(@Query("FechaSincronizacion") date: String): Call<String>

        @GET("ws/mobile/qsistemas/LoginInicial.asmx/Envio_Resultados_Previos_Busqueda_Infracciones_Mod")
        fun doSearchByFilter(@Query("Json") json: String): Call<String>

        @GET("ws/mobile/qsistemas/LoginInicial.asmx/Envio_Resultado_Busqueda_Infraccion")
        fun doSearchByIdInfraction(@Query("Json") json:String): Call<String>

        @POST("ws/mobile/qsistemas/logininicial.asmx/Receptor_Infracciones_Moviles")
        fun sendInfractionToServer(@Query("Json") json : String): Call<String>

        @GET("ws/mobile/qsistemas/logininicial.asmx/UpdatePerson")
        fun updatePerson(@Query("Json") json : String): Call<String>
    }
}