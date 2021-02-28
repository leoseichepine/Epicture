package com.example.epicture_compose

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.example.epicture_compose.nav.AppRoute
import com.example.epicture_compose.ui.BottomTabsScreen
import com.example.epicture_compose.ui.login.LoginActivity
import com.koduok.compose.navigation.Router
import com.koduok.compose.navigation.core.backStackController
import com.example.epicture_compose.networking.ImgurAPI
import com.example.epicture_compose.networking.UploadActivity
import com.example.epicture_compose.ui.shared.VectorResourceDrawable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

private fun initRxErrorHandler(){
    RxJavaPlugins.setErrorHandler { throwable ->
        if (throwable is UndeliverableException) {
            throwable.cause?.let {
                Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(Thread.currentThread(), it)
                return@setErrorHandler
            }
        }
        if (throwable is IOException || throwable is SocketException) {
            // fine, irrelevant network problem or API that throws on cancellation
            return@setErrorHandler
        }
        if (throwable is InterruptedException) {
            // fine, some blocking code was interrupted by a dispose call
            return@setErrorHandler
        }
        if (throwable is NullPointerException || throwable is IllegalArgumentException) {
            // that's likely a bug in the application
            Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(Thread.currentThread(), throwable)
            return@setErrorHandler
        }
        if (throwable is IllegalStateException) {
            // that's a bug in RxJava or in a custom operator
            Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(Thread.currentThread(), throwable)
            return@setErrorHandler
        }
        Log.w("Undeliverable exception", throwable)
    }
}

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRxErrorHandler()
        if (ImgurAPI.isConnected()) {
            setContent {
                MaterialTheme {
                    /*
                    Button(onClick = {
                        Log.i(ContentValues.TAG, "clicked!")
                        val upload = Intent(this, UploadActivity::class.java)
                        startActivity(upload)
                    }, Modifier) {
                        VectorResourceDrawable(com.example.epicture_compose.R.drawable.ic_baseline_cloud_upload_24)
                    }
                     */
                    AppRoot()
                }
            }
        } else {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (!backStackController.pop()) super.onBackPressed()
    }
}

@Composable
fun AppRoot() {
    Router<AppRoute>("Root", AppRoute.BottomTabsRoute) {
        when (it.data) {
            AppRoute.BottomTabsRoute -> BottomTabsScreen()
        }
    }
}