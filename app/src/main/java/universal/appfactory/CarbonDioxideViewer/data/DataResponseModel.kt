package universal.appfactory.CarbonDioxideViewer.data

import com.google.gson.annotations.SerializedName

data class DataResponseModel(
    @SerializedName("co2")
    val co2: ArrayList<Statistics>?
)
