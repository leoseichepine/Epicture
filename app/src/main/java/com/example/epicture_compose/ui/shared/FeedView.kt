package com.example.epicture_compose.ui.shared

import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.HorizontalAlignmentLine
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.epicture_compose.data.model.FeedItem
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PhotoView(
        url: String,
        didFail: MutableState<Boolean>,
        modifier: Modifier
) {
    CoilImage(
            imageModel = url,
            // shows a progress indicator when loading an image.
            loading = {
                ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                ) {
                    Column {
                        CircularProgressIndicator(
                                modifier = modifier
                        )
                    }
                }
                didFail.value = false
            },
            // shows an error text message when request failed.
            failure = {
                didFail.value = true
            },
            modifier = modifier
    )
}

@Composable
fun PhotoCellView(
        feedItem: FeedItem,
        onClick: () -> Unit
) {
    val didFail = remember { mutableStateOf(false) }
    val padding = 16.dp

    Column(
            Modifier
                    .clickable(onClick = onClick)
                    .padding(padding)
                    .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            feedItem.photo.title?.let { Text(it, style = MaterialTheme.typography.h6) }
        }
        Spacer(Modifier.preferredSize(padding))
        Card(elevation = 4.dp) {
            if (!didFail.value) {
                PhotoView(url = feedItem.photo.url, didFail = didFail, Modifier)
            }
        }
        Spacer(Modifier.preferredSize(padding))
        Row(verticalAlignment = Alignment.CenterVertically) {
            feedItem.photo.description?.let { Text(it, style = MaterialTheme.typography.caption) }
        }
        Spacer(Modifier.preferredSize(padding))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
fun PhotoCollectionCellView(
        feedItem: FeedItem,
        onClick: () -> Unit
) {
    val didFail = remember { mutableStateOf(false) }
    val padding = 16.dp

    Column(
            modifier = Modifier
                    .clickable(onClick = onClick)
                    .padding(padding)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(color = Color.LightGray, thickness = 1.dp)
        Spacer(Modifier.preferredSize(padding))
        Row(verticalAlignment = Alignment.CenterVertically) {
            feedItem.photo.title?.let {
                Text(
                        text = it,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1
                )
            }
        }
        Spacer(Modifier.preferredSize(padding))
        Card(elevation = 4.dp) {
            if (!didFail.value) {
                PhotoView(
                        url = feedItem.photo.url,
                        didFail = didFail,
                        Modifier
                                .preferredSize(200.dp)
                                .align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(Modifier.preferredSize(padding))
        Row(verticalAlignment = Alignment.CenterVertically) {
            feedItem.photo.description?.let {
                Text(
                        text = it,
                        style = MaterialTheme.typography.caption,
                        maxLines = 3
                )
            }
        }
    }
}

@Composable
fun FeedViewCollection(
        feedItems: List<FeedItem>,
        onSelected: (FeedItem) -> Unit
) {
    LazyGridFor(feedItems, 2) { item ->
        PhotoCollectionCellView(item) { onSelected(item) }
    }
}

@Composable
fun FeedView(
        feedItems: List<FeedItem>,
        onSelected: (FeedItem) -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        LazyColumnFor(feedItems) { item ->
            PhotoCellView(item) { onSelected(item) }
        }
    }
}