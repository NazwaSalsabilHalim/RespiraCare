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

class Breathing : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var circleInner: View
    private lateinit var txtStatus: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnMulai: LinearLayout

    private var phase = 0
    private var cycleCount = 0
    private val maxCycle = 1
    private var timer: CountDownTimer? = null

    // 4-7-8
    private val phases = arrayOf("Tarik napas", "Tahan", "Hembuskan")
    private val durations = arrayOf(4, 7, 8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_breathing)

        circleInner = findViewById(R.id.circleInner)
        txtStatus = findViewById(R.id.txtStatus)
        txtTimer = findViewById(R.id.txtTimer)
        btnMulai = findViewById(R.id.btnMulai)

        btnMulai.setOnClickListener {
            phase = 0
            cycleCount = 0
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
        }
    }

    private fun saveSessionAndGoToFeedback() {
        val session = hashMapOf(
            "userId" to FirebaseAuth.getInstance().currentUser?.uid,
            "exerciseType" to "breathing_4_7_8",
            "pattern" to "4-7-8",
            "totalDuration" to 19,
            "completedAt" to FieldValue.serverTimestamp(),
            "feedback" to null
        )

        db.collection("breathing_sessions")
            .add(session)
            .addOnSuccessListener { doc ->
                val intent = Intent(this, Feedback::class.java)
                intent.putExtra("sessionId", doc.id)
                startActivity(intent)
                finish()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}