package com.example.epicture_compose.data.repository.feed

import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.data.repository.user.UserLocalDataSource
import com.example.epicture_compose.data.repository.user.UserRemoteDataSource
import com.example.epicture_compose.data.repository.user.UserRepository
import io.reactivex.Observable

class FeedRepository(
        private val feedLocalDataSource: FeedLocalDataSource,
        private val feedRemoteDataSource: FeedRemoteDataSource): FeedDataSource {

    override fun getItems(): Observable<List<FeedItem>> {
        TODO("Not yet implemented")
    }

    companion object {

        private var INSTANCE: FeedRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         *
         * @param feedLocalDataSource the local data source
         * @param feedRemoteDataSource the remote data source
         * @return the [FeedRepository] instance
         */
        @JvmStatic
        fun getInstance(feedLocalDataSource: FeedLocalDataSource,
                        feedRemoteDataSource: FeedRemoteDataSource) =
                INSTANCE ?: synchronized(FeedRepository::class.java) {
                    INSTANCE
                            ?: FeedRepository(feedLocalDataSource, feedRemoteDataSource)
                                    .also { INSTANCE = it }
                }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }

    }

}