package com.bangkit.konter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VoucherAdapter(
    private val voucherList: List<Voucher>,
    private val onItemClick: (Voucher) -> Unit
) : RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>() {

    inner class VoucherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvVoucherName: TextView = itemView.findViewById(R.id.tvVoucherName)
        val tvVoucherPrice: TextView = itemView.findViewById(R.id.tvVoucherPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voucher, parent, false)
        return VoucherViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        val voucher = voucherList[position]
        Log.d("VoucherAdapter", "Voucher: $voucher")
        holder.tvVoucherName.text = voucher.name
        holder.tvVoucherPrice.text = "Price: ${voucher.price}"
        holder.itemView.setOnClickListener {
            onItemClick(voucher) // Memanggil fungsi onItemClick saat item diklik
        }

    }

    override fun getItemCount(): Int {
        return voucherList.size
    }

}
