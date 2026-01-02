package com.example.respiracare

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Obatku : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obatku)

        val btnLanjutkan = findViewById<LinearLayout>(R.id.btnLanjutkan)

        btnLanjutkan.setOnClickListener {

            val namaObat = "Paracetamol"
            val dosis = "3x sehari"
            val jam = "08:00"

            val reminder = hashMapOf(
                "nama" to namaObat,
                "dosis" to dosis,
                "jam" to jam,
                "aktif" to true
            )

            db.collection("reminder")
                .add(reminder)
                .addOnSuccessListener { doc ->
                    setAlarm(doc.id, jam, namaObat)

                    startActivity(
                        Intent(this, Reminder::class.java)
                    )
                }
        }
    }

    private fun setAlarm(id: String, jam: String, namaObat: String) {
        val split = jam.split(":")
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, split[0].toInt())
            set(Calendar.MINUTE, split[1].toInt())
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("ID", id)
            putExtra("NAMA", namaObat)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}