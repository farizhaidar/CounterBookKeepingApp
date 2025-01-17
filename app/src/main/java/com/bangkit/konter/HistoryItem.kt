package com.bangkit.konter

sealed class HistoryItem {
    data class Header(val date: String, val totalProfit: Double,val totalSpending: Double
    ) : HistoryItem()
    data class VoucherItem(
        val id: String,
        val name: String,
        val price: Double,
        val costPrice: Double,
        val profit: Double,
        val createdAt: String
    ) : HistoryItem()
}


