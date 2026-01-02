package com.example.respiracare

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.TextView

class Reminder : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val listReminder = findViewById<LinearLayout>(R.id.listReminder)

        db.collection("reminder")
            .whereEqualTo("aktif", true)
            .get()
            .addOnSuccessListener { result ->

                listReminder.removeAllViews()

                for (doc in result) {
                    val nama = doc.getString("nama") ?: ""
                    val jam = doc.getString("jam") ?: ""

                    val item = layoutInflater.inflate(
                        R.layout.activity_item_reminder,
                        listReminder,
                        false
                    )

                    item.findViewById<TextView>(R.id.txtNama).text = nama
                    item.findViewById<TextView>(R.id.txtJam).text = jam

                    listReminder.addView(item)
                }
            }
    }
}
