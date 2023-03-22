package universal.appfactory.finalapplication.API

import retrofit2.Call
import retrofit2.http.*
import universal.appfactory.finalapplication.API.ApiClient.API_KEY
import universal.appfactory.finalapplication.data.DataResponseModel

interface ApiInterface {

    @GET("api/co2-api")
    fun getData() : Call<DataResponseModel>?

}