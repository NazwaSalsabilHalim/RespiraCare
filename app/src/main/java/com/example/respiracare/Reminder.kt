package com.example.respiracare

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

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

                    val item = layoutInflater.inflate(
                        R.layout.activity_item_reminder,
                        container,
                        false
                    )

                    item.findViewById<TextView>(R.id.txtNama).text = nama
                    item.findViewById<TextView>(R.id.txtJam).text = waktu

                    container.addView(item)
                }
            }
    }
}