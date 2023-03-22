@file:Suppress("MemberVisibilityCanBePrivate")

package universal.appfactory.CarbonDioxideViewer.API

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL_1 = "https://global-warming.org/"
    private const val BASE_URL_2 = "https://beta3.api.climatiq.io/"
    const val API_KEY = "5355YRMF5C4JSXMHQ60KPAS6RT2P"

    var okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun getInstance1(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL_1).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            // we need to add converter factory to
            // convert JSON object to Java object

    }

    fun getInstance2(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL_2).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
        // we need to add converter factory to
        // convert JSON object to Java object

    }

}