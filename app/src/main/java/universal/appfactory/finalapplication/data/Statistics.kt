package universal.appfactory.finalapplication.data

import com.google.gson.annotations.SerializedName

data class Statistics(
    @SerializedName("year")
    val year: String? = "0000",
    @SerializedName("month")
    val month: String? = "00",
    @SerializedName("day")
    val day: String? = "00",
    @SerializedName("cycle")
    val cycle: String? = "0",
    @SerializedName("trend")
    val trend: String? = "0"
)
