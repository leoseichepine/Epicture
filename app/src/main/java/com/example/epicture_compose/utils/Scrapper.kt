package com.example.epicture_compose.utils

import com.example.epicture_compose.data.model.Photo
import com.example.epicture_compose.data.model.Score
import com.example.epicture_compose.extensions.indexesOf
import com.example.epicture_compose.extensions.isValidExtension

/**
 * Class to scrap the html content of the Imgur gallery
 */
class Scrapper {
    /**
     * Function to get image url from the Imgur gallery
     *
     * @param document The url of the gallery
     * @return the url of the image
     */
    fun getImageURLFromGallery(document: String): String {
        val indexes = document.indexesOf(".jpg", true)
        if (indexes.count() > 0) {
            val startIndex = indexes[0] - 50
            val endIndex = indexes[0] + ".jpg".count()
            if (startIndex >= 0 && endIndex < document.count()) {
                var result = document.substring(startIndex, endIndex)
                result = result.substring(result.indexOf("content="), result.indexOf(".jpg") + ".jpg".count())
                result = result.removePrefix("""content="""")
                return result
            }
        }
        return ""
    }
}