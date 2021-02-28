package com.example.epicture_compose.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Avatar(
    @SerializedName("avatar")
    val url: String,
    var username: String
):Serializable