package com.example.respiracare

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.content.res.ColorStateList
import android.widget.Switch
import androidx.core.content.ContextCompat

class Reminder : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val listReminder = findViewById<LinearLayout>(R.id.listReminder)
        loadReminder(listReminder)
    }

    private fun loadReminder(container: LinearLayout) {
        db.collection("reminder")
            .whereEqualTo("aktif", true)
            .get()
            .addOnSuccessListener { result ->

                container.removeAllViews()

                for (doc in result) {
                    val nama = doc.getString("nama") ?: ""
                    val waktu = doc.getString("waktu") ?: ""

                    // 1️⃣ inflate item
                    val item = layoutInflater.inflate(
                        R.layout.activity_item_reminder,
                        container,
                        false
                    )

                    // 2️⃣ set text
                    item.findViewById<TextView>(R.id.txtNama).text = nama
                    item.findViewById<TextView>(R.id.txtJam).text = waktu

                    // 3️⃣ ambil switch DARI ITEM
                    val switchAktif = item.findViewById<Switch>(R.id.switchAktif)

                    // 4️⃣ atur warna track (ON biru tua, OFF putih)
                    val trackColors = ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_checked),
                            intArrayOf(android.R.attr.state_checked)
                        ),
                        intArrayOf(
                            ContextCompat.getColor(this, android.R.color.white),
                            ContextCompat.getColor(this, R.color.blue_dark)
                        )
                    )
                    switchAktif.trackTintList = trackColors

                    // 5️⃣ BARU tampilkan item ke layar
                    container.addView(item)
                }
            }
    }
}