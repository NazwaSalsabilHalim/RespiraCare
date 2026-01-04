package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import com.example.respiracare.fragment.ObatkuFragment
import com.google.android.material.card.MaterialCardView

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
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("FRAGMENT_TO_LOAD", "obatku")  // Tambah extra untuk tentukan fragment
            startActivity(intent)
        }

        menuInformasi.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("FRAGMENT_TO_LOAD", "latihan")
            startActivity(intent)
        }

        menuTentang.setOnClickListener {
            // TODO: Tentang
        }
    }
}
