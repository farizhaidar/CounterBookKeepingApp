package com.bangkit.konter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class KatalogFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_katalog, container, false)

        // Tombol Voucher
        val btnVoucher: View = view.findViewById(R.id.cardVoucher)
        btnVoucher.setOnClickListener {
            val intent = Intent(requireContext(), KatalogVoucherActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher1: View = view.findViewById(R.id.cardKartuPerdana)
        btnVoucher1.setOnClickListener {
            val intent = Intent(requireContext(), KatalogKartuPerdanaActivity::class.java)
            startActivity(intent)
        }

        val btnVoucher2: View = view.findViewById(R.id.cardAksesoris)
        btnVoucher2.setOnClickListener {
            val intent = Intent(requireContext(), KatalogAksesorisActivity::class.java)
            startActivity(intent)
        }

        val fabAddProduct: FloatingActionButton = view.findViewById(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
