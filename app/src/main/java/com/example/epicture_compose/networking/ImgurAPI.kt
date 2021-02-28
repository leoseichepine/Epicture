@file:Suppress("DEPRECATION")

package com.example.epicture_compose.networking

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import rx.Observable
import com.example.epicture_compose.data.model.Avatar
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap

object ImgurAPI
{
    /**
     * Holds the URL to the api
     */
    private const val apiHost = "https://api.imgur.com/3/"

    /**
     * Holds the client ID of the application
     */
    private const val clientId: String = "a57524ef19659f8"

    /**
     *  Holds the client secret ID of the application
     */
    private const val clientSecret: String = "3097b663920703fc649c2634728b75dc8d400e20"

    /**
     * Holds connection's state to the API
     */
    private var connected: Boolean = false;

    /**
     * Function to get the API Connection State
     */
    fun isConnected(): Boolean
    {
        return connected;
    }

    /**
     * Function to start Authentification to the API
     *
     * @param context The Application Context
     */
    fun startAuthentification(context: Context)
    {
        val url = "https://api.imgur.com/oauth2/authorize?client_id=$clientId&response_type=token"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(context, intent, null);
    }

    /**
     * Holds user Imgur information
     */
    private val credentials: HashMap<String, String> = HashMap()

    /**
     * Return user information
     */
    fun getCredentials(): HashMap<String, String>
    {
        return credentials;
    }

    /**
     * Parse authentification response and fills the HashMap accordingly
     */
    fun loadCredentials(data: String?)
    {
        val request = data!!.split("#")[1];
        for (parsed in request.split('&')) {
            val v = parsed.split('=');
            credentials[v[0]] = v[1]
        }
        for (cred in credentials) {
            println(cred.key + " = " + cred.value)
        }
        connected = true
    }

