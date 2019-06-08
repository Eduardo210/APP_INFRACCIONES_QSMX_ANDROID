package mx.qsistemas.payments_transfer.net

import mx.qsistemas.payments_transfer.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class API_Banorte {

    companion object {
        fun getBanorteService(): GetBanorteInterface {
            val customClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
            /* Enable network logging only in debug mode */
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                customClient.addInterceptor(loggingInterceptor)
            }
            val clientBuilder = customClient.build()
            val builder = Retrofit.Builder().baseUrl("https://via.banorte.com/InterredesSeguro/")
                    .addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder).build()
            return builder.create(GetBanorteInterface::class.java)
        }
    }

    interface GetBanorteInterface {
        @FormUrlEncoded
        @POST("./")
        fun getCipherKey(@FieldMap(encoded = true) headers: Map<String, String>): Call<Void>

        @FormUrlEncoded
        @POST("./")
        fun processTx(@FieldMap(encoded = true) headers: Map<String, String>): Call<Void>
    }
}