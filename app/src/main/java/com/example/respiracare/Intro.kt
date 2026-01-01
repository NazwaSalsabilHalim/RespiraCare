package com.example.respiracare

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Intro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btnStart = findViewById<LinearLayout>(R.id.btnStart)

        btnStart.setOnClickListener {
            Toast.makeText(this, "Tombol Mulai Prediksi diklik", Toast.LENGTH_SHORT).show()
        }
    }
}