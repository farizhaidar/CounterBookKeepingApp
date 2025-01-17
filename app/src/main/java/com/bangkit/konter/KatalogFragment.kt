package com.bangkit.konter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class KatalogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_katalog, container, false)

//        // Tombol Voucher
//        val btnVoucher: View = view.findViewById(R.id.btnVoucher)
//        btnVoucher.setOnClickListener {
//            navigateToCategory("Voucher")
//        }
//
//        // Tombol Kartu Perdana
//        val btnKartuPerdana: View = view.findViewById(R.id.btnKartuPerdana)
//        btnKartuPerdana.setOnClickListener {
//            navigateToCategory("Kartu Perdana")
//        }
//
//        // Tombol Aksesoris
//        val btnAksesoris: View = view.findViewById(R.id.btnAksesoris)
//        btnAksesoris.setOnClickListener {
//            navigateToCategory("Aksesoris")
//        }

        // FloatingActionButton untuk menambahkan data baru
        val fabAddProduct: FloatingActionButton = view.findViewById(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            // Navigasi ke halaman input produk
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            startActivity(intent)
        }

        return view
    }

//    private fun navigateToCategory(category: String) {
//        // Navigasi ke halaman kategori tertentu
//        val intent = Intent(requireContext(), CategoryActivity::class.java)
//        intent.putExtra("CATEGORY_NAME", category)
//        startActivity(intent)
//    }
}
