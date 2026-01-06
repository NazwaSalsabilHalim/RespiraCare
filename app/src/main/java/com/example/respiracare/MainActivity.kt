package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.respiracare.fragment.LatihanPernapasanFragment
import com.example.respiracare.fragment.ObatkuFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tombol logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Cek intent untuk load fragment tertentu
        val fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")
        val fragment = when (fragmentToLoad) {
            "obatku" -> ObatkuFragment()
            "latihan" -> LatihanPernapasanFragment()
            else -> null
        }

        // Replace fragment
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, it)
                .commit()
        }
    }
}