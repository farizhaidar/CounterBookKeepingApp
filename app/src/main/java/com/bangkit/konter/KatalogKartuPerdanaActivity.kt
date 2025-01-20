package com.bangkit.konter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.konter.katalog.perdana.AxisKartuKatalogActivity
import com.bangkit.konter.katalog.perdana.ByuKartuKatalogActivity
import com.bangkit.konter.katalog.perdana.IndosatKartuKatalogActivity
import com.bangkit.konter.katalog.perdana.SmartfrenKartuKatalogActivity
import com.bangkit.konter.katalog.perdana.TelkomselKartuKatalogActivity
import com.bangkit.konter.katalog.perdana.TriKartuKatalogActivity
import com.bangkit.konter.katalog.perdana.XLKartuKatalogActivity

class KatalogKartuPerdanaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_katalog_voucher)

        // Tombol Voucher
        val btnVoucher: View = findViewById(R.id.cardTelkomsel)
        btnVoucher.setOnClickListener {
            val intent = Intent(this, TelkomselKartuKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher1: View = findViewById(R.id.cardXL)
        btnVoucher1.setOnClickListener {
            val intent = Intent(this, XLKartuKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher2: View = findViewById(R.id.cardIndosat)
        btnVoucher2.setOnClickListener {
            val intent = Intent(this, IndosatKartuKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher3: View = findViewById(R.id.cardAxis)
        btnVoucher3.setOnClickListener {
            val intent = Intent(this, AxisKartuKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher4: View = findViewById(R.id.cardTri)
        btnVoucher4.setOnClickListener {
            val intent = Intent(this, TriKartuKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher5: View = findViewById(R.id.cardSmartfren)
        btnVoucher5.setOnClickListener {
            val intent = Intent(this, SmartfrenKartuKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher6: View = findViewById(R.id.cardByu)
        btnVoucher6.setOnClickListener {
            val intent = Intent(this, ByuKartuKatalogActivity::class.java)
            startActivity(intent)
        }
    }
}
