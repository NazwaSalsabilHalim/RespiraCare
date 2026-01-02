package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Container utama
        val container = findViewById<LinearLayout>(R.id.containerCard)

        // Ambil card berdasarkan urutan di XML
        val menuDiagnosa = container.getChildAt(0) as MaterialCardView
        val menuObatku = container.getChildAt(1) as MaterialCardView
        val menuInformasi = container.getChildAt(2) as MaterialCardView
        val menuTentang = container.getChildAt(3) as MaterialCardView

        // MENU 1 - Diagnosa
        menuDiagnosa.setOnClickListener {
            startActivity(Intent(this, Intro::class.java))
        }

        // MENU 2 - Obatku
        menuObatku.setOnClickListener {
            startActivity(Intent(this, Obatku::class.java))
        }

        // MENU 3 - Informasi
        menuInformasi.setOnClickListener {
            startActivity(Intent(this, LatihanPernapasan::class.java))
        }

        // MENU 4 - Tentang
        menuTentang.setOnClickListener {
            // TODO: halaman Tentang
        }
    }
}