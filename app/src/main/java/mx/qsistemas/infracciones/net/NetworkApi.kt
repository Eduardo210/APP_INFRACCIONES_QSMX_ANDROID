package mx.qsistemas.infracciones.net

import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.net.request_web.LogInRequest
import mx.qsistemas.infracciones.net.request_web.RequestInfraction
import mx.qsistemas.infracciones.net.result_web.LogInResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
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
        val builder = Retrofit.Builder().baseUrl("http://189.240.246.23:8000/")
                //.addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder).build()
        return builder.create(ApiService::class.java)
    }

    interface ApiService {
        //Para la migración de la app
        @POST("api/accounts/login/")
        fun login(@Body body: LogInRequest): Call<LogInResult>

        @POST("api/infringement/add/")
        fun sendInfractionToServer(@Body body: RequestInfraction): Call<String>

        @GET("api/infringement/search/")
        fun searchInfraction(@Query("name") name: String): Call<String>
    }
}