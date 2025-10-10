package com.example.remind

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.remind.databinding.ItemMoodBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * RecyclerView Adapter for displaying mood entries
 */
class MoodAdapter(
    private val onDeleteClick: (Mood) -> Unit
) : ListAdapter<Mood, MoodAdapter.MoodViewHolder>(MoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val binding = ItemMoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MoodViewHolder(
        private val binding: ItemMoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mood: Mood) {
            binding.apply {
                // Set emoji
                tvEmoji.text = mood.emoji
                
                // Set mood name
                tvMoodName.text = mood.moodName
                
                // Set note
                if (mood.note.isNotEmpty()) {
                    tvMoodNote.visibility = android.view.View.VISIBLE
                    tvMoodNote.text = mood.note
                } else {
                    tvMoodNote.visibility = android.view.View.GONE
                }
                
                // Set time
                tvMoodTime.text = formatTimestamp(mood.timestamp)
                
                // Set delete button listener
                btnDeleteMood.setOnClickListener {
                    onDeleteClick(mood)
                }
            }
        }

        private fun formatTimestamp(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val difference = now - timestamp
            
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            
            val todayCalendar = Calendar.getInstance()
            
            return when {
                isSameDay(calendar, todayCalendar) -> {
                    // Today
                    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                    "${timeFormat.format(Date(timestamp))} • Today"
                }
                difference < 2 * 24 * 60 * 60 * 1000 -> {
                    // Yesterday
                    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                    "${timeFormat.format(Date(timestamp))} • Yesterday"
                }
                difference < 7 * 24 * 60 * 60 * 1000 -> {
                    // This week
                    val dateFormat = SimpleDateFormat("EEEE, h:mm a", Locale.getDefault())
                    dateFormat.format(Date(timestamp))
                }
                else -> {
                    // Older
                    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                    dateFormat.format(Date(timestamp))
                }
            }
        }

        private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        }
    }

    class MoodDiffCallback : DiffUtil.ItemCallback<Mood>() {
        override fun areItemsTheSame(oldItem: Mood, newItem: Mood): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Mood, newItem: Mood): Boolean {
            return oldItem == newItem
        }
    }
}

