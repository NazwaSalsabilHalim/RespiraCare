package com.example.respiracare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class ItemMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_menu)

        val itemTitle = findViewById<TextView>(R.id.itemTitle)
        val itemSubtitle = findViewById<TextView>(R.id.itemSubtitle)
        val itemIcon = findViewById<ImageView>(R.id.itemIcon)

        itemTitle.text = "Judul Menu Dari Kotlin"
        itemSubtitle.text = "Deskripsi dari Kotlin"
        itemIcon.setImageResource(R.drawable.ic_info)
    }
}