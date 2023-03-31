package universal.appfactory.CarbonDioxideViewer.API

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    const val BASE_URL_1 = "https://global-warming.org"
    const val BASE_URL_2 = "https://beta3.api.climatiq.io/"
    const val API_KEY = "Bearer 5355YRMF5C4JSXMHQ60KPAS6RT2P"

    private val client = OkHttpClient.Builder().callTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS).build()

    private val retrofitURL1 = Retrofit.Builder()
        .baseUrl(BASE_URL_1)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val retrofitURL2 = Retrofit.Builder()
        .baseUrl(BASE_URL_2)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildServiceURL1(service: Class<T>): T{
        return retrofitURL1.create(service)
    }
    fun<T> buildServiceURL2(service: Class<T>): T{
        return retrofitURL2.create(service)
    }
}