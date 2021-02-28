package com.example.epicture_compose.data.model

import java.net.URL
import java.time.LocalDate
import java.util.*

data class Score (
    var up: Int,
    var down: Int
)

data class Comment (
    val user: User,
    var like: Int,
    val comment: String,
    val date: LocalDate
)

data class Photo (
    val id: String,
    val title: String?,
    val description: String?,
    val url: String,
    var score: Score,
    var views: Int,
    var comments: List<Comment>
)