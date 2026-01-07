package com.example.respiracare

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.app.AlarmManager
import android.widget.TextView

class Notifikasi : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        val id = intent.getStringExtra("ID") ?: return

        findViewById<LinearLayout>(R.id.btnSudahMinum).setOnClickListener {
            selesai(id)
        }

        findViewById<LinearLayout>(R.id.btnAbaikan).setOnClickListener {
            selesai(id)
        }

        val txtKeterangan = findViewById<TextView>(R.id.txtKeterangan)

        db.collection("reminder")
            .document(id)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val nama = doc.getString("nama") ?: ""
                    val waktu = doc.getString("waktu") ?: ""

                    txtKeterangan.text = "$nama - $waktu"
                }
            }
    }

    private fun selesai(id: String) {

        db.collection("reminder")
            .document(id)
            .update("aktif", false)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent?.let {
            alarmManager.cancel(it)
        }

        startActivity(
            Intent(this, Reminder::class.java)
        )
//        finish()
    }
}