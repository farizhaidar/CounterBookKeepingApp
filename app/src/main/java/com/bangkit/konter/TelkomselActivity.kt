package com.bangkit.konter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TelkomselActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var voucherAdapter: VoucherAdapter
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var firestore: FirebaseFirestore

    private val voucherList = mutableListOf<Voucher>()
    private val filteredList = mutableListOf<Voucher>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telkomsel)

        firestore = FirebaseFirestore.getInstance()

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        setupRecyclerView()
        setupSearchView()
        fetchVouchersFromFirestore()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        voucherAdapter = VoucherAdapter(filteredList) { voucher ->
            showVoucherDialog(voucher)
        }
        recyclerView.adapter = voucherAdapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterAndSortVouchers(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterAndSortVouchers(newText)
                return true
            }
        })
    }

    private fun fetchVouchersFromFirestore() {
        firestore.collection("v.telkomsel")
            .get()
            .addOnSuccessListener { querySnapshot ->
                populateVoucherList(querySnapshot)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun populateVoucherList(querySnapshot: QuerySnapshot) {
        val voucherItems = querySnapshot.documents.mapNotNull { doc ->
            val name = doc.getString("name") ?: return@mapNotNull null
            val sellingPrice = doc.getDouble("sellingPrice") ?: return@mapNotNull null
            val costPrice = doc.getDouble("costPrice") ?: return@mapNotNull null
            Voucher(name, sellingPrice, costPrice)
        }
        voucherList.clear()
        voucherList.addAll(voucherItems)
        filterAndSortVouchers("")
    }

    private fun filterAndSortVouchers(query: String?) {
        val input = query?.lowercase() ?: ""

        val filteredVouchers = voucherList.filter { it.name.lowercase().contains(input) }

        val sortedVouchers = filteredVouchers.sortedWith(compareBy(
            { extractNumericValue(it.name) },
            { extractDays(it.name) }
        ))

        filteredList.clear()
        filteredList.addAll(sortedVouchers)
        voucherAdapter.notifyDataSetChanged()
    }

    private fun extractNumericValue(name: String): Double {
        val cleanedName = name.lowercase().replace(",", ".")
        val regex = Regex("([0-9]+(?:\\.[0-9]+)?)\\s*gb")
        return regex.find(cleanedName)?.groups?.get(1)?.value?.toDoubleOrNull() ?: 0.0
    }

    private fun extractDays(name: String): Int {
        val regex = Regex("\\b([0-9]+)\\s*hari\\b")
        return regex.find(name.lowercase())?.groups?.get(1)?.value?.toIntOrNull() ?: Int.MAX_VALUE
    }

    private fun setupQuantityButtons(tvQuantity: TextView, btnIncrease: View, btnDecrease: View) {
        var quantity = 1

        btnIncrease.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
        }

        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
            }
        }
    }

    private fun showVoucherDialog(voucher: Voucher) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_quantity_input, null)
        val tvQuantity = dialogView.findViewById<TextView>(R.id.tvQuantity)
        val btnIncrease = dialogView.findViewById<Button>(R.id.btnIncrease)
        val btnDecrease = dialogView.findViewById<Button>(R.id.btnDecrease)

        setupQuantityButtons(tvQuantity, btnIncrease, btnDecrease)

        AlertDialog.Builder(this)
            .setTitle("Input Quantity")
            .setView(dialogView)
            .setPositiveButton("Simpen") { _, _ ->
                val quantity = tvQuantity.text.toString().toInt()

                val totalPrice = voucher.price * quantity
                val totalProfit = calculateProfit(voucher.price, voucher.costPrice) * quantity

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentTime = dateFormat.format(Date())

                val voucherData = mapOf(
                    "name" to voucher.name.trim(),
                    "price" to totalPrice,
                    "cost_price" to voucher.costPrice * quantity,
                    "profit" to totalProfit,
                    "quantity" to quantity,
                    "created_at" to currentTime
                )

                saveVoucherToFirestore(voucherData)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun saveVoucherToFirestore(voucherData: Map<String, Any>) {
        firestore.collection("vouchers")
            .add(voucherData)
            .addOnSuccessListener {
                Toast.makeText(this, "Data disimpenn!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save voucher: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun calculateProfit(sellingPrice: Double, costPrice: Double): Double {
        return sellingPrice - costPrice
    }
}
