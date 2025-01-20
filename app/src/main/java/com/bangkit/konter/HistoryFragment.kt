package com.bangkit.konter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        setupRecyclerView()

        firestore = FirebaseFirestore.getInstance()
        fetchHistoryData()

        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            reverseLayout = true // Membalikkan urutan layout
            stackFromEnd = true // Menempatkan item terakhir di bawah
        }
        historyAdapter = HistoryAdapter(emptyList()){ itemId ->
            deleteHistoryItem(itemId) // Panggil fungsi deleteHistoryItem dengan itemId
        }
        recyclerView.adapter = historyAdapter
    }

    private fun fetchHistoryData() {
        firestore.collection("transaksi")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val groupedHistory = mutableListOf<HistoryItem>()
                var currentDay = ""
                var dailyProfit = 0.0
                var dailySpending = 0.0

                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name") ?: "Unknown"
                    val price = document.getDouble("price") ?: 0.0
                    val costPrice = document.getDouble("cost_price") ?: 0.0
                    val profit = document.getDouble("profit") ?: 0.0
                    val timestamp = document.getTimestamp("created_at") ?: Timestamp.now()
                    val createdAt = timestamp.toDate()

                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(createdAt)
                    if (currentDay != formattedDate) {
                        if (currentDay.isNotEmpty()) {
                            groupedHistory.add(
                                0,
                                HistoryItem.Header(
                                    date = currentDay,
                                    totalProfit = dailyProfit,
                                    totalSpending = dailySpending
                                )
                            )
                        }
                        currentDay = formattedDate
                        dailyProfit = 0.0
                        dailySpending = 0.0
                    }

                    dailyProfit += profit
                    dailySpending += price
                    groupedHistory.add(
                        0,
                        HistoryItem.VoucherItem(id, name, price, costPrice, profit, timestamp)
                    )
                }

                if (currentDay.isNotEmpty()) {
                    groupedHistory.add(
                        0,
                        HistoryItem.Header(
                            date = currentDay,
                            totalProfit = dailyProfit,
                            totalSpending = dailySpending
                        )
                    )
                }

                historyAdapter.updateData(groupedHistory)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Failed to fetch history: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun deleteHistoryItem(itemId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi")
            .setMessage("Yakin mau ngapus ni?")
            .setPositiveButton("Hapus") { dialog, _ ->
                firestore.collection("transaksi")
                    .document(itemId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
                        fetchHistoryData() // Segarkan data setelah dihapus
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Gagal menghapus item: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
