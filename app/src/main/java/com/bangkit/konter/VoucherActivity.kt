package com.bangkit.konter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.konter.databinding.ActivityVoucherBinding
import com.bangkit.konter.voucher.AxisActivity
import com.bangkit.konter.voucher.ByuActivity
import com.bangkit.konter.voucher.IndosatActivity
import com.bangkit.konter.voucher.SmartfrenActivity
import com.bangkit.konter.voucher.TelkomselActivity
import com.bangkit.konter.voucher.TriActivity
import com.bangkit.konter.voucher.XLActivity

class VoucherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoucherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button click listeners
        binding.cardTelkomsel.setOnClickListener {
            val intent = Intent(this, TelkomselActivity::class.java)
            startActivity(intent)        }
        binding.cardXL.setOnClickListener {
            val intent = Intent(this, XLActivity::class.java)
            startActivity(intent)  }
        binding.cardIndosat.setOnClickListener {
            val intent = Intent(this, IndosatActivity::class.java)
            startActivity(intent)  }
        binding.cardAxis.setOnClickListener {
            val intent = Intent(this, AxisActivity::class.java)
            startActivity(intent)  }
        binding.cardTri.setOnClickListener {
            val intent = Intent(this, TriActivity::class.java)
            startActivity(intent)  }
        binding.cardSmartfren.setOnClickListener {
            val intent = Intent(this, SmartfrenActivity::class.java)
            startActivity(intent)  }
        binding.cardByu.setOnClickListener {
            val intent = Intent(this, ByuActivity::class.java)
            startActivity(intent)  }
    }


}
