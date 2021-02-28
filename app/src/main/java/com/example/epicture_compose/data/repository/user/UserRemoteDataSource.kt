package com.example.epicture_compose.data.repository.user

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.Score
import com.example.epicture_compose.data.model.User
import com.example.epicture_compose.networking.ImgurAPI
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

object UserRemoteDataSource: UserDataSource {
    override fun getUser(): Observable<User> {
        return Observable.create { emitter ->
            ImgurAPI.fetchAvatar()
                    .map { avatar ->
                        User(
                                name = avatar.username,
                                pictureURL = avatar.url
                        )
                    }
                    .subscribe({ emitter.onNext(it) }, { emitter.onError(it) })
        }
    }

    override fun getFavouritePhotos(): Observable<List<Photo>> {
        return Observable.create { emitter ->
            ImgurAPI.fetchUserFavorites()
                    .map { images ->
                        images.map { Photo(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                url = it.url,
                                score = Score(it.ups, it.downs),
                                views = it.views,
                                comments = listOf()
                        ) }
                    }
                    .subscribe({ emitter.onNext(it) }, { emitter.onError(it) })
        }
    }

    override fun getImages(): Observable<List<Photo>> {
        return Observable.create { emitter->
            ImgurAPI.fetchUserImages()
                    .map { images->
                        images.map { Photo(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                url = it.url,
                                score = Score(it.ups, it.downs),
                                views = it.views,
                                comments = listOf()
                        ) }
                    }
                    .subscribe({ emitter.onNext(it) }, { emitter.onError(it) })
        }
    }
}