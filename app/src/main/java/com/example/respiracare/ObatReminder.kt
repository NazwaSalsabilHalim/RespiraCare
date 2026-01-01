package com.example.respiracare

data class ObatReminder(
    val nama: String = "",
    val dosis: String = "",
    val waktu: String = "",
    val aktif: Boolean = true
)