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

        // Button click listeners
        binding.cardTelkomsel.setOnClickListener {
            val intent = Intent(this, PerdanaTelkomselActivity::class.java)
            startActivity(intent)        }
        binding.cardXL.setOnClickListener {
            val intent = Intent(this, PerdanaXLActivity::class.java)
            startActivity(intent)  }
        binding.cardIndosat.setOnClickListener {
            val intent = Intent(this, PerdanaIndosatActivity::class.java)
            startActivity(intent)  }
        binding.cardAxis.setOnClickListener {
            val intent = Intent(this, PerdanaAxisActivity::class.java)
            startActivity(intent)  }
        binding.cardTri.setOnClickListener {
            val intent = Intent(this, PerdanaTriActivity::class.java)
            startActivity(intent)  }
        binding.cardSmartfren.setOnClickListener {
            val intent = Intent(this, PerdanaSmartfrenActivity::class.java)
            startActivity(intent)  }
        binding.cardByu.setOnClickListener {
            val intent = Intent(this, PerdanaByuActivity::class.java)
            startActivity(intent)  }
    }


}
