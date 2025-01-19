package com.bangkit.konter.katalog.voucher

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.konter.KatalogAdapter
import com.bangkit.konter.R
import com.bangkit.konter.Voucher
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TelkomselKatalogActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var katalogAdapter: KatalogAdapter
    private lateinit var firestore: FirebaseFirestore

    private val voucherList = mutableListOf<Voucher>()
    private val filteredList = mutableListOf<Voucher>()
    private lateinit var etSearchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telkomsel_katalog)

        firestore = FirebaseFirestore.getInstance()
        etSearchBar = findViewById(R.id.etSearchBar)

        recyclerView = findViewById(R.id.recyclerView)

        etSearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterAndSortVouchers(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        setupRecyclerView()
        fetchVouchersFromFirestore()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        katalogAdapter = KatalogAdapter(filteredList,
            onEditClicked = { voucher ->
                // Navigasi ke halaman update
                val intent = Intent(this, UpdateVoucherActivity::class.java).apply {
                    putExtra("voucherId", voucher.id)
                    putExtra("name", voucher.name)
                    putExtra("sellingPrice", voucher.sellingPrice)
                    putExtra("costPrice", voucher.costPrice)
                }
                startActivity(intent)
            },
            onDeleteClicked = { voucher ->
                // Konfirmasi hapus
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus voucher ini?")
                    .setPositiveButton("Ya") { _, _ ->
                        deleteVoucherFromFirestore(voucher.id)
                    }
                    .setNegativeButton("Tidak", null)
                    .show()
            }
        )
        recyclerView.adapter = katalogAdapter
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
            val id = doc.id
            val name = doc.getString("name") ?: return@mapNotNull null
            val sellingPrice = doc.getDouble("sellingPrice") ?: return@mapNotNull null
            val costPrice = doc.getDouble("costPrice") ?: return@mapNotNull null
            Voucher(id, name, sellingPrice, costPrice)
        }
        voucherList.clear()
        voucherList.addAll(voucherItems)
        filterAndSortVouchers("")
    }

    private fun filterAndSortVouchers(query: String?) {
        val input = query?.lowercase() ?: ""

        val filteredVouchers = voucherList.filter { it.name.lowercase().contains(input) }

        val sortedVouchers = filteredVouchers.sortedWith(compareBy(
            { extractDays(it.name) },
            { extractNumericValue(it.name) }
        ))

        filteredList.clear()
        filteredList.addAll(sortedVouchers)
        katalogAdapter.notifyDataSetChanged()
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
            .setPositiveButton("Update") { _, _ ->
                val quantity = tvQuantity.text.toString().toInt()

                val totalPrice = voucher.sellingPrice * quantity
                val totalProfit = calculateProfit(voucher.sellingPrice, voucher.costPrice) * quantity

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentTime = dateFormat.format(Date())

                val voucherData = mapOf(
                    "name" to voucher.name.trim(),
                    "price" to totalPrice,
                    "cost_price" to voucher.costPrice * quantity,
                    "profit" to totalProfit,
                    "quantity" to quantity,
                    "updated_at" to currentTime
                )

                updateVoucherInFirestore(voucher.id, voucherData)
            }
            .setNegativeButton("Delete") { _, _ ->
                deleteVoucherFromFirestore(voucher.id)
            }
            .setNeutralButton("Cancel", null)
            .show()
    }

    private fun updateVoucherInFirestore(voucherId: String, voucherData: Map<String, Any>) {
        firestore.collection("v.telkomsel")
            .document(voucherId)
            .update(voucherData)
            .addOnSuccessListener {
                Toast.makeText(this, "Voucher updated successfully!", Toast.LENGTH_SHORT).show()
                fetchVouchersFromFirestore()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update voucher: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteVoucherFromFirestore(voucherId: String) {
        firestore.collection("v.telkomsel")
            .document(voucherId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Voucher deleted successfully!", Toast.LENGTH_SHORT).show()
                fetchVouchersFromFirestore()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to delete voucher: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun calculateProfit(sellingPrice: Double, costPrice: Double): Double {
        return sellingPrice - costPrice
    }
}
