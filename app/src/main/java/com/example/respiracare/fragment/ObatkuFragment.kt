package com.example.respiracare.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.respiracare.ObatkuAdpter
import com.example.respiracare.R
import com.example.respiracare.ReminderReceiver
import com.example.respiracare.model.ObatReminder
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class ObatkuFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val listObat = mutableListOf<ObatReminder>()
    private lateinit var adapter: ObatkuAdpter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_obatku, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etNama = view.findViewById<EditText>(R.id.etNamaObat)
        val etDosis = view.findViewById<EditText>(R.id.etDosis)
        val etWaktu = view.findViewById<EditText>(R.id.etWaktu)
        val btnSimpan = view.findViewById<LinearLayout>(R.id.btnLanjutkan)
        val container = view.findViewById<LinearLayout>(R.id.containerCard)

        // 🔹 Buat RecyclerView programmatically
        val rv = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ObatkuAdpter(listObat)
        }
        container.addView(rv) // tambahkan ke bawah containerCard
        adapter = rv.adapter as ObatkuAdpter

        loadData()

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString()
            val dosis = etDosis.text.toString()
            val jam = etWaktu.text.toString()

            if (nama.isEmpty() || dosis.isEmpty() || jam.isEmpty()) {
                Toast.makeText(requireContext(), "Lengkapi data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val doc = db.collection("reminder").document()
            val data = ObatReminder(
                id = doc.id,
                nama = nama,
                dosis = dosis,
                jam = jam,
                aktif = true
            )

            doc.set(data).addOnSuccessListener {
                setAlarm(data)
                listObat.add(data)
                adapter.notifyItemInserted(listObat.size - 1)
                etNama.text.clear()
                etDosis.text.clear()
                etWaktu.text.clear()
            }
        }
    }

    private fun loadData() {
        db.collection("reminder")
            .whereEqualTo("aktif", true)
            .get()
            .addOnSuccessListener { result ->
                listObat.clear()
                for (doc in result) {
                    listObat.add(
                        ObatReminder(
                            id = doc.id,
                            nama = doc.getString("nama") ?: "",
                            dosis = doc.getString("dosis") ?: "",
                            jam = doc.getString("jam") ?: "",
                            aktif = doc.getBoolean("aktif") ?: true
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun setAlarm(data: ObatReminder) {
        val (h, m) = data.jam.split(":").map { it.toInt() }
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, h)
            set(Calendar.MINUTE, m)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(requireContext(), ReminderReceiver::class.java).apply {
            putExtra("ID", data.id)
            putExtra("NAMA", data.nama)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            data.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            requireContext().getSystemService(AlarmManager::class.java)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            pendingIntent
        )
    }
}