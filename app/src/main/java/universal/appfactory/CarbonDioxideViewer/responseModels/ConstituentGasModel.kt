package universal.appfactory.CarbonDioxideViewer.responseModels

import com.google.gson.annotations.SerializedName

data class ConstituentGasModel(
    @SerializedName("co2e_total")
    val co2e_total: Double,
    @SerializedName("co2e_other")
    val co2e_other: String,
    @SerializedName("co2")
    val co2: Double,
    @SerializedName("ch4")
    val ch4: Double,
    @SerializedName("n2o")
    val n2o: Double
)
