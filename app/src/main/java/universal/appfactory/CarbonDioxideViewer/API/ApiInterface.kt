package universal.appfactory.CarbonDioxideViewer.API

import retrofit2.Call
import retrofit2.http.*
import universal.appfactory.CarbonDioxideViewer.API.ServiceBuilder.API_KEY
import universal.appfactory.CarbonDioxideViewer.responseModels.DataResponseModel
import universal.appfactory.CarbonDioxideViewer.responseModels.EstimateResponseModel

interface ApiInterface {

    @GET("api/co2-api")
    fun getData() : Call<DataResponseModel>?

    @POST("estimate")
    fun getEstimate(@Header("Authorization") authorization: String = API_KEY, @Body body: String): Call<EstimateResponseModel>?

}