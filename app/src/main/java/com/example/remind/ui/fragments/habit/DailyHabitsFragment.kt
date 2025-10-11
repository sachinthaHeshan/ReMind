package com.example.remind.ui.fragments.habit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.remind.R
import com.example.remind.data.model.Habit
import com.example.remind.data.repository.HabitRepository
import com.example.remind.databinding.FragmentDailyHabitsBinding
import com.example.remind.ui.adapter.HabitAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Daily Habits Fragment - Main screen showing list of daily habits
 */
class DailyHabitsFragment : Fragment() {

    private var _binding: FragmentDailyHabitsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    
    private lateinit var habitRepository: HabitRepository
    private lateinit var habitAdapter: HabitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        android.util.Log.d("DailyHabitsFragment", "onCreateView called")
        _binding = FragmentDailyHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repository
        habitRepository = HabitRepository(requireContext())

        // Setup toolbar
        binding.toolbar.title = ""

        // Setup dynamic greeting and date
        setupGreeting()

        // Setup RecyclerView
        setupRecyclerView()

        // Setup FAB click listener to navigate to Add Habit fragment
        binding.fabAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_DailyHabitsFragment_to_AddHabitFragment)
        }

        // Load habits
        loadHabits()
    }
    
    override fun onResume() {
        super.onResume()
        // Reload habits when returning to this fragment
        loadHabits()
    }
    
    private fun setupGreeting() {
        // Set greeting based on time of day
        val calendar = java.util.Calendar.getInstance()
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        
        val greeting = when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
        binding.tvGreeting.text = greeting
        
        // Set current date
        val dateFormat = java.text.SimpleDateFormat("EEEE, MMMM d", java.util.Locale.getDefault())
        val currentDate = dateFormat.format(java.util.Date())
        binding.tvDate.text = currentDate
    }
    
    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            onHabitClick = { habit ->
                // TODO: Navigate to habit details
                // For now, just show a toast
                com.google.android.material.snackbar.Snackbar.make(
                    binding.root,
                    "Clicked: ${habit.name}",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
            },
            onHabitChecked = { habit, isChecked ->
                // Update habit completion status
                habitRepository.updateHabitCompletion(habit.id, isChecked)
                loadHabits() // Refresh the list
            },
            onEditClick = { habit ->
                // Navigate to edit habit fragment
                val action = DailyHabitsFragmentDirections.actionDailyHabitsFragmentToEditHabitFragment(habit.id)
                findNavController().navigate(action)
            },
            onDeleteClick = { habit ->
                // Show confirmation dialog
                showDeleteConfirmationDialog(habit)
            }
        )
        
        binding.rvHabits.apply {
            adapter = habitAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        }
    }
    
    private fun showDeleteConfirmationDialog(habit: Habit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Habit")
            .setMessage("Are you sure you want to delete \"${habit.name}\"?")
            .setPositiveButton("Delete") { _, _ ->
                deleteHabit(habit)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun deleteHabit(habit: Habit) {
        val success = habitRepository.deleteHabit(habit.id)
        if (success) {
            com.google.android.material.snackbar.Snackbar.make(
                binding.root,
                "Habit deleted successfully",
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
            loadHabits() // Refresh the list
        } else {
            com.google.android.material.snackbar.Snackbar.make(
                binding.root,
                "Failed to delete habit",
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
        }
    }
    
    private fun loadHabits() {
        val habits = habitRepository.getTodayHabits()
        
        if (habits.isEmpty()) {
            binding.emptyState.visibility = android.view.View.VISIBLE
            binding.rvHabits.visibility = android.view.View.GONE
        } else {
            binding.emptyState.visibility = android.view.View.GONE
            binding.rvHabits.visibility = android.view.View.VISIBLE
            habitAdapter.submitList(habits)
        }
        
        // Update statistics
        updateStatistics(habits)
    }
    
    private fun updateStatistics(habits: List<Habit>) {
        val completedCount = habits.count { it.isCompleted }
        val totalCount = habits.size
        
        // Update progress text
        binding.tvHabitProgress.text = "$completedCount of $totalCount habits completed"
        
        // Update progress bar
        val progress = if (totalCount > 0) {
            (completedCount * 100) / totalCount
        } else {
            0
        }
        binding.progressHabits.progress = progress
        
        // Update statistics card
        val stats = habitRepository.getStatistics()
        binding.tvStreakCount.text = stats.averageStreak.toString()
        binding.tvWeeklyCount.text = stats.totalCompleted.toString()
        binding.tvTotalHabits.text = stats.totalHabits.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

