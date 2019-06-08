package mx.qsistemas.payments_transfer.net

import mx.qsistemas.payments_transfer.BuildConfig
import mx.qsistemas.payments_transfer.dtos.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class API_Banxico {

    companion object {
        fun getBanxicoService(): GetBanxicoInterface {
            val customClient = OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
            /* Enable network logging only in debug mode */
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                customClient.addInterceptor(loggingInterceptor)
            }
            val clientBuilder = customClient.build()
            val builder = Retrofit.Builder().baseUrl("https://www.banxico.org.mx/pagospei-beta/")
                    .addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder).build()
            return builder.create(GetBanxicoInterface::class.java)
        }
    }

    interface GetBanxicoInterface {
        @POST("registroInicial")
        fun getRegistroInicial(@Body d: RequestBody): Call<RegistroInicial_Result>

        @POST("registroSubsecuente")
        fun getRegistroDispositivo(@Body d: RequestBody): Call<RegistroDispositivo_Result>

        @POST("registraAppPorOmision")
        fun getRegistroDispositivoPorOmision(@Body d: RequestBody): Call<RegistroDispositivoPorOmision_Result>

        @POST("bajaDispositivos")
        fun getBajaDispotivo(@Body request: RequestBody): Call<BajaDispositivos_Result>

        @POST("consulta")
        fun getConsultaMensajeDeCobro(@Body d: RequestBody): Call<Consulta_Result>

        @POST("solicitaClaveDescifradoMC")
        fun getClaveDescifrado(@Body d: RequestBody): Call<SolicitudClaveDescifrado_Result>

        @POST("validacionCuenta")
        fun getValidacionCuentasBeneficiarias(@Body d: RequestBody): Call<ValidacionCuentasBeneficiarias_Result>

        @POST("consultaValidacionCuenta")
        fun getConsultaValidacionCuentasBeneficiarias(@Body d: RequestBody): Call<ConsultaValidacionCuentasBeneficiarias_Result>
    }
}