package com.bangkit.konter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.konter.databinding.ActivityKartuPerdanaBinding
import com.bangkit.konter.kartu.PerdanaAxisActivity
import com.bangkit.konter.kartu.PerdanaByuActivity
import com.bangkit.konter.kartu.PerdanaIndosatActivity
import com.bangkit.konter.kartu.PerdanaSmartfrenActivity
import com.bangkit.konter.kartu.PerdanaTelkomselActivity
import com.bangkit.konter.kartu.PerdanaTriActivity
import com.bangkit.konter.kartu.PerdanaXLActivity

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
            val intent = Intent(this, PerdanaXLActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct3.setOnClickListener {
            val intent = Intent(this, PerdanaIndosatActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct4.setOnClickListener {
            val intent = Intent(this, PerdanaAxisActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct5.setOnClickListener {
            val intent = Intent(this, PerdanaTriActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct6.setOnClickListener {
            val intent = Intent(this, PerdanaSmartfrenActivity::class.java)
            startActivity(intent)  }
        binding.btnProduct7.setOnClickListener {
            val intent = Intent(this, PerdanaByuActivity::class.java)
            startActivity(intent)  }
    }


}
