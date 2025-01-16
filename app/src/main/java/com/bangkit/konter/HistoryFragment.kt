package com.bangkit.konter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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
        historyAdapter = HistoryAdapter(emptyList())
        recyclerView.adapter = historyAdapter
    }

    private fun fetchHistoryData() {
        firestore.collection("vouchers")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val groupedHistory = mutableListOf<HistoryItem>() // Data dengan header per hari
                var currentDay = ""
                var dailyProfit = 0.0

                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name") ?: "Unknown"
                    val price = document.getDouble("price") ?: 0.0
                    val costPrice = document.getDouble("cost_price") ?: 0.0
                    val profit = document.getDouble("profit") ?: 0.0
                    val createdAt = document.getString("created_at") ?: "N/A"

                    val dateOnly = createdAt.split(" ")[0]

                    if (currentDay != dateOnly) {
                        if (currentDay.isNotEmpty()) {
                            groupedHistory.add(
                                0,
                                HistoryItem.Header(currentDay, dailyProfit)
                            )
                        }
                        currentDay = dateOnly
                        dailyProfit = 0.0
                    }

                    dailyProfit += profit
                    groupedHistory.add(
                        0,
                        HistoryItem.VoucherItem(id, name, price, costPrice, profit, createdAt)
                    )
                }

                if (currentDay.isNotEmpty()) {
                    groupedHistory.add(
                        0,
                        HistoryItem.Header(currentDay, dailyProfit)
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
}
