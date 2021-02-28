package com.example.epicture_compose.ui.user

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.User
import com.example.epicture_compose.data.repository.user.UserLocalDataSource
import com.example.epicture_compose.data.repository.user.UserRemoteDataSource
import com.example.epicture_compose.data.repository.user.UserRepository
import com.example.epicture_compose.extensions.isValidExtension
import com.example.epicture_compose.utils.Publisher
import com.example.epicture_compose.utils.Scrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.net.HttpURLConnection
import java.net.URL

class UserViewModel: ViewModel() {

    // Inputs

    private val imageSubject: PublishSubject<Photo> = PublishSubject.create()
    private val feedItemSubject: PublishSubject<FeedItem> = PublishSubject.create()

    // Outputs
    private var _user = MutableLiveData(User("", ""))

    val user: LiveData<User> = _user
    private var _feedItems = MutableLiveData(mutableListOf<FeedItem>())
    val feedItems: LiveData<MutableList<FeedItem>> = _feedItems

    // Private

    private var scrapper = Scrapper()
    private val userPublisher = Publisher<User, Throwable>()
    private var userRepository: UserRepository = UserRepository(UserLocalDataSource, UserRemoteDataSource)

    // Methods

    @SuppressLint("CheckResult")
    private fun getUser() {
        this.userRepository.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(
                    { user ->
                        println("getUser: $user")
                        userPublisher.successSubject.onNext(user)
                    },
                    { e ->
                        e.printStackTrace()
                        userPublisher.errorSubject.onNext(e)
                    }
                )
    }

    @SuppressLint("CheckResult")
    private fun bindUser() {
        userPublisher.successSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            _user.value = it
                        },
                        { e ->
                            e.printStackTrace()
                        }
                )
    }

    @SuppressLint("CheckResult")
    private fun getUserImages() {
        userRepository.getImages()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(
                        { images ->
                            println("Received images: $images")
                            images.forEach { photo ->
                                if (photo.title == "title" && photo.description == "description") {
                                    return@subscribe
                                }
                                imageSubject.onNext(photo)
                            }
                        },
                        { e ->
                            e.printStackTrace()
                            imageSubject.onError(e)
                        }
                )
    }

    @SuppressLint("CheckResult")
    private fun bindUserImages() {
        this.imageSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(
                        { photo ->
                            println("Received photo: $photo")

                            val url = URL(photo.url)
                            val urlConnection = url.openConnection() as HttpURLConnection

                            var imageURL = photo.url
                            if (!photo.url.isValidExtension()) {
                                try {
                                    val text = urlConnection.inputStream.bufferedReader().readText()
                                    println("text html = $text")
                                    imageURL = scrapper.getImageURLFromGallery(text)
                                    println("imageURL: $imageURL")
                                } finally {
                                    urlConnection.disconnect()
                                }
                            }
                            val feedItem = FeedItem(
                                    user = null,
                                    photo = Photo(
                                            id = photo.id,
                                            title = photo.title,
                                            description = photo.description,
                                            url = imageURL,
                                            score = photo.score,
                                            views = photo.views,
                                            comments = listOf()
                                    )
                            )
                            feedItemSubject.onNext(feedItem)
                        },
                        { e ->
                            e.printStackTrace()
                        }
                )
    }

    @SuppressLint("CheckResult")
    fun bindFeedItem() {
        this.feedItemSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            val tempList= _feedItems.value
                            tempList?.add(it)
                            _feedItems.value = tempList
                        },
                        { e ->
                            e.printStackTrace()
                        }
                )
    }

    // Initializer

    init {
        this.bindUser()
        this.bindUserImages()
        this.bindFeedItem()
        this.getUser()
        this.getUserImages()
    }
}