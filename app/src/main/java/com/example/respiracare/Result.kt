package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class Result : AppCompatActivity() {
    private val treatmentMap = mapOf(
        "Rest" to "Istirahat yang cukup dan kurangi aktivitas berat",
        "Medication" to "Minum obat sesuai anjuran dokter",
        "Hospitalization" to "Segera konsultasi atau rawat inap",
        "Lifestyle" to "Hindari pemicu dan jaga pola hidup sehat"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val name = intent.getStringExtra("EXTRA_NAME") ?: "Pengguna"
        val disease = intent.getStringExtra("EXTRA_DISEASE") ?: "-"
        val severity = intent.getStringExtra("EXTRA_SEVERITY") ?: "-"
        val treatment = intent.getStringExtra("EXTRA_TREATMENT") ?: "-"

        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val tvDisease = findViewById<TextView>(R.id.tvDisease)
        val tvSeverity = findViewById<TextView>(R.id.tvSeverity)
        val tvCause = findViewById<TextView>(R.id.tvCause)
        val btnBack = findViewById<android.view.View>(R.id.btnBack)

        val treatmentDisplay = treatmentMap[treatment] ?: treatment

        tvGreeting.text = "Halo, $name 👋"
        tvDisease.text = disease
        tvSeverity.text = "Tingkat keparahan: $severity"
        tvCause.text = "🫁 Rekomendasi Treatment\n• $treatmentDisplay"

        btnBack.setOnClickListener {
            val intent = Intent(this, Intro::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}