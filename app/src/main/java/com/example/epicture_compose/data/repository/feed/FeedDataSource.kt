package com.example.epicture_compose.data.repository.feed

import com.example.epicture_compose.data.model.FeedItem
import io.reactivex.Observable

interface FeedDataSource {
    fun getItems(): Observable<List<FeedItem>>
}