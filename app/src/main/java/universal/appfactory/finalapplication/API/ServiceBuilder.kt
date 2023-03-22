package universal.appfactory.finalapplication.API

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    private val client = OkHttpClient.Builder().callTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS).build()

    private val retrofit1 = Retrofit.Builder()
        .baseUrl("https://global-warming.org") // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val retrofit2 = Retrofit.Builder()
        .baseUrl("https://beta3.api.climatiq.io") // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService1(service: Class<T>): T{
        return retrofit1.create(service)
    }

    fun<T> buildService2(service: Class<T>): T{
        return retrofit2.create(service)
    }

}