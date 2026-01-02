package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val container = findViewById<LinearLayout>(R.id.containerCard)

        val menuDiagnosa = container.getChildAt(0) as MaterialCardView
        val menuObatku = container.getChildAt(1) as MaterialCardView
        val menuInformasi = container.getChildAt(2) as MaterialCardView
        val menuTentang = container.getChildAt(3) as MaterialCardView

        menuDiagnosa.setOnClickListener {
            startActivity(Intent(this, Intro::class.java))
        }

        menuObatku.setOnClickListener {
            startActivity(Intent(this, Obatku::class.java))
        }

        menuInformasi.setOnClickListener {
            startActivity(Intent(this, LatihanPernapasan::class.java))
        }

        menuTentang.setOnClickListener {
            // TODO: Tentang
        }
    }
}
