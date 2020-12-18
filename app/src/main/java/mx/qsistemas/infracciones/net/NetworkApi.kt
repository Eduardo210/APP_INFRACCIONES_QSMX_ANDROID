package mx.qsistemas.infracciones.net

import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.net.catalogs.CipherData
import mx.qsistemas.infracciones.net.catalogs.CipherDataResult
import mx.qsistemas.infracciones.net.request_web.*
import mx.qsistemas.infracciones.net.result_web.GenericResult
import mx.qsistemas.infracciones.net.result_web.InfractionResult
import mx.qsistemas.infracciones.net.result_web.LogInResult
import mx.qsistemas.infracciones.net.result_web.detail_result.DetailResult
import mx.qsistemas.infracciones.net.result_web.search_result.SearchResult
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

open class NetworkApi {

    companion object {
        var API_URL = "http://3.15.191.119:8000/"
        var HONOS_API_URL = "https://us-central1-honos-7f224.cloudfunctions.net/"

        private val interceptor = HttpLoggingInterceptor()
    }

    fun getNetworkService(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val customClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
        /* Enable interceptor only in debug mode */
        if (BuildConfig.DEBUG) {
            customClient.addInterceptor(interceptor)
        }
        val certificatePinner = CertificatePinner.Builder()
                .add("qs-testing.mx", "sha256/NCg85dKcDRYFTaggjrztZrYWlVV1NRK9eA03FBA91G4=")
                .add("qs-testing.mx", "sha256/48hXNwn3laJAzsrIBprOcewUb097BGNL7e+MVM7Rcis=")
                .add("qs-testing.mx", "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=")
                .build()
        val clientBuilder = customClient/*.certificatePinner(certificatePinner)*/.build()
        val builder = Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder).build()
        return builder.create(ApiService::class.java)
    }

    fun getHonosService(): GetHonosApiService {
        val customClient = OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
        /* Enable interceptor only in debug mode */
        if (BuildConfig.DEBUG) {
            customClient.addInterceptor(interceptor)
        }
        val clientBuilder = customClient.build()
        val builder = Retrofit.Builder().baseUrl(HONOS_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(clientBuilder).build()
        return builder.create(GetHonosApiService::class.java)
    }

    interface ApiService {
        //Para la migración de la app
        @POST("api/infringement/login/")
        fun login(@Body body: LogInRequest): Call<LogInResult>

        @POST("api/accounts/token-verify/")
        fun verifyToken(@Body body: ValidateTokenRequest): Call<GenericResult>

        @POST("api/infringement/add/")
        fun sendInfractionToServer(@Header("Authorization") tokenSession: String, @Body body: InfractionRequest): Call<InfractionResult>

        @POST("api/infringement/add-payment-order/")
        fun savePaymentToServer(@Header("Authorization") tokenSession: String, @Header(value = "idPayment") idPayment: Long, @Body body: PaymentRequest): Call<GenericResult>

        @GET("api/infringement/search/")
        fun searchInfraction(@Header("Authorization") tokenSession: String, @Query("name") name: String): Call<SearchResult>

        @POST("api/infringement/order-payer/")
        fun updatePayer(@Header("Authorization") tokenSession: String, @Body body: DriverRequest): Call<GenericResult>

        @POST("api/infringement/detail/")
        fun detailInfraction(@Header("Authorization") tokenSession: String, @Body token: HashMap<String,String>): Call<DetailResult>
    }

    interface GetHonosApiService {
        @POST("cipherData")
        fun cipherData(@Body body: CipherData): Call<CipherDataResult>
    }
}