package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.respiracare.fragment.ObatkuFragment
import com.example.respiracare.fragment.LatihanPernapasanFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")

        val fragment = when (fragmentToLoad) {
            "obatku" -> ObatkuFragment()
            "latihan" -> LatihanPernapasanFragment()
            else -> {
                // Buka LoginActivity langsung
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // optional: tutup MainActivity
                null
            }
        }

        // Hanya replace fragment kalau fragment != null
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, it)
                .commit()
        }

        // Tombol logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}