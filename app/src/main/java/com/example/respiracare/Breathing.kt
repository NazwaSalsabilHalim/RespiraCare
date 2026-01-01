package com.example.respiracare

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Breathing : AppCompatActivity() {

    private lateinit var txtStatus: TextView
    private lateinit var txtTimer: TextView
    private lateinit var btnMulai: LinearLayout

    private var phase = 0
    private var timer: CountDownTimer? = null

    // Durasi 4-7-8
    private val phases = arrayOf("Tarik napas", "Tahan", "Hembuskan")
    private val durations = arrayOf(4, 7, 8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_breathing) // layout yang sama

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
        txtStatus.text = phases[phase]
        txtTimer.text = duration.toString()

        timer?.cancel()
        timer = object : CountDownTimer((duration * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                phase++
                if (phase >= phases.size) phase = 0
                startPhase()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}