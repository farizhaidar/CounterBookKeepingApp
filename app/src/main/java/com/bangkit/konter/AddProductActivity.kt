package com.bangkit.konter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerSecond: Spinner
    private lateinit var etProductName: EditText
    private lateinit var etCostPrice: EditText
    private lateinit var etSellingPrice: EditText
    private lateinit var btnSave: Button
    private lateinit var tvSecondLabel: TextView

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Initialize views
        spinnerCategory = findViewById(R.id.spinnerCategory)
        spinnerSecond = findViewById(R.id.spinnerSecond)
        etProductName = findViewById(R.id.etProductName)
        etCostPrice = findViewById(R.id.etCostPrice)
        etSellingPrice = findViewById(R.id.etSellingPrice)
        btnSave = findViewById(R.id.btnSave)
        tvSecondLabel = findViewById(R.id.tvSecondLabel)

        // Set up category spinner
        val categories = arrayOf("Voucher", "Kartu Perdana", "Aksesoris")
        val categoryAdapter = ArrayAdapter(
            this,
            R.layout.spinner_item, // Custom layout for Spinner items
            categories
        )
        categoryAdapter.setDropDownViewResource(R.layout.spinner_item) // Dropdown layout
        spinnerCategory.adapter = categoryAdapter

        // Listen for category selection
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSecondSpinner(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Save button click
        btnSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun updateSecondSpinner(category: String) {
        when (category) {
            "Voucher", "Kartu Perdana" -> {
                tvSecondLabel.text = "Merk"
                val brands = arrayOf("Telkomsel", "Indosat", "XL", "Axis", "Smartfren", "Tri", "Byu")
                val brandAdapter = ArrayAdapter(
                    this,
                    R.layout.spinner_item, // Custom layout for Spinner items
                    brands
                )
                brandAdapter.setDropDownViewResource(R.layout.spinner_item)
                spinnerSecond.adapter = brandAdapter
            }
            "Aksesoris" -> {
                tvSecondLabel.text = "Tipe"
                val types = arrayOf("Headset", "Charger", "Lainnya")
                val typeAdapter = ArrayAdapter(
                    this,
                    R.layout.spinner_item, // Custom layout for Spinner items
                    types
                )
                typeAdapter.setDropDownViewResource(R.layout.spinner_item)
                spinnerSecond.adapter = typeAdapter
            }
        }
    }

    private fun saveProduct() {
        val category = spinnerCategory.selectedItem.toString()
        val secondValue = spinnerSecond.selectedItem.toString()
        val productName = etProductName.text.toString().trim()
        val costPrice = etCostPrice.text.toString().toDoubleOrNull()
        val sellingPrice = etSellingPrice.text.toString().toDoubleOrNull()

        if (productName.isEmpty() || costPrice == null || sellingPrice == null) {
            Toast.makeText(this, "Isi semua data dengan benar", Toast.LENGTH_SHORT).show()
            return
        }

        val collectionName = when (category) {
            "Voucher" -> "v.${secondValue.lowercase()}"
            "Kartu Perdana" -> "k.${secondValue.lowercase()}"
            "Aksesoris" -> "${secondValue.lowercase()}"
            else -> null
        }

        if (collectionName != null) {
            val productData = mapOf(
                "name" to productName,
                "costPrice" to costPrice,
                "sellingPrice" to sellingPrice,
                "category" to category,
                "typeOrBrand" to secondValue
            )

            firestore.collection(collectionName)
                .add(productData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Produk berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke halaman sebelumnya
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan produk: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
