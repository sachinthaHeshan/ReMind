package com.example.remind

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.remind.databinding.ItemHabitBinding

/**
 * RecyclerView Adapter for displaying habits
 */
class HabitAdapter(
    private val onHabitClick: (Habit) -> Unit,
    private val onHabitChecked: (Habit, Boolean) -> Unit,
    private val onEditClick: (Habit) -> Unit,
    private val onDeleteClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitViewHolder>(HabitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HabitViewHolder(
        private val binding: ItemHabitBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.apply {
                // Set habit name
                tvHabitName.text = habit.name
                
                // Set habit time
                tvHabitTime.text = habit.time
                
                // Set category
                tvHabitCategory.text = habit.category
                
                // Set icon and color
                ivHabitIcon.setImageResource(habit.getCategoryIconRes())
                try {
                    iconContainer.setCardBackgroundColor(Color.parseColor(habit.getCategoryColor()))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                
                // Set streak
                if (habit.currentStreak > 0) {
                    streakBadge.visibility = android.view.View.VISIBLE
                    tvStreak.text = "${habit.currentStreak} days"
                } else {
                    streakBadge.visibility = android.view.View.GONE
                }
                
                // Set checkbox state
                checkboxHabit.isChecked = habit.isCompleted
                
                // Set checkbox listener
                checkboxHabit.setOnCheckedChangeListener { _, isChecked ->
                    if (checkboxHabit.isPressed) { // Only respond to user clicks
                        onHabitChecked(habit, isChecked)
                    }
                }
                
                // Set card click listener
                cardHabit.setOnClickListener {
                    onHabitClick(habit)
                }
                
                // Set menu button listener
                btnMenu.setOnClickListener { view ->
                    val popupMenu = PopupMenu(view.context, view)
                    popupMenu.menuInflater.inflate(R.menu.habit_item_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.action_edit -> {
                                onEditClick(habit)
                                true
                            }
                            R.id.action_delete -> {
                                onDeleteClick(habit)
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.show()
                }
            }
        }
    }

    class HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }
    }
}

