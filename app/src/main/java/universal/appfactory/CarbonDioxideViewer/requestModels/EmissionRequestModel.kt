package universal.appfactory.CarbonDioxideViewer.requestModels

data class EmissionRequestModel(
    val emission_factor: EmissionFactors,
    val parameters: EmissionParameters
)
