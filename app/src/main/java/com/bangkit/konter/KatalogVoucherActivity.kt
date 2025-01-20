package com.bangkit.konter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.konter.katalog.voucher.IndosatKatalogActivity
import com.bangkit.konter.katalog.voucher.TelkomselKatalogActivity
import com.bangkit.konter.katalog.voucher.XLKatalogActivity

class KatalogVoucherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_katalog_voucher)

        // Tombol Voucher
        val btnVoucher: View = findViewById(R.id.cardTelkomsel)
        btnVoucher.setOnClickListener {
            val intent = Intent(this, TelkomselKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher1: View = findViewById(R.id.cardXL)
        btnVoucher1.setOnClickListener {
            val intent = Intent(this, XLKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher2: View = findViewById(R.id.cardIndosat)
        btnVoucher2.setOnClickListener {
            val intent = Intent(this, IndosatKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher3: View = findViewById(R.id.cardXL)
        btnVoucher3.setOnClickListener {
            val intent = Intent(this, XLKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher4: View = findViewById(R.id.cardXL)
        btnVoucher4.setOnClickListener {
            val intent = Intent(this, XLKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher5: View = findViewById(R.id.cardXL)
        btnVoucher5.setOnClickListener {
            val intent = Intent(this, XLKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher6: View = findViewById(R.id.cardXL)
        btnVoucher6.setOnClickListener {
            val intent = Intent(this, XLKatalogActivity::class.java)
            startActivity(intent)
        }
    }
}
