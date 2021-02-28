package com.example.epicture_compose.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.loadVectorResource

@Composable
fun VectorResourceDrawable(id: Int) {
    val image = loadVectorResource(id = id)

    image.resource.resource?.let {
        Image(asset = it)
    }
}