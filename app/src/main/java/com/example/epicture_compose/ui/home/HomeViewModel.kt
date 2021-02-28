package com.example.epicture_compose.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.Score
import com.example.epicture_compose.extensions.isValidExtension
import com.example.epicture_compose.networking.ImgurAPI
import com.example.epicture_compose.utils.Publisher
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * ViewModel of HomeViewModel to update its view.
 */
class HomeViewModel: ViewModel() {
    /**
     * Holds feed items (posts).
     * Once assigned, the view is updated.
     */
    private var _feedItems = MutableLiveData(listOf<FeedItem>())
    val feedItems: LiveData<List<FeedItem>> = _feedItems

    /**
     * Publisher to track feed items events (success, error, loading)
     */
    val feedItemsPublisher = Publisher<List<FeedItem>, Throwable>()

    /**
     * Function to get feed items from the ImgurAPI gallery
     */
    fun getFeedItems(section: String? = "hot", sort: String? = "viral", page: String? = "0", window: String? = "day",
                     showViral: String? = "true", showMature: String? = "false", albumPreviews: String? = "false") {
        this.feedItemsPublisher.loaderSubject.onNext(true)

        ImgurAPI.fetchGallery(
                section = section,
                sort = sort,
                page = page,
                window = window,
                showViral  = showViral,
                showMature = showMature,
                albumPreviews = albumPreviews
        )
                .subscribe(
                        { images ->
                            val filteredImages = images.filter { it.url.isValidExtension() }
                            val feedItems = filteredImages.map {
                                FeedItem(
                                        user = null,
                                        photo = Photo(
                                                id = it.id,
                                                title = it.title,
                                                description = it.description,
                                                url = it.url,
                                                score = Score(it.ups, it.downs),
                                                views = it.views,
                                                comments = listOf()
                                        )
                                )
                            }
                            this.feedItemsPublisher.successSubject.onNext(feedItems)
                        },
                        {
                            this.feedItemsPublisher.errorSubject.onNext(it)
                            Log.e(TAG, "Cannot fetch the gallery", it)
                        }
                )
    }

    /**
     * Function to bind the feed items and push them to the view.
     */
    @SuppressLint("CheckResult")
    private fun bindFeedItems() {
        feedItemsPublisher.successSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    this._feedItems.value = it
                }
    }

    /**
     * Function to filter viral images
     */
    fun showViral(status: Boolean) {
        for (i in 0..3) {
            this.getFeedItems(
                    page = i.toString(),
                    showViral = status.toString()
            )
        }
    }

    /**
     * Function to filter mature images
     */
    fun showMature(status: Boolean) {
        for (i in 0..3) {
            this.getFeedItems(
                    page = i.toString(),
                    showMature = "true"
            )
        }
    }

    init {
        for (i in 0..3) {
            this.getFeedItems(page = i.toString())
        }
        this.bindFeedItems()
    }
}