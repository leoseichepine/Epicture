package com.example.epicture_compose.ui.home

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import com.example.epicture_compose.data.model.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.example.epicture_compose.ui.shared.FeedViewCollection

// TODO: https://joebirch.co/android/exploring-jetpack-compose-radio-group/

enum class Filter {
    Viral,
    NoViral,
    Mature,
}


@Composable
fun FiltersView(
        onSelected: (Filter) -> Unit
) {
    val radioOptions = enumValues<Filter>()
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Column(
            modifier = Modifier
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        radioOptions.forEach { text ->
            Row(Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                onSelected(text)
                            }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                )
                Text(
                        text = text.toString(),
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun HomeView(
    homeViewModel: HomeViewModel = viewModel(),
    onSelected: (FeedItem) -> Unit
) {
    val feedItems: List<FeedItem> by homeViewModel.feedItems.observeAsState(listOf())
    val padding = 16.dp

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(text = "Home")
                        },
                        actions = {
                        },
                )
            }
    ) { innerPadding ->
        Column(
                modifier = Modifier
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.preferredSize(padding))
            FiltersView(onSelected = { filter ->
                when(filter) {
                    Filter.Viral ->
                        homeViewModel.showViral(true)
                    Filter.NoViral ->
                        homeViewModel.showViral(true)
                    Filter.Mature ->
                        homeViewModel.showMature(true)
                }
            })
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