package com.example.epicture_compose.networking

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

object Constants {
    const val BASE_URL = "https://api.imgur.com/3/"
}
interface APIService {
    @GET
    fun getAvatar(@Url url: String, @HeaderMap header: Map<String, String>): Observable<AvatarAPIResponse>
    @GET
    fun getImage(@Url url: String, @HeaderMap header: Map<String, String>): Observable<ImageAPIResponse>
    @GET
    fun getUserImages(@Url url: String, @HeaderMap header: Map<String, String>): Observable<ImageListAPIResponse>
    @GET
    fun getUserFavorites(@Url url: String, @HeaderMap header: Map<String, String>): Observable<ImageListAPIResponse>
    @GET
    fun getGallery(@Url url: String, @HeaderMap header: Map<String, String>): Observable<ImageListAPIResponse>
    @GET
    fun getSearchInGallery(@Url url: String, @HeaderMap header: Map<String, String>): Observable<ImageListAPIResponse>

    @POST("https://api.imgur.com/oauth2/token")
    @FormUrlEncoded
    fun refreshCredentials(@Field("refresh_token") refresh_token:String, @Field("client_id") client_id:String, @Field("client_secret") client_secret:String, @Field("grant_type") grant_type:String?="refresh_token"): Observable<RefreshCredentialResponse>

    @POST("https://api.imgur.com/3/upload")
    fun uploadImage(@HeaderMap header: Map<String, String>, @Body body: RequestBody): Observable<ImageAPIResponse>

    @POST("https://api.imgur.com/3/comment")
    fun postComment(@HeaderMap header: Map<String, String>, @Body body: RequestBody): Observable<BaseAPIResponse>

    @POST
    fun voteImage(@Url url: String, @HeaderMap header: Map<String, String>): Observable<BaseAPIResponse>

    @POST
    fun addFavorite(@Url url: String): Observable<BaseAPIResponse>
}

class APIClient {
    private var retrofit: Retrofit? = null

    enum class LogLevel {
        LOG_NOT_NEEDED,
        LOG_REQ_RES,
        LOG_REQ_RES_BODY_HEADERS,
        LOG_REQ_RES_HEADERS_ONLY
    }

    /**
     * Returns Retrofit builder to create
     * @param logLevel - to print the log of Request-Response
     * @return retrofit
     */
    fun getClient(logLevel: LogLevel): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        when(logLevel) {
            LogLevel.LOG_NOT_NEEDED ->
                interceptor.level = HttpLoggingInterceptor.Level.NONE
            LogLevel.LOG_REQ_RES ->
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
            LogLevel.LOG_REQ_RES_BODY_HEADERS ->
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            LogLevel.LOG_REQ_RES_HEADERS_ONLY ->
                interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        }

        val client = OkHttpClient.Builder().connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES).addInterceptor(interceptor).build()

        if(null == retrofit) {
            retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()
        }

        return retrofit!!
    }

    fun getAPIService(logLevel: LogLevel = LogLevel.LOG_REQ_RES_BODY_HEADERS) = getClient(logLevel).create(APIService::class.java)
}