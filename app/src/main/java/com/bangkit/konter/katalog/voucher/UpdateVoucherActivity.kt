package com.bangkit.konter.katalog.voucher

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.konter.R
import com.google.firebase.firestore.FirebaseFirestore

class UpdateVoucherActivity : AppCompatActivity() {

    private lateinit var etVoucherName: EditText
    private lateinit var etSellingPrice: EditText
    private lateinit var etCostPrice: EditText
    private lateinit var btnUpdateVoucher: Button
    private lateinit var btnCancel: Button

    private lateinit var firestore: FirebaseFirestore
    private var voucherId: String? = null
    private var collectionName: String? = null  // Menyimpan nama koleksi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_voucher)

        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi view
        etVoucherName = findViewById(R.id.etVoucherName)
        etSellingPrice = findViewById(R.id.etSellingPrice)
        etCostPrice = findViewById(R.id.etCostPrice)
        btnUpdateVoucher = findViewById(R.id.btnUpdateVoucher)

        // Ambil data dari intent
        voucherId = intent.getStringExtra("voucherId")
        val name = intent.getStringExtra("name")
        val sellingPrice = intent.getDoubleExtra("sellingPrice", 0.0)
        val costPrice = intent.getDoubleExtra("costPrice", 0.0)
        collectionName = intent.getStringExtra("collectionName") // Koleksi dari Intent

        // Tampilkan data di EditText
        etVoucherName.setText(name)
        etSellingPrice.setText(sellingPrice.toString())
        etCostPrice.setText(costPrice.toString())

        // Aksi tombol update
        btnUpdateVoucher.setOnClickListener {
            if (collectionName != null) {
                updateVoucher(collectionName!!)
            } else {
                Toast.makeText(this, "Collection name is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateVoucher(collection: String) {
        val name = etVoucherName.text.toString().trim()
        val sellingPrice = etSellingPrice.text.toString().toDoubleOrNull()
        val costPrice = etCostPrice.text.toString().toDoubleOrNull()

        if (name.isEmpty() || sellingPrice == null || costPrice == null) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        // Data yang akan diupdate
        val updatedData = mapOf(
            "name" to name,
            "sellingPrice" to sellingPrice,
            "costPrice" to costPrice
        )

        // Update ke Firestore
        voucherId?.let {
            firestore.collection(collection)  // Gunakan koleksi dari parameter
                .document(it)
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Voucher updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update voucher: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
