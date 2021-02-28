package com.example.epicture_compose.ui.favourite

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.Score
import com.example.epicture_compose.data.repository.user.UserLocalDataSource
import com.example.epicture_compose.data.repository.user.UserRemoteDataSource
import com.example.epicture_compose.data.repository.user.UserRepository
import com.example.epicture_compose.extensions.isValidExtension
import com.example.epicture_compose.networking.Image
import com.example.epicture_compose.networking.ImgurAPI
import com.example.epicture_compose.utils.Scrapper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import it.skrape.core.fetcher.BrowserFetcher
import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.extractIt
import it.skrape.selects.eachHref
import it.skrape.selects.html5.a
import it.skrape.skrape
import java.net.HttpURLConnection
import java.net.URL

/**
 * ViewModel of FavouriteViewModel to update its view.
 */
class FavouriteViewModel: ViewModel() {

    // Inputs

    /**
     * Subject to track favourite events
     */
    private val favouriteSubject: PublishSubject<Photo> = PublishSubject.create()
    private val feedItemSubject: PublishSubject<FeedItem> = PublishSubject.create()

    // Outputs

    /**
     * Holds feed items (posts).
     * Once assigned, the view is updated.
     */
    private var _feedItems = MutableLiveData(mutableListOf<FeedItem>())
    val feedItems: LiveData<MutableList<FeedItem>> = _feedItems

    // Private

    /**
     * Class to scrap the html content of the Imgur gallery.
     */
    private var scrapper = Scrapper()

    /**
     * User repository to get remote or local data
     */
    private var userRepository: UserRepository = UserRepository(UserLocalDataSource, UserRemoteDataSource)

    // Methods

    /**
     * Function get user's favourites
     */
    @SuppressLint("CheckResult")
    private fun getFavourites() {
        userRepository.getFavouritePhotos()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(
                        { images ->
                            println("Received images: $images")
                            images.forEach { favouriteSubject.onNext(it) }
                        },
                        { e ->
                            e.printStackTrace()
                            favouriteSubject.onError(e)
                        }
                )
    }

    /**
     * Function to bind user's favourite.
     * Stream of feed item.
     */
    @SuppressLint("CheckResult")
    private fun bindFavourite() {
        this.favouriteSubject
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

    /**
     * Function to bind the feed item and push it to the view.
     */
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
        this.bindFavourite()
        this.bindFeedItem()
        this.getFavourites()
    }
}
