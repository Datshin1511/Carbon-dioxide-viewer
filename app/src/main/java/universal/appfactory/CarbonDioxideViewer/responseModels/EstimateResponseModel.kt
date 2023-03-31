package universal.appfactory.CarbonDioxideViewer.responseModels

import com.google.gson.annotations.SerializedName

data class EstimateResponseModel(
    @SerializedName("co2e")
    val co2e: Double,
    @SerializedName("co2e_unit")
    val co2e_unit: String,
    @SerializedName("co2e_calculation_method")
    val co2e_calculation_method: String,
    @SerializedName("co2e_calculation_origin")
    val co2e_calculation_origin: String,
    @SerializedName("emission_factor")
    val emission_factor: EmissionModel,
    @SerializedName("constituent_gases")
    val constituent_gases: ConstituentGasModel,
    @SerializedName("error")
    val error: String = "No errors",
    @SerializedName("message")
    val message: String = "Success"
)
