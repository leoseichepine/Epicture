package com.example.epicture_compose.ui.detail

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.example.epicture_compose.data.model.Comment
import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.ui.shared.PhotoView

@Composable
fun VoteView(
        detailViewModel: DetailViewModel,
        feedItem: FeedItem
) {
    val totalVote = remember { mutableStateOf(feedItem.photo.score.up + feedItem.photo.score.down) }
    val isSelectedUp = remember { mutableStateOf(false) }
    val isSelectedDown = remember { mutableStateOf(false) }
    val totalVoteOriginal = feedItem.photo.score.up + feedItem.photo.score.down

    Row(verticalAlignment = Alignment.CenterVertically) {
        // UP
        IconButton(onClick = {
            if (!isSelectedUp.value) {
                totalVote.value = totalVoteOriginal + 1
                detailViewModel.voteUp(imageID = feedItem.photo.id)
            } else {
                isSelectedUp.value = false
                totalVote.value = totalVoteOriginal
                return@IconButton
            }
            isSelectedDown.value = false
            isSelectedUp.value = true
        }) {
            Icon(
                    asset = Icons.Filled.ArrowUpward,
                    tint = if (isSelectedUp.value) Color.Green else Color.Black
            )
        }

        Text(text = totalVote.value.toString())

        // Down
        IconButton(onClick = {
            if (!isSelectedDown.value) {
                totalVote.value = totalVoteOriginal - 1
                detailViewModel.voteDown(imageID = feedItem.photo.id)
            } else {
                isSelectedDown.value = false
                totalVote.value = totalVoteOriginal
                return@IconButton
            }
            isSelectedDown.value = true
            isSelectedUp.value = false
        }) {
            Icon(
                    asset = Icons.Filled.ArrowDownward,
                    tint = if (isSelectedDown.value) Color.Red else Color.Black
            )
        }
    }
}

@Composable
fun CommentsView(
        detailViewModel: DetailViewModel,
        feedItem: FeedItem
) {
    val comments: MutableList<Comment> by detailViewModel.comments.observeAsState(mutableListOf())
    val nbComments = remember { mutableStateOf(feedItem.photo.comments.count()) }
    val label = if (nbComments.value > 0) "Comments" else "Comment"
    val padding = 16.dp
    val textState = remember { mutableStateOf(TextFieldValue()) }

    Column(
            modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = textState.value,
                onValueChange = {
                    textState.value = it
                },
                label = { Text("Write a comment...") }
        )
        Spacer(Modifier.preferredSize(padding))
        Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
            detailViewModel.postComment(
                    imageID = feedItem.photo.id,
                    text = textState.value.text
            )
            nbComments.value += 1
        }) {
            Row {
                Text("Post")
                Spacer(Modifier.preferredSize(8.dp))
                Icon(
                        asset = Icons.Filled.PostAdd,
                )
            }
        }
        Spacer(Modifier.preferredSize(padding))
        Divider(color = Color.LightGray, thickness = 1.dp)
        Spacer(Modifier.preferredSize(padding))
        Text(
                text = "${nbComments.value} $label",
                style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.preferredSize(padding))
    }
}

@Composable
fun ContentDetailView(
        detailViewModel: DetailViewModel,
        feedItem: FeedItem
) {
    val didFail = remember { mutableStateOf(false) }
    val padding = 16.dp

    ScrollableColumn(
            modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            feedItem.photo.title?.let { Text(it, style = MaterialTheme.typography.h6) }
        }
        Spacer(Modifier.preferredSize(padding))
        Card(elevation = 4.dp) {
            if (!didFail.value) {
                PhotoView(url = feedItem.photo.url, didFail = didFail, Modifier.fillMaxSize())
            }
        }
        Spacer(Modifier.preferredSize(padding))
        Row(verticalAlignment = Alignment.CenterVertically) {
            feedItem.photo.description?.let { Text(it, style = MaterialTheme.typography.caption) }
        }
        Spacer(Modifier.preferredSize(padding))
        Divider(color = Color.LightGray, thickness = 1.dp)
        VoteView(detailViewModel, feedItem)
        Divider(color = Color.LightGray, thickness = 1.dp)
        CommentsView(detailViewModel, feedItem)
    }
}

@Composable
fun DetailView(
        detailViewModel: DetailViewModel = viewModel(),
        feedItem: FeedItem?,
        allowMarkAsFavourite: Boolean = true
) {
    val isMarkedAsFavourite = remember { mutableStateOf(false) }

    feedItem?.let { item ->
        Scaffold(
                topBar = {
                    TopAppBar(
                            title = {
                                Text(text = "Post Detail")
                            },
                            actions = {
                                if (allowMarkAsFavourite) {
                                    IconButton(onClick = {
                                        isMarkedAsFavourite.value = !isMarkedAsFavourite.value
                                        if (!isMarkedAsFavourite.value) {
                                            detailViewModel.markAsFavourite(feedItem.photo.url)
                                        }
                                    }) {
                                        Icon(
                                                asset = Icons.Filled.Favorite,
                                                tint = if (isMarkedAsFavourite.value) Color.Red else Color.White
                                        )
                                    }
                                }
                            }
                    )
                }
        ) { innerPadding ->
            ContentDetailView(detailViewModel, item)
        }
    } ?: run {
        Text("Nothing to display.")
    }
}