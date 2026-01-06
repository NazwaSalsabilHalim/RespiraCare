package com.example.respiracare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.respiracare.model.ObatReminder

class ObatkuAdpter(
    private val list: MutableList<ObatReminder>
) : RecyclerView.Adapter<ObatkuAdpter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNama: TextView = view.findViewById(R.id.txtNamaObat)
        val txtDosis: TextView = view.findViewById(R.id.txtDosis)
        val txtWaktu: TextView = view.findViewById(R.id.txtWaktu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_obat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.txtNama.text = data.nama
        holder.txtDosis.text = data.dosis
        holder.txtWaktu.text = data.waktu
    }

    override fun getItemCount(): Int = list.size
}