package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.respiracare.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val auth = FirebaseAuth.getInstance()

        val nextIntent = if (auth.currentUser != null) {
            // Sudah login
            Intent(this, Home::class.java)
        } else {
            // Belum login
            Intent(this, LoginActivity::class.java)
        }

        startActivity(nextIntent)
        finish()
    }
}