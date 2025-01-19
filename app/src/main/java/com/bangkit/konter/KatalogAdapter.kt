package com.bangkit.konter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KatalogAdapter(
    private val katalogList: List<Voucher>,
    private val onEditClicked: (Voucher) -> Unit,
    private val onDeleteClicked: (Voucher) -> Unit
) : RecyclerView.Adapter<KatalogAdapter.KatalogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KatalogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_katalog, parent, false)
        return KatalogViewHolder(view)
    }

    override fun onBindViewHolder(holder: KatalogViewHolder, position: Int) {
        val voucher = katalogList[position]
        holder.bind(voucher)
    }

    override fun getItemCount() = katalogList.size

    inner class KatalogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvKatalogName: TextView = itemView.findViewById(R.id.tvKatalogName)
        private val tvKatalogSellingPrice: TextView = itemView.findViewById(R.id.tvKatalogSellingPrice)
        private val tvKatalogCostPrice: TextView = itemView.findViewById(R.id.tvKatalogCostPrice)
        private val ivEditKatalog: ImageView = itemView.findViewById(R.id.ivEditKatalog)
        private val ivDeleteKatalog: ImageView = itemView.findViewById(R.id.ivDeleteKatalog)

        fun bind(voucher: Voucher) {
            tvKatalogName.text = voucher.name
            tvKatalogSellingPrice.text = "Harga Jual: Rp${voucher.sellingPrice}"
            tvKatalogCostPrice.text = "Harga Modal: Rp${voucher.costPrice}"

            ivEditKatalog.setOnClickListener {
                onEditClicked(voucher)
            }

            ivDeleteKatalog.setOnClickListener {
                onDeleteClicked(voucher)
            }
        }
    }
}
