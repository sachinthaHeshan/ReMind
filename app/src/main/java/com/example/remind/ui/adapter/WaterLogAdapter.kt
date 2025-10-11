package com.example.remind.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.remind.data.model.WaterLog
import com.example.remind.databinding.ItemWaterLogBinding

/**
 * Adapter for displaying water log entries in a RecyclerView
 */
class WaterLogAdapter(
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<WaterLog, WaterLogAdapter.WaterLogViewHolder>(WaterLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterLogViewHolder {
        val binding = ItemWaterLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WaterLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterLogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WaterLogViewHolder(
        private val binding: ItemWaterLogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(waterLog: WaterLog) {
            binding.tvAmount.text = waterLog.getFormattedAmount()
            binding.tvTime.text = waterLog.getFormattedTime()
            
            binding.btnDelete.setOnClickListener {
                onDeleteClick(waterLog.id)
            }
        }
    }

    class WaterLogDiffCallback : DiffUtil.ItemCallback<WaterLog>() {
        override fun areItemsTheSame(oldItem: WaterLog, newItem: WaterLog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WaterLog, newItem: WaterLog): Boolean {
            return oldItem == newItem
        }
    }
}

