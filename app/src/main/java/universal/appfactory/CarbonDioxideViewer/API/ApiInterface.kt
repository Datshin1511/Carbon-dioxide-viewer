package universal.appfactory.CarbonDioxideViewer.API

import retrofit2.Call
import retrofit2.http.*
import universal.appfactory.CarbonDioxideViewer.data.DataResponseModel

interface ApiInterface {

    @GET("api/co2-api")
    fun getData() : Call<DataResponseModel>?

}