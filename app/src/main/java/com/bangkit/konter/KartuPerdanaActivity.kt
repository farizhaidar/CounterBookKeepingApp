package com.bangkit.konter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.konter.databinding.ActivityKartuPerdanaBinding
import com.bangkit.konter.kartu.PerdanaTelkomselActivity
import com.bangkit.konter.voucher.AxisActivity
import com.bangkit.konter.voucher.ByuActivity
import com.bangkit.konter.voucher.IndosatActivity
import com.bangkit.konter.voucher.SmartfrenActivity
import com.bangkit.konter.voucher.TriActivity
import com.bangkit.konter.voucher.XLActivity

class KartuPerdanaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKartuPerdanaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKartuPerdanaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dummy data
        val vouchers = listOf("Telkomsel", "XL", "Indosat", "Axis", "Tri", "Smartfren", "Byu")

        // Set button text
        binding.btnProduct1.text = vouchers.getOrNull(0) ?: "N/A"
        binding.btnProduct2.text = vouchers.getOrNull(1) ?: "N/A"
        binding.btnProduct3.text = vouchers.getOrNull(2) ?: "N/A"
        binding.btnProduct4.text = vouchers.getOrNull(3) ?: "N/A"
        binding.btnProduct5.text = vouchers.getOrNull(4) ?: "N/A"
        binding.btnProduct6.text = vouchers.getOrNull(5) ?: "N/A"
        binding.btnProduct7.text = vouchers.getOrNull(6) ?: "N/A"

        // Button click listeners
        binding.btnProduct1.setOnClickListener {
            val intent = Intent(this, PerdanaTelkomselActivity::class.java)
            startActivity(intent)        }
        binding.btnProduct2.setOnClickListener {
            val intent = Intent(this, XLActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct3.setOnClickListener {
            val intent = Intent(this, IndosatActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct4.setOnClickListener {
            val intent = Intent(this, AxisActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct5.setOnClickListener {
            val intent = Intent(this, TriActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct6.setOnClickListener {
            val intent = Intent(this, SmartfrenActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct7.setOnClickListener {
            val intent = Intent(this, ByuActivity::class.java)
            startActivity(intent)  }
    }


}
