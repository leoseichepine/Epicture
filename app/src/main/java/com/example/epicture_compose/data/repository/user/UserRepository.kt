package com.example.epicture_compose.data.repository.user

import android.content.ContentValues.TAG
import android.util.Log
import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.User
import io.reactivex.Observable

class UserRepository(
        private val userLocalDataSource: UserLocalDataSource,
        private val userRemoteDataSource: UserRemoteDataSource): UserDataSource  {

    override fun getUser(): Observable<User> {
        return userRemoteDataSource.getUser()
    }

    override fun getFavouritePhotos(): Observable<List<Photo>> {
        return userRemoteDataSource.getFavouritePhotos()
    }

    override fun getImages(): Observable<List<Photo>> {
        return userRemoteDataSource.getImages()
    }

    companion object {

        private var INSTANCE: UserRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         *
         * @param userLocalDataSource the local data source
         * @param userRemoteDataSource the remote data source
         * @return the [UserRepository] instance
         */
        @JvmStatic
        fun getInstance(userLocalDataSource: UserLocalDataSource,
                        userRemoteDataSource: UserRemoteDataSource) =
                INSTANCE ?: synchronized(UserRepository::class.java) {
                    INSTANCE
                            ?: UserRepository(userLocalDataSource, userRemoteDataSource)
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