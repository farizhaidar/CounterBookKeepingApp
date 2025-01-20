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
        // Inflate layout
        val view = inflater.inflate(R.layout.fragment_katalog, container, false)

        // Tombol Voucher
        val btnVoucher: View = view.findViewById(R.id.cardVoucher) // Access with view.findViewById
        btnVoucher.setOnClickListener {
            val intent = Intent(requireContext(), KatalogVoucherActivity::class.java)
            startActivity(intent)
        }

        // FloatingActionButton untuk menambahkan data baru
        val fabAddProduct: FloatingActionButton = view.findViewById(R.id.fabAddProduct) // Access with view.findViewById
        fabAddProduct.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
