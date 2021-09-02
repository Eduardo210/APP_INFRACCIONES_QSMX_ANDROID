package mx.qsistemas.infracciones.net

import mx.qsistemas.infracciones.BuildConfig
import mx.qsistemas.infracciones.net.catalogs.CipherData
import mx.qsistemas.infracciones.net.catalogs.CipherDataResult
import mx.qsistemas.infracciones.net.request_web.*
import mx.qsistemas.infracciones.net.result_web.GenericResult
import mx.qsistemas.infracciones.net.result_web.InfractionResult
import mx.qsistemas.infracciones.net.result_web.LogInResult
import mx.qsistemas.infracciones.net.result_web.RecurrenceGeneric
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
// 8080 Tepoz
// 8000 San mateo
        var API_URL = "http://18.191.14.236:8080/"
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

    interface ApiService {
        //Para la migración de la app
        @POST("api/infringement/login/")
        fun login(@Body body: LogInRequest): Call<LogInResult>

        @POST("api/accounts/token-verify/")
        fun verifyToken(@Body body: ValidateTokenRequest): Call<GenericResult>

        @POST("api/infringement/add-infringement/")
        fun sendInfractionToServer(@Header("token") tokenSession: String, @Body body: InfractionRequest): Call<InfractionResult>

        @POST("api/infringement/save-payer/")
        fun savePaymentToServer(@Header("token") tokenSession: String, @Header(value = "idPayment") idPayment: Long, @Body body: SendPaymentRequest): Call<GenericResult>

        @GET("api/infringement/get-infringements/")
        fun searchInfraction(@Header("token") tokenSession: String, @Query("param") name: String): Call<SearchResult>

        @POST("api/infringement/order-payer/")
        fun updatePayer(@Header("Authorization") tokenSession: String, @Body body: PayerInfraction): Call<GenericResult>

        @GET("api/infringement/get-selected-infringement/")
        fun detailInfraction(@Header("token") tokenSession: String, @Query("param") token: String): Call<DetailResult>

        @GET("api/infringement/get-infringements-to-date/")
        fun saveRecurrences(@Header("token") tokenSession: String,
                            @QueryMap params: Map<String, String>):
                            Call<List<RecurrenceGeneric>>

    }
}