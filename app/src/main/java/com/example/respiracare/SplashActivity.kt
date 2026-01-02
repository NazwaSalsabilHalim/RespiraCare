package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val auth = FirebaseAuth.getInstance()

        window.decorView.postDelayed({

            val intent = if (auth.currentUser != null) {
                // User sudah login
                Intent(this, Home::class.java)
            } else {
                // User belum login
                Intent(this, LoginActivity::class.java)
            }

            // 🔥 bersihkan semua activity sebelumnya
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()

        }, 1200)
    }
}
