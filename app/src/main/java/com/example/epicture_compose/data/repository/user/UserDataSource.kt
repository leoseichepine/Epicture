package com.example.epicture_compose.data.repository.user

import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.User
import io.reactivex.Observable

interface UserDataSource {
    fun getUser(): Observable<User>
    fun getFavouritePhotos(): Observable<List<Photo>>
    fun getImages(): Observable<List<Photo>>
}