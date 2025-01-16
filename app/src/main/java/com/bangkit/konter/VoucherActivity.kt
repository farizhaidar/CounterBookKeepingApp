package com.bangkit.konter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.konter.databinding.ActivityVoucherBinding

class VoucherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoucherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dummy data
        val vouchers = listOf("Telkomsel", "XL", "Indosat")

        // Set button text
        binding.btnProduct1.text = vouchers.getOrNull(0) ?: "N/A"
        binding.btnProduct2.text = vouchers.getOrNull(1) ?: "N/A"
        binding.btnProduct3.text = vouchers.getOrNull(2) ?: "N/A"

        // Button click listeners
        binding.btnProduct1.setOnClickListener {
            val intent = Intent(this, TelkomselActivity::class.java)
            startActivity(intent)        }
        binding.btnProduct2.setOnClickListener { selectProduct(vouchers.getOrNull(1)) }
        binding.btnProduct3.setOnClickListener { selectProduct(vouchers.getOrNull(2)) }
    }

    private fun selectProduct(productName: String?) {
        productName?.let {
            Toast.makeText(this, "Selected: $it", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(this, "No product available", Toast.LENGTH_SHORT).show()
    }
}
