package com.example.epicture_compose.ui.favourite

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.viewModel
import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.ui.detail.ContentDetailView
import com.example.epicture_compose.ui.shared.FeedView
import com.example.epicture_compose.ui.shared.FeedViewCollection

@Composable
fun FavouriteView(
        favouriteViewModel: FavouriteViewModel = viewModel(),
        onSelected: (FeedItem) -> Unit
) {
    val feedItems: List<FeedItem> by favouriteViewModel.feedItems.observeAsState(listOf())

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(text = "Favourites")
                        },
                        actions = {

                        }
                )
            }
    ) { innerPadding ->
        FeedViewCollection(
                feedItems = feedItems,
                onSelected = {
                    onSelected(it)
                }
        )
    }

}