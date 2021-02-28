@file:Suppress("DEPRECATION")

package com.example.epicture_compose.ui

import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.epicture_compose.data.model.FeedItem
import com.koduok.compose.navigation.Router
import com.koduok.compose.navigation.core.BackStack
import com.example.epicture_compose.nav.AppRoute
import com.example.epicture_compose.ui.detail.DetailView
import com.example.epicture_compose.ui.favourite.FavouriteView
import com.example.epicture_compose.ui.favourite.FavouriteViewModel
import com.example.epicture_compose.ui.home.HomeView
import com.example.epicture_compose.ui.home.HomeViewModel
import com.example.epicture_compose.ui.search.SearchView
import com.example.epicture_compose.ui.search.SearchViewModel
import com.example.epicture_compose.ui.shared.VectorResourceDrawable
import com.example.epicture_compose.ui.user.UserView
import com.example.epicture_compose.ui.user.UserViewModel

@Composable
fun BottomTabsScreen() {
    var selectedItem: FeedItem? = null

    Router<AppRoute>("Tab", start = AppRoute.HomeRoute) {
        Column {
            Box(modifier = Modifier.weight(1f)) {
                when (it.data) {
                    AppRoute.HomeRoute -> HomeView(HomeViewModel(), onSelected = {
                        selectedItem = it
                        push(AppRoute.DetailRoute)
                    })
                    AppRoute.SearchRoute -> SearchView(SearchViewModel(), onSelected = {
                        selectedItem = it
                        push(AppRoute.DetailRoute)
                    })
                    AppRoute.FavouriteRoute -> FavouriteView(FavouriteViewModel(), onSelected = {
                        selectedItem = it
                        push(AppRoute.DetailRoute)
                    })
                    AppRoute.UserRoute -> UserView(UserViewModel(), onSelected = {
                        selectedItem = it
                        push(AppRoute.DetailRoute)
                    })
                    AppRoute.DetailRoute -> DetailView(feedItem = selectedItem)
                }
            }

            BottomNavigation {
                TabButton(backStack = this@Router, tabRoute = AppRoute.HomeRoute, currentTabRoute = it.data)
                TabButton(backStack = this@Router, tabRoute = AppRoute.SearchRoute, currentTabRoute = it.data)
                TabButton(backStack = this@Router, tabRoute = AppRoute.FavouriteRoute, currentTabRoute = it.data)
                TabButton(backStack = this@Router, tabRoute = AppRoute.UserRoute, currentTabRoute = it.data)
            }
        }
    }
}

@Composable
fun RowScope.TabButton(backStack: BackStack<AppRoute>, tabRoute: AppRoute, currentTabRoute: AppRoute) {
    val isSelected = currentTabRoute == tabRoute
    Box(modifier = Modifier.weight(1f), backgroundColor = Color.Gray.copy(alpha = if (isSelected) 1f else 0.3f)) {
        Box(Modifier.fillMaxSize().clickable(onClick = { backStack.replace(tabRoute) })) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    VectorResourceDrawable(
                            id = tabRoute.getIconID()
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                            text = tabRoute.toString(),
                            style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}