package id.hanifalfaqih.reuseit.data.model


import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String?,
    @SerializedName("header")
    val header: String?,
    @SerializedName("image_tumbnail")
    val imageThumbnail: String?,
    @SerializedName("image_content")
    val imageContent: String?,
    @SerializedName("link_video")
    val linkVideo: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("views")
    val views: Int,
    @SerializedName("type_content")
    val typeContent: String?,
    @SerializedName("type_trash")
    val typeTrash: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_name")
    val userName: String?
)