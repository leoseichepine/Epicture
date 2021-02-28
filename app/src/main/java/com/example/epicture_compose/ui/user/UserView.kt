package com.example.epicture_compose.ui.user

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.core.content.ContextCompat.startActivity
import com.example.epicture_compose.MainActivity
import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.data.model.User
import com.example.epicture_compose.networking.UploadActivity
import com.example.epicture_compose.ui.detail.ContentDetailView
import com.example.epicture_compose.ui.shared.FeedView
import com.example.epicture_compose.ui.shared.FeedViewCollection
import com.example.epicture_compose.ui.shared.PhotoView

@Composable
fun UserProfile(
    user: User,
) {
    val didFail = remember { mutableStateOf(false) }
    val padding = 16.dp

    Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
    ) {
        PhotoView(url = user.pictureURL, didFail = didFail, Modifier.preferredSize(50.dp))
        Spacer(Modifier.padding(padding))
        Text(text = user.name, style = MaterialTheme.typography.h6)
    }
}

@Composable
fun UserView(
        userViewModel: UserViewModel = viewModel(),
        onSelected: (FeedItem) -> Unit
) {
    val user: User by userViewModel.user.observeAsState(User("", ""))
    val feedItems: List<FeedItem> by userViewModel.feedItems.observeAsState(listOf())
    val padding = 16.dp
    val context = ContextAmbient.current

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(text = "Profile")
                        },
                        actions = {
                            IconButton(onClick = {
                                val upload = Intent(context, UploadActivity::class.java)
                                startActivity(context, upload, null)
                            }) {
                                Icon(
                                        asset = Icons.Filled.CloudUpload
                                )
                            }
                        }
                )
            }
    ) { innerPadding ->
        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
        ) {
            UserProfile(user = user)
            Spacer(Modifier.preferredSize(padding))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(Modifier.preferredSize(padding))
            Text(
                    modifier = Modifier.padding(horizontal = padding),
                    text = "My Gallery",
                    style = MaterialTheme.typography.h5)
            FeedViewCollection(
                    feedItems = feedItems,
                    onSelected = {
                        onSelected(it)
                    }
            )
        }
    }

}