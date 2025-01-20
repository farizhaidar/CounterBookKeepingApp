package com.bangkit.konter.kartu

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
import com.bangkit.konter.R
import com.bangkit.konter.Voucher
import com.bangkit.konter.VoucherAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class PerdanaIndosatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var voucherAdapter: VoucherAdapter
    private lateinit var firestore: FirebaseFirestore

    private val voucherList = mutableListOf<Voucher>()
    private val filteredList = mutableListOf<Voucher>()
    private lateinit var etSearchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perdana_indosat)

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
        voucherAdapter = VoucherAdapter(filteredList) { voucher ->
            showVoucherDialog(voucher)
        }
        recyclerView.adapter = voucherAdapter
    }

    private fun fetchVouchersFromFirestore() {
        firestore.collection("k.indosat")
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
            val id = doc.id // Ambil ID dokumen
            val name = doc.getString("name") ?: return@mapNotNull null
            val sellingPrice = doc.getDouble("sellingPrice") ?: return@mapNotNull null
            val costPrice = doc.getDouble("costPrice") ?: return@mapNotNull null // Pastikan ini bertipe Double
            Voucher(id, name, sellingPrice, costPrice) // Pastikan parameter sesuai
        }
        voucherList.clear()
        voucherList.addAll(voucherItems)
        filterAndSortVouchers("")
    }

    private fun filterAndSortVouchers(query: String?) {
        val input = query?.lowercase() ?: ""

        val filteredVouchers = voucherList.filter { it.name.lowercase().contains(input) }

        // Prioritizing "days" first, then "GB size"
        val sortedVouchers = filteredVouchers.sortedWith(compareBy(
            { extractDays(it.name) }, // Prioritize by days
            { extractNumericValue(it.name) } // Then by numeric value (GB)
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
                val totalPrice = voucher.sellingPrice * quantity
                val totalProfit = calculateProfit(voucher.sellingPrice, voucher.costPrice) * quantity

                // Gunakan Timestamp dari tanggal saat ini
                val currentTime = Timestamp.now()

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
        firestore.collection("transaksi")
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
