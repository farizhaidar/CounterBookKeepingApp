package com.bangkit.konter.katalog.voucher

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ByuKatalogActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var katalogAdapter: KatalogAdapter
    private lateinit var firestore: FirebaseFirestore

    private val voucherList = mutableListOf<Voucher>()
    private val filteredList = mutableListOf<Voucher>()
    private lateinit var etSearchBar: EditText
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telkomsel_katalog)

        firestore = FirebaseFirestore.getInstance()
        etSearchBar = findViewById(R.id.etSearchBar)

        recyclerView = findViewById(R.id.recyclerView)
        tvTitle = findViewById(R.id.tvTitle)

        tvTitle.text = "Byu"
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
                    putExtra("collectionName", "v.byu")  // Kirimkan koleksi
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
        val telkomselTask = firestore.collection("v.byu").get()

        telkomselTask
            .addOnSuccessListener { telkomselData ->
                populateVoucherList(telkomselData.documents)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        fetchVouchersFromFirestore()  // Mengambil data dari firestore saat kembali ke halaman
    }

    private fun populateVoucherList(documents: List<DocumentSnapshot>) {
        val voucherItems = documents.mapNotNull { doc ->
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

    private fun deleteVoucherFromFirestore(voucherId: String) {
        firestore.collection("v.byu")
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

}
