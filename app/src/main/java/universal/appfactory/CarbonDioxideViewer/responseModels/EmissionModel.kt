package universal.appfactory.CarbonDioxideViewer.responseModels

import com.google.gson.annotations.SerializedName

data class EmissionModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("activity_id")
    val activity_id: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("access_type")
    val access_type: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("source_dataset")
    val source_dataset: String,
    @SerializedName("year")
    val year: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("lca_activity")
    val lca_activity: String,
    @SerializedName("data_quality_flags")
    val data_quality_flags: ArrayList<*>
)
