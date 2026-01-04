package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.respiracare.fragment.LatihanPernapasanFragment
import com.google.firebase.firestore.FirebaseFirestore

class Feedback : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val sessionId = intent.getStringExtra("sessionId") ?: return
        val btnKeren = findViewById<LinearLayout>(R.id.btnKeren)
        val btnBaik = findViewById<LinearLayout>(R.id.btnBaik)
        val btnBagus = findViewById<LinearLayout>(R.id.btnBagus)
        val btnBuruk = findViewById<LinearLayout>(R.id.btnBuruk)

        val backToMenu = {
            startActivity(Intent(this, LatihanPernapasanFragment::class.java))
            finish()
        }

        btnKeren.setOnClickListener {
            saveFeedback(sessionId, "Keren")
            backToMenu()
        }

        btnBaik.setOnClickListener {
            saveFeedback(sessionId, "Baik")
            backToMenu()
        }

        btnBagus.setOnClickListener {
            saveFeedback(sessionId, "Bagus")
            backToMenu()
        }

        btnBuruk.setOnClickListener {
            saveFeedback(sessionId, "Buruk")
            backToMenu()
        }
    }

    private fun saveFeedback(sessionId: String, feedback: String) {
        FirebaseFirestore.getInstance()
            .collection("breathing_sessions")
            .document(sessionId)
            .update("feedback", feedback)
    }
}
