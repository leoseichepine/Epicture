package com.example.epicture_compose.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> LazyGridFor(
        data: List<T>,
        rowSize: Int = 1,
        itemContent: @Composable BoxScope.(T) -> Unit
) {
    val rows = data.windowed(rowSize, rowSize, true)
    LazyColumnFor(rows) { row ->
        Row(modifier = Modifier.fillParentMaxWidth()) {
            val weight = 1f / rowSize
            for (item in row) {
                Box(modifier = Modifier.weight(weight)) {
                    itemContent(item)
                }
            }
            if (row.size < rowSize) {
                for (i in 0 until (rowSize - row.size)) {
                    Box(modifier = Modifier.weight(weight))
                }
            }
        }
    }
}