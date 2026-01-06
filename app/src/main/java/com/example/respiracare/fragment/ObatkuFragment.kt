package com.example.respiracare.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.respiracare.R
import com.example.respiracare.ReminderReceiver
import com.example.respiracare.model.ObatReminder
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ObatkuFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

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
        val btnSimpan = view.findViewById<LinearLayout>(R.id.btnSimpanObat)

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val dosis = etDosis.text.toString().trim()
            val waktu = etWaktu.text.toString().trim() // format HH:mm

            if (nama.isEmpty() || dosis.isEmpty() || waktu.isEmpty()) {
                Toast.makeText(requireContext(), "Lengkapi data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // simpan ke Firestore
            val doc = db.collection("reminder").document()
            val obat = hashMapOf(
                "id" to doc.id,
                "nama" to nama,
                "dosis" to dosis,
                "waktu" to waktu,
                "aktif" to true
            )

            doc.set(obat).addOnSuccessListener {
                Toast.makeText(requireContext(), "Obat tersimpan!", Toast.LENGTH_SHORT).show()
                etNama.text.clear()
                etDosis.text.clear()
                etWaktu.text.clear()

                // set alarm sesuai waktu
                setAlarm(doc.id, nama, waktu)

                // buka halaman Reminder
                startActivity(Intent(requireContext(), com.example.respiracare.Reminder::class.java))
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal simpan: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAlarm(id: String, nama: String, waktu: String) {
        val (h, m) = waktu.split(":").map { it.toInt() }
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, h)
            set(Calendar.MINUTE, m)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(requireContext(), ReminderReceiver::class.java).apply {
            putExtra("ID", id)
            putExtra("NAMA", nama)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
    }
}
