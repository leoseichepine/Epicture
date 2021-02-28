package com.example.epicture_compose.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.epicture_compose.networking.ImgurAPI
import com.example.epicture_compose.MainActivity
import com.example.epicture_compose.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var submitBtn = findViewById<Button>(R.id.btn_submit)

        submitBtn.setOnClickListener {
            ImgurAPI.startAuthentification(this)
        }
    }

    override fun onResume() {
        super.onResume()
        val access = intent.dataString;
        val isNull = access.isNullOrEmpty();

        if (!isNull) {
            ImgurAPI.loadCredentials(access)
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }
    }
}