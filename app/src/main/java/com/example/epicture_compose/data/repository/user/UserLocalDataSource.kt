package com.example.epicture_compose.data.repository.user

import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.User
import io.reactivex.Observable

object UserLocalDataSource: UserDataSource {
    override fun getUser(): Observable<User> {
        TODO("Not yet implemented")
    }

    override fun getFavouritePhotos(): Observable<List<Photo>> {
        TODO("Not yet implemented")
    }

    override fun getImages(): Observable<List<Photo>> {
        TODO("Not yet implemented")
    }
}