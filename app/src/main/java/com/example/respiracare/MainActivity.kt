package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.respiracare.fragment.LatihanPernapasanFragment
import com.example.respiracare.fragment.ObatkuFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tombol logout (TETAP)
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Cek intent fragment (TETAP)
        val fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")
        val fragment = when (fragmentToLoad) {
            "obatku" -> ObatkuFragment()
            "latihan" -> LatihanPernapasanFragment()
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, it)
                .commit()
        }

        // 🔽 TAMBAHAN: FOOTER NAV
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // balik ke halaman awal (tidak ganti fragment)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, profil::class.java))
                    true
                }
                else -> false
            }
        }
    }
}