package com.example.epicture_compose.nav

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.loadImageResource
import java.util.*

/**
 * Class which defines the routes of the application.
 */
sealed class AppRoute {
    abstract fun getIconID(): Int

    object HomeRoute : AppRoute() {
        override fun toString(): String {
            return "Home"
        }

        override fun getIconID(): Int {
            return com.example.epicture_compose.R.drawable.ic_baseline_home_24
        }
    }

    object SearchRoute: AppRoute() {
        override fun toString(): String {
            return "Search"
        }

        override fun getIconID(): Int {
            return com.example.epicture_compose.R.drawable.ic_baseline_search_24
        }
    }
    object FavouriteRoute : AppRoute() {
        override fun toString(): String {
            return "Favourite"
        }

        override fun getIconID(): Int {
            return com.example.epicture_compose.R.drawable.ic_baseline_star_24
        }
    }

    object UserRoute : AppRoute() {
        override fun toString(): String {
            return "Profile"
        }

        override fun getIconID(): Int {
            return com.example.epicture_compose.R.drawable.ic_baseline_person_24
        }
    }

    object BottomTabsRoute : AppRoute() {
        override fun getIconID(): Int {
            TODO("Not yet implemented")
        }

        override fun toString(): String {
            return "Bottom tabs"
        }
    }

    object DetailRoute : AppRoute() {
        override fun getIconID(): Int {
            TODO("Not yet implemented")
        }

        override fun toString(): String {
            return "Detail"
        }
    }
}