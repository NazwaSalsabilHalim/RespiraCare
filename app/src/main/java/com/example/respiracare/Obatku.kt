package com.example.respiracare

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Obatku : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obatku)

        // 🔹 AMBIL VIEW
        val btnLanjutkan = findViewById<LinearLayout>(R.id.btnLanjutkan)
        val etNama = findViewById<EditText>(R.id.etNamaObat)
        val etDosis = findViewById<EditText>(R.id.etDosis)
        val etWaktu = findViewById<EditText>(R.id.etWaktu)

        // 🔹 PASTIKAN BISA DI KLIK
        btnLanjutkan.isClickable = true
        btnLanjutkan.isFocusable = true

        btnLanjutkan.setOnClickListener {

            val namaObat = etNama.text.toString().trim()
            val dosis = etDosis.text.toString().trim()
            val jam = etWaktu.text.toString().trim()

            // 🔹 VALIDASI
            if (namaObat.isEmpty() || dosis.isEmpty() || jam.isEmpty()) {
                Toast.makeText(this, "Semua data wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔹 DATA KE FIREBASE
            val reminder = hashMapOf(
                "nama" to namaObat,
                "dosis" to dosis,
                "jam" to jam,
                "aktif" to true
            )

            db.collection("reminder")
                .add(reminder)
                .addOnSuccessListener { doc ->
                    Log.d("OBATKU", "SUKSES SIMPAN: ${doc.id}")

                    setAlarm(doc.id, jam, namaObat)

                    startActivity(Intent(this@Obatku, Reminder::class.java))
                }
                .addOnFailureListener { e ->
                    Log.e("OBATKU", "GAGAL SIMPAN", e)
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setAlarm(id: String, jam: String, namaObat: String) {
        val split = jam.split(":")
        if (split.size != 2) return

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
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}