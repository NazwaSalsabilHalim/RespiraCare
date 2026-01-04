package com.example.respiracare.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.respiracare.BoxBreathing
import com.example.respiracare.Breathing
import com.example.respiracare.EqualBreathing
import com.example.respiracare.R

class LatihanPernapasanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.activity_latihan_pernapasan,
            container,
            false
        )

        val cardBox = view.findViewById<LinearLayout>(R.id.cardBoxBreathing)
        val cardBreathing = view.findViewById<LinearLayout>(R.id.cardBreathing)
        val cardEqual = view.findViewById<LinearLayout>(R.id.cardEqualBreathing)

        cardBox.setOnClickListener {
            startActivity(Intent(requireContext(), BoxBreathing::class.java))
        }

        cardBreathing.setOnClickListener {
            startActivity(Intent(requireContext(), Breathing::class.java))
        }

        cardEqual.setOnClickListener {
            startActivity(Intent(requireContext(), EqualBreathing::class.java))
        }

        return view
    }
}