package com.example.respiracare.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.respiracare.Informasi
import com.example.respiracare.Intro
import com.example.respiracare.R
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)

        val containerCard = view.findViewById<LinearLayout>(R.id.containerCard)

        val menuDiagnosa = containerCard.getChildAt(0) as MaterialCardView
        val menuObatku = containerCard.getChildAt(1) as MaterialCardView
        val menuInformasi = containerCard.getChildAt(2) as MaterialCardView
        val menuTentang = containerCard.getChildAt(3) as MaterialCardView

        // Menu Diagnosa → Activity
        menuDiagnosa.setOnClickListener {
            startActivity(Intent(requireContext(), Intro::class.java))
        }

        // Menu Obatku → Fragment
        menuObatku.setOnClickListener {
            val fragment = ObatkuFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Menu Latihan Pernapasan → Fragment
        menuInformasi.setOnClickListener {
            val fragment = LatihanPernapasanFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Menu Tentang → Activity
        menuTentang.setOnClickListener {
            startActivity(Intent(requireContext(), Informasi::class.java))
        }

        return view
    }
}