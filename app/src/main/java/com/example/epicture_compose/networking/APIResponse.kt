package com.example.epicture_compose.networking

import com.example.epicture_compose.data.model.Avatar
import com.example.epicture_compose.data.model.Comment
import com.example.epicture_compose.data.model.Score
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseAPIResponse (
    @SerializedName("success")
    val success:Boolean,
    @SerializedName("status")
    val status:Int
): Serializable

open class AvatarAPIResponse(
    success:Boolean,
    status:Int,
    @SerializedName("data")
    val data: Avatar
): BaseAPIResponse(success, status)

open class RefreshCredentialResponse(
    success: Boolean,
    status: Int,
    @SerializedName("access_token")
    val access_token: String,
    @SerializedName("refresh_token")
    val refresh_token: String,
    @SerializedName("expires_in")
    val expires_in: String
): BaseAPIResponse(success, status)

open class ImageListAPIResponse(
    success: Boolean,
    status: Int,
    @SerializedName("data")
    val data: List<Image>
): BaseAPIResponse(success, status)

open class ImageAPIResponse(
    success: Boolean,
    status: Int,
    @SerializedName("data")
    val data: Image
): BaseAPIResponse(success, status)

open class Image (
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("link")
    val url: String,
    @SerializedName("ups")
    var ups: Int,
    @SerializedName("downs")
    var downs: Int,
    @SerializedName("views")
    var views: Int,
    @SerializedName("comment_count")
    var commentNb: Int,
    @SerializedName("favourite_count")
    var favoriteNb: Int,
)