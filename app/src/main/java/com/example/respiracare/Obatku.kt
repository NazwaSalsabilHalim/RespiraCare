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
            val waktu = "08:00"

            val reminder = hashMapOf(
                "nama" to namaObat,
                "dosis" to dosis,
                "waktu" to waktu,
                "aktif" to true
            )

            db.collection("reminder")
                .add(reminder)
                .addOnSuccessListener { doc ->
                    setAlarm(doc.id, namaObat)
                    startActivity(Intent(this, Reminder::class.java))
                }
        }
    }

    private fun setAlarm(id: String, namaObat: String) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("nama_obat", namaObat)
        intent.putExtra("id", id)

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