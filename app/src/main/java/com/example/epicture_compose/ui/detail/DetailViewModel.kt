package com.example.epicture_compose.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.epicture_compose.data.model.Comment
import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.User
import com.example.epicture_compose.networking.ImgurAPI
import io.reactivex.subjects.PublishSubject
import java.time.LocalDate

/**
 * ViewModel of DetailView to update its view.
 */
class DetailViewModel: ViewModel() {

    // Inputs

    // Outputs

    /**
     * Holds comments.
     * Once assigned, the view is updated.
     */
    private var _comments = MutableLiveData(mutableListOf<Comment>())
    val comments: LiveData<MutableList<Comment>> = _comments

    // Private

    // Methods

    private fun fetchComments() {
    }

    private fun bindComments() {

    }

    /**
     * Function to vote up a post.
     *
     * @param imageID The image ID.
     */
    fun voteUp(imageID: String) {
        ImgurAPI.voteImage(imageID, "up")
    }

    /**
     * Function to vote down a post.
     *
     * @param imageID The image ID.
     */
    fun voteDown(imageID: String) {
        ImgurAPI.voteImage(imageID, "down")
    }

    /**
     * Function to post a comment.
     *
     * @param imageID The image ID.
     * @param text The comment to post.
     */
    fun postComment(imageID: String, text: String) {
        comments.value?.add(Comment(
                user = User(
                        name = "",
                        pictureURL = ""
                ),
                like = 1,
                comment = text,
                date = LocalDate.now()
        ))

        ImgurAPI.postComment(imageID, text)
    }

    /**
     * Function to mark an image as favourite.
     *
     * @param imageID The image ID.
     */
    fun markAsFavourite(imageID: String) {
        ImgurAPI.addToFavorite(imageID)
                .subscribe(
                        { status ->
                            print(status)
                        },
                        { e ->
                            e.printStackTrace()
                        }
                )
    }


    // Initializer

    init {
        bindComments()
        fetchComments()
    }
}