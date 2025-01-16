package com.bangkit.konter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TelkomselActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var voucherAdapter: VoucherAdapter
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var firestore: FirebaseFirestore

    // Sample data
    private val voucherList = listOf(
        Voucher("Telkomsel 1GB", "10000", 8000.0),
        Voucher("Telkomsel 5GB", "25000", 20000.0),
        Voucher("Telkomsel 10GB", "50000", 40000.0),
        Voucher("Telkomsel Unlimited 1 Day", "5000", 3000.0),
        Voucher("Telkomsel Unlimited 7 Days", "35000", 30000.0)
    )

    // Filtered data
    private var filteredList = voucherList.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telkomsel)

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance()

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        setupRecyclerView()
        setupSearchView()
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
                filterVouchers(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterVouchers(newText)
                return true
            }
        })
    }

    private fun filterVouchers(query: String?) {
        val input = query?.lowercase() ?: ""
        filteredList.clear()
        filteredList.addAll(voucherList.filter { it.name.lowercase().contains(input) })
        voucherAdapter.notifyDataSetChanged()

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No vouchers found", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showVoucherDialog(voucher: Voucher) {
        val sellingPrice = voucher.price.toDoubleOrNull()
        if (sellingPrice == null) {
            Toast.makeText(this, "Invalid price format!", Toast.LENGTH_SHORT).show()
            return
        }

        // Hitung keuntungan menggunakan harga modal statis
        val profit = calculateProfit(sellingPrice, voucher.costPrice)

        // Format waktu
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())

        // Data voucher
        val voucherData = mapOf(
            "name" to voucher.name.trim(),
            "price" to sellingPrice,
            "cost_price" to voucher.costPrice,
            "profit" to profit,
            "created_at" to currentTime
        )

        // Simpan ke Firestore
        showConfirmationDialog(voucherData)
    }

    private fun showConfirmationDialog(voucherData: Map<String, Any>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Save")
        builder.setMessage("Input Data Voucher ${voucherData["name"]} ini?" )

        builder.setPositiveButton("Iya dongg") { _, _ ->
            saveVoucherToFirestore(voucherData)
        }

        builder.setNegativeButton("Enggak ah") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
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

    // Fungsi untuk menghitung keuntungan
    private fun calculateProfit(sellingPrice: Double, costPrice: Double): Double {
        return sellingPrice - costPrice
    }
}
