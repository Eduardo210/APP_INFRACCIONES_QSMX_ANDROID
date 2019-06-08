package mx.qsistemas.payments_transfer.net

import mx.qsistemas.payments_transfer.BuildConfig
import mx.qsistemas.payments_transfer.dtos.CipherData_Request
import mx.qsistemas.payments_transfer.dtos.CipherData_Result
import mx.qsistemas.payments_transfer.dtos.GetKey_Request
import mx.qsistemas.payments_transfer.dtos.GetKey_Result
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class API_Quetzalcoatl {

    companion object {
        fun getQuetzalcoatlService(): GetQuetzalcoatlService {
            val customClient = OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
            /* Enable network logging only in debug mode */
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                customClient.addInterceptor(loggingInterceptor)
            }
            val clientBuilder = customClient.build()
            val builder = Retrofit.Builder().baseUrl("http://189.240.246.19/WS/")
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .client(clientBuilder).build()
            return builder.create(GetQuetzalcoatlService::class.java)
        }
    }

    interface GetQuetzalcoatlService {
        @Headers(*arrayOf("Content-Type: application/soap+xml", "Accept-Charset: utf-8"))
        @POST("AES_Android/Payworks.asmx?op=GetKey")
        fun getKey(@Body body: GetKey_Request): Call<GetKey_Result>

        @Headers(*arrayOf("Content-Type: application/soap+xml", "Accept-Charset: utf-8"))
        @POST("AES/Service.asmx?op=Encrypt_KeyStr")
        fun encryptData(@Body body: CipherData_Request): Call<CipherData_Result>
    }
}