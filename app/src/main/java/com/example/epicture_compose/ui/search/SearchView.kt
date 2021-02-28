package com.example.epicture_compose.ui.search

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.ui.tooling.preview.Preview
import com.example.epicture_compose.data.model.FeedItem
import com.example.epicture_compose.ui.detail.ContentDetailView
import com.example.epicture_compose.ui.shared.FeedView
import com.example.epicture_compose.ui.shared.FeedViewCollection

@Composable
fun SearchBarView(
        searchViewModel: SearchViewModel
) {
    val padding = 16.dp
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        OutlinedTextField(
                value = textState.value,
                onValueChange = {
                    textState.value = it
                    searchViewModel.getFeedItems(textState.value.text)
                },
                label = { Text("Search for an image") }
        )
    }
}

@Composable
fun SearchView(
        searchViewModel: SearchViewModel = viewModel(),
        onSelected: (FeedItem) -> Unit
) {
    val feedItems: List<FeedItem> by searchViewModel.feedItems.observeAsState(listOf())
    val padding = 16.dp

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(text = "Search")
                        },
                        actions = {
                        }
                )
            }
    ) { innerPadding ->
        Column {
            SearchBarView(searchViewModel = searchViewModel)
            Spacer(Modifier.preferredSize(padding))
            FeedViewCollection(
                    feedItems = feedItems,
                    onSelected = {
                        onSelected(it)
                    }
            )
        }
    }

}