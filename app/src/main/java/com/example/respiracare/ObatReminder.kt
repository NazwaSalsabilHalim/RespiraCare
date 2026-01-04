package com.example.respiracare.model

data class ObatReminder(
    val id: String = "",
    val nama: String = "",
    val dosis: String = "",
    val jam: String = "",
    val aktif: Boolean = true
)