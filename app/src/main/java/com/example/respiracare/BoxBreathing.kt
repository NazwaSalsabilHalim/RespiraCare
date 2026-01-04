package com.example.respiracare

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class BoxBreathing : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var sessionId: String
    private lateinit var circleInner: View
    private var cycleCount = 0
    private val maxCycle = 1 // 1x putaran 4-4-4-4
    private lateinit var txtStatus: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnMulai: LinearLayout

    private var phase = 0
    private var timer: CountDownTimer? = null

    // Durasi Box Breathing 4-4-4-4
    private val phases = arrayOf("Tarik napas", "Tahan", "Hembuskan", "Tahan")
    private val durations = arrayOf(4, 4, 4, 4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_breathing)

        circleInner = findViewById(R.id.circleInner)

        txtStatus = findViewById(R.id.txtStatus)
        txtTimer = findViewById(R.id.txtTimer)
        btnMulai = findViewById(R.id.btnMulai)

        btnMulai.setOnClickListener {
            phase = 0
            startPhase()
        }
    }

    private fun startPhase() {
        val duration = durations[phase]
        val currentPhase = phases[phase]

        txtStatus.text = currentPhase
        txtTimer.text = duration.toString()

        animateCircle(currentPhase, duration)

        timer?.cancel()
        timer = object : CountDownTimer((duration * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                phase++

                if (phase >= phases.size) {
                    cycleCount++
                    phase = 0

                    if (cycleCount >= maxCycle) {
                        saveSessionAndGoToFeedback()
                        return
                    }
                }

                startPhase()
            }
        }.start()
    }

    private fun animateCircle(type: String, duration: Int) {
        val scaleUp = 1.4f
        val scaleNormal = 1.0f

        when (type) {
            "Tarik napas" -> {
                circleInner.animate()
                    .scaleX(scaleUp)
                    .scaleY(scaleUp)
                    .setDuration((duration * 1000).toLong())
                    .start()
            }

            "Hembuskan" -> {
                circleInner.animate()
                    .scaleX(scaleNormal)
                    .scaleY(scaleNormal)
                    .setDuration((duration * 1000).toLong())
                    .start()
            }

            "Tahan" -> {
                // Tidak ada animasi, tetap di ukuran sekarang
            }
        }
    }

    private fun saveSessionAndGoToFeedback() {
        val session = hashMapOf(
            "userId" to FirebaseAuth.getInstance().currentUser?.uid,
            "exerciseType" to "box_breathing",
            "pattern" to "4-4-4-4",
            "totalDuration" to 16,
            "completedAt" to FieldValue.serverTimestamp(),
            "feedback" to null
        )

        db.collection("breathing_sessions")
            .add(session)
            .addOnSuccessListener { doc ->
                sessionId = doc.id

                val intent = Intent(this, Feedback::class.java)
                intent.putExtra("sessionId", sessionId)
                startActivity(intent)
                finish()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}