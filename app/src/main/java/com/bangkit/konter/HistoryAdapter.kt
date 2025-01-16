package com.bangkit.konter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private var historyList: List<HistoryItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_VOUCHER = 1
    }

    fun updateData(newData: List<HistoryItem>) {
        historyList = newData
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (historyList[position]) {
            is HistoryItem.Header -> VIEW_TYPE_HEADER
            is HistoryItem.VoucherItem -> VIEW_TYPE_VOUCHER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history, parent, false)
            VoucherViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = historyList[position]) {
            is HistoryItem.Header -> (holder as HeaderViewHolder).bind(item)
            is HistoryItem.VoucherItem -> (holder as VoucherViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = historyList.size

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTextView: TextView = view.findViewById(R.id.tvDate)
        private val totalProfitTextView: TextView = view.findViewById(R.id.tvTotalProfit)

        fun bind(header: HistoryItem.Header) {
            dateTextView.text = header.date
            totalProfitTextView.text = "Total Profit: Rp${header.totalProfit}"
        }
    }

    class VoucherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.tvName)
        private val priceTextView: TextView = view.findViewById(R.id.tvPrice)
        private val costPriceTextView: TextView = view.findViewById(R.id.tvCostPrice)
        private val profitTextView: TextView = view.findViewById(R.id.tvProfit)

        fun bind(voucher: HistoryItem.VoucherItem) {
            nameTextView.text = voucher.name
            priceTextView.text = "Harga Jual: Rp${voucher.price}"
            costPriceTextView.text = "Harga Modal: Rp${voucher.costPrice}"
            profitTextView.text = "Profit: Rp${voucher.profit}"
        }
    }
}
