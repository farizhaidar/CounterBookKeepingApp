package com.bangkit.konter

data class Voucher(
    val id: String = "",
    val name: String,
    val sellingPrice: Double,
    val costPrice: Double // Pastikan tipe data sesuai dengan data di Firestore
)

