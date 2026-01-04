package com.example.respiracare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.respiracare.R
import com.example.respiracare.model.ObatReminder

class ObatkuAdpter(
    private val list: List<ObatReminder>
) : RecyclerView.Adapter<ObatkuAdpter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaObat)
        val tvDosis: TextView = view.findViewById(R.id.tvDosis)
        val tvJam: TextView = view.findViewById(R.id.tvJam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_obat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.tvNama.text = data.nama
        holder.tvDosis.text = data.dosis
        holder.tvJam.text = data.jam
    }

    override fun getItemCount(): Int = list.size
}