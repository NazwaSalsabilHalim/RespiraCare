package com.example.respiracare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Reminder : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        db.collection("reminder")
            .whereEqualTo("aktif", true)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result.documents) {
                    val nama = doc.getString("nama")
                    val jam = doc.getString("jam")
                }
            }
    }
}
