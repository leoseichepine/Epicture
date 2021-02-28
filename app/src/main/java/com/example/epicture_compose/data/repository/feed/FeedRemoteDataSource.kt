package com.example.epicture_compose.data.repository.feed

import com.example.epicture_compose.data.model.FeedItem
import io.reactivex.Observable

object FeedRemoteDataSource: FeedDataSource {
    override fun getItems(): Observable<List<FeedItem>> {
        TODO("Not yet implemented")
    }
}