    /**
     * Refresh user information
     */
    fun refreshCredentials()
    {
        APIClient()
                .getAPIService()
                .refreshCredentials(credentials["refresh_token"]!!, clientId, clientSecret)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { response ->
                            credentials["access_token"] = response.access_token
                            credentials["refresh_token"] = response.refresh_token
                            credentials["expires_in"] = response.expires_in
                        },
                        onError = { e ->
                            e.printStackTrace()
                        }
                )
    }

    /**
     * Make a request to get the user's avatar from the API
     */
    fun fetchAvatar(): Observable<Avatar> {
        val url = apiHost + "account/" + credentials["account_username"] + "/avatar"
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]

            return Observable.create() { emitter ->
            APIClient()
                    .getAPIService()
                    .getAvatar(url, header)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { response ->
                                response.data.username = credentials["account_username"]!!
                                if (response.success) {
                                    emitter.onNext(response.data)
                                } else {
                                    emitter.onError(Exception("Getting avatar request has failed."))
                                }
                            },
                            onError = { e->
                                e.printStackTrace()
                                emitter.onError(e)
                            }
                    )
        }
    }

    /**
     * Make a request to fetch an image from the API
     */
    fun fetchImage(imageId: String): Observable<Image> {
        val url = apiHost + "image/$imageId"
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]

        return Observable.create { emitter ->
            APIClient()
                    .getAPIService()
                    .getImage(url, header)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { response ->
                                if (response.success) {
                                    emitter.onNext(response.data)
                                } else {
                                    emitter.onError(Exception("Fetching image request has failed."))
                                }
                            },
                            onError = { e ->
                                e.printStackTrace()
                                emitter.onError(e)
                            }
                    )
        }
    }

    /**
     * Make a request to fetch user's images from the API
     */
    fun fetchUserImages(): Observable<List<Image>> {
        val url = apiHost + "account/" + credentials["account_username"] + "/images"
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]

        Log.i(ContentValues.TAG, "Fetch user images!")
        return Observable.create { emitter ->
            APIClient()
                    .getAPIService()
                    .getUserImages(url, header)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { response ->
                                if (response.success) {
                                    emitter.onNext(response.data)
                                } else {
                                    emitter.onError(Exception("Getting user images request has failed."))
                                }
                            },
                            onError = { e ->
                                e.printStackTrace()
                                emitter.onError(e)
                            }
                    )
        }
    }

    /**
     * Make a request to fetch user's favorites from the API
     */
    fun fetchUserFavorites(page: String? = ""): Observable<List<Image>> {
        val url = apiHost + "account/" + credentials["account_username"] + "/favorites/$page/newest"
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]

        return Observable.create { emitter ->
            APIClient()
                    .getAPIService()
                    .getUserFavorites(url, header)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { response ->
                                if (response.success) {
                                    emitter.onNext(response.data)
                                } else {
                                    emitter.onError(Exception("Getting user favourites request has failed."))
                                }
                            },
                            onError = { e ->
                                e.printStackTrace()
                                emitter.onError(e)
                            }
                    )
        }
    }

    /**
     * Make a request to add an image to user's favorites
     *
     * @param imageId Id of image to add to favorites
     */
    fun addToFavorite(imageId: String): Observable<Boolean> {
        val url: String = "https://api.imgur.com/3/image/$imageId/favorite"

        return Observable.create { emitter ->
            APIClient()
                    .getAPIService()
                    .addFavorite(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { response ->
                                println(response.success)
                                println(response.status)
                                emitter.onNext(response.success)
                            },
                            onError = { e ->
                                e.printStackTrace()
                                emitter.onError(e)
                            }
                    )
        }

    }

    /**
     * Make a request to fetch an entire gallery
     *
     * todo
     * @param section Gallery's section, either "hot", "top", or "user"
     * @param sort Gallery's sorting option, either "viral", "top", or "time"
     * @param page The data paging number
     * @param window Change the date range of the request if the sorting is "top", either "day", "week", "month", "year", or "all"
     * @param showViral either "true" or "false", show or hide viral images
     * @param showMature either "true" or "false", show or hide mature images
     * @param albumPreviews either "true" or "false", include metadata for posts which are albums
     */
    fun fetchGallery(section: String? = "hot", sort: String? = "viral", page: String? = "0", window: String? = "day",
                     showViral: String? = "true", showMature: String? = "false", albumPreviews: String? = "false"): Observable<List<Image>> {
        val url = apiHost + "gallery/$section/$sort/$window/$page/"
        val params = "?showViral=$showViral&showMature=$showMature&albumPreview=$albumPreviews"
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]

        return Observable.create { emitter ->
            APIClient()
                    .getAPIService()
                    .getGallery(url + params, header)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { response ->
                                if (response.success) {
                                    emitter.onNext(response.data)
                                } else {
                                    emitter.onError(Exception("Getting gallery request has failed."))
                                }
                            },
                            onError = { e ->
                                e.printStackTrace()
                                emitter.onError(e)
                            }
                    )
        }
    }

    /**
     * Make a request to do a research in a gallery
     *
     * todo
     * @param query The research query (ex: "cats")
     * @param sort Search's sorting option, either "time", "viral", or "top"
     * @param window Change the date range of the request if the sorting is "top", either "day", "week", "month", "year", or "all"
     * @param page The data paging number
     */
    fun searchInGallery(query: String, sort: String? = "time", window: String? = "all", page: String? = ""): Observable<List<Image>> {
        val url = apiHost + "gallery/search/$sort/$window/$page/?q=$query"
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]

        return Observable.create { emitter ->
            APIClient()
                    .getAPIService()
                    .getSearchInGallery(url, header)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { response ->
                                if (response.success) {
                                    emitter.onNext(response.data)
                                } else {
                                    emitter.onError(Exception("Getting search gallery request has failed."))
                                }
                            },
                            onError = { e ->
                                e.printStackTrace()
                                emitter.onError(e)
                            }
                    )
        }
    }

    /**
     * Make a request to upload an image from a url
     *
     * @param url Image's url
     * @param title Image's title
     * @param description Image's description
     */
    fun uploadImageFromUrl(url: String, title:String, description: String?="") {
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]
        val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", url)
                .addFormDataPart("type", "url")
                .addFormDataPart("title", title)
                .addFormDataPart("description", description!!)
                .build()

        APIClient()
                .getAPIService()
                .uploadImage(header, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { response ->
                            println(response.success)
                            println(response.status)
                            println(response.data.title)
                        },
                        onError = { e ->
                            e.printStackTrace()
                        }
                )
    }

    /**
     * Utility function to convert Bitmap image to Base64
     *
     * @param img Bitmap information of the image
     */
    fun bitmapToBase64(img: Bitmap): String
    {
        val stream: ByteArrayOutputStream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return Base64.getEncoder().encodeToString(stream.toByteArray())
    }

    /**
     * Make a request to upload an image from Bitmap information
     *
     * @param url Image's url
     * @param title Image's title
     * @param description Image's description
     */
    fun uploadImageFromBitmap(img: Bitmap, title:String, description: String?="") {
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]
        val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", bitmapToBase64(img))
                .addFormDataPart("type", "base64")
                .addFormDataPart("title", title)
                .addFormDataPart("description", description!!)
                .build()

        APIClient()
                .getAPIService()
                .uploadImage(header, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { response ->
                            println(response.success)
                            println(response.status)
                            println(response.data.title)
                        },
                        onError = { e ->
                            e.printStackTrace()
                        }
                )
    }

    /**
     * Make a request to add a comment to an image
     *
     * @param imageID Image's ID
     * @param comment Comment text value
     */
    fun postComment(imageID: String, comment: String) {
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]
        val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image_id", imageID)
                .addFormDataPart("comment", comment)
                .build()

        APIClient()
                .getAPIService()
                .postComment(header, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {response ->
                            println(response.success)
                        },
                        onError = {e ->
                            e.printStackTrace()
                        }
                )
    }

    /**
     * Make a request to vote for an image
     *
     * @param imageID Image's ID
     * @param vote Vote type, either "up" or "down"
     */
    fun voteImage(imageID: String, vote: String) {
        val url = "https://api.imgur.com/3/gallery/$imageID/vote/$vote"
        val header = HashMap<String,String>()
        header["Authorization"] = "Bearer " + credentials["access_token"]

        APIClient()
                .getAPIService()
                .voteImage(url, header)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { response ->
                            println(response.success)
                        },
                        onError = { e ->
                            e.printStackTrace()
                        }
                )
    }
}