package com.example.epicture_compose.networking

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class UploadActivity : AppCompatActivity() {
    var FROM_GALLERY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(ContentValues.TAG, "on create!")
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, FROM_GALLERY, null)
    }

    private fun uploadSelectedImage(bitmap: Bitmap, title: String = "", description: String? = "")
    {
        ImgurAPI.uploadImageFromBitmap(bitmap, title, description)
        finish()
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.getScheme().equals("content")) {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.getPath()
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            finish()
            return
        }
        when (requestCode) {
            FROM_GALLERY -> {
                val image = data!!.data
                val name = getFileName(image!!)
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image)
                uploadSelectedImage(bitmap, name!!)
            }
        }
    }
}