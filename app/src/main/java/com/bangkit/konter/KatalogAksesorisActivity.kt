package com.bangkit.konter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.konter.katalog.aksesoris.ChargerKatalogActivity
import com.bangkit.konter.katalog.aksesoris.HeadsetKatalogActivity
import com.bangkit.konter.katalog.aksesoris.LainnyaKatalogActivity

class KatalogAksesorisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_katalog_aksesoris)

        val btnVoucher: View = findViewById(R.id.cardHeadset)
        btnVoucher.setOnClickListener {
            val intent = Intent(this, HeadsetKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher1: View = findViewById(R.id.cardCharger)
        btnVoucher1.setOnClickListener {
            val intent = Intent(this, ChargerKatalogActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher2: View = findViewById(R.id.cardLainnya)
        btnVoucher2.setOnClickListener {
            val intent = Intent(this, LainnyaKatalogActivity::class.java)
            startActivity(intent)
        }

    }
}