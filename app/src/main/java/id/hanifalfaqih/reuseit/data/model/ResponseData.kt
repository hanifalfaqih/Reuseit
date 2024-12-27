package id.hanifalfaqih.reuseit.data.model


import com.google.gson.annotations.SerializedName

data class ResponseData<T>(
    @SerializedName("data")
    val data: List<T>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?
)