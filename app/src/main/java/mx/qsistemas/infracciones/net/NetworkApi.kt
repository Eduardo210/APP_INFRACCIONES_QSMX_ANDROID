package mx.qsistemas.infracciones.net

import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.net.request_web.*
import mx.qsistemas.infracciones.net.result_web.GenericResult
import mx.qsistemas.infracciones.net.result_web.InfractionResult
import mx.qsistemas.infracciones.net.result_web.LogInResult
import mx.qsistemas.infracciones.net.result_web.detail_result.DetailResult
import mx.qsistemas.infracciones.net.result_web.search_result.SearchResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
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

        @POST("api/accounts/token-verify/")
        fun verifyToken(@Body body: ValidateTokenRequest): Call<GenericResult>

        @POST("api/infringement/add/")
        fun sendInfractionToServer(@Header("Authorization") tokenSession: String, @Body body: InfractionRequest): Call<InfractionResult>

        @POST("api/infringement/add-payment-order/")
        fun savePaymentToServer(@Header("Authorization") tokenSession: String, @Header(value = "idPayment") idPayment: Long, @Body body: PaymentRequest): Call<GenericResult>

        @GET("api/infringement/search/")
        fun searchInfraction(@Header("Authorization") tokenSession: String, @Query("name") name: String): Call<SearchResult>

        @POST("api/infringement/update-driver/")
        fun updateDriver(@Header("Authorization") tokenSession: String, @Body body: DriverRequest): Call<GenericResult>

        @POST("api/infringement/detail/")
        fun detailInfraction(@Header("Authorization") tokenSession: String, @Query("token") token: String): Call<DetailResult>
    }
}