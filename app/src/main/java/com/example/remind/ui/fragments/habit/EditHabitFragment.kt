package com.example.remind.ui.fragments.habit

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.remind.R
import com.example.remind.data.model.Habit
import com.example.remind.data.repository.HabitRepository
import com.example.remind.databinding.FragmentAddHabitBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class EditHabitFragment : Fragment() {

    private var _binding: FragmentAddHabitBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory: String = "Health"
    private var selectedTime: String = ""
    private var selectedFrequency: String = "Daily"
    
    private lateinit var habitRepository: HabitRepository
    private lateinit var currentHabit: Habit
    
    private val args: EditHabitFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repository
        habitRepository = HabitRepository(requireContext())

        // Load the habit to edit
        val habitId = args.habitId
        currentHabit = habitRepository.getHabitById(habitId) ?: run {
            Snackbar.make(binding.root, "Habit not found", Snackbar.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        setupToolbar()
        loadHabitData()
        setupCategorySelection()
        setupTimePicker()
        setupFrequencySelection()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "Edit Habit"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadHabitData() {
        // Load habit name
        binding.etHabitName.setText(currentHabit.name)
        
        // Load category
        selectedCategory = currentHabit.category
        
        // Load time
        selectedTime = currentHabit.time
        binding.tvSelectedTime.text = currentHabit.time
        
        // Load frequency
        selectedFrequency = currentHabit.frequency
        when (currentHabit.frequency) {
            "Daily" -> binding.chipDaily.isChecked = true
            "Weekly" -> binding.chipWeekly.isChecked = true
        }
    }

    private fun setupCategorySelection() {
        // Health category
        binding.cardCategoryHealth.setOnClickListener {
            selectCategory("Health", binding.cardCategoryHealth)
        }

        // Mindfulness category
        binding.cardCategoryMindfulness.setOnClickListener {
            selectCategory("Mindfulness", binding.cardCategoryMindfulness)
        }

        // Learning category
        binding.cardCategoryLearning.setOnClickListener {
            selectCategory("Learning", binding.cardCategoryLearning)
        }

        // Hydration category
        binding.cardCategoryHydration.setOnClickListener {
            selectCategory("Hydration", binding.cardCategoryHydration)
        }

        // Sleep category
        binding.cardCategorySleep.setOnClickListener {
            selectCategory("Sleep", binding.cardCategorySleep)
        }

        // Select current category
        val currentCategoryCard = when (selectedCategory) {
            "Health" -> binding.cardCategoryHealth
            "Mindfulness" -> binding.cardCategoryMindfulness
            "Learning" -> binding.cardCategoryLearning
            "Hydration" -> binding.cardCategoryHydration
            "Sleep" -> binding.cardCategorySleep
            else -> binding.cardCategoryHealth
        }
        selectCategory(selectedCategory, currentCategoryCard)
    }

    private fun selectCategory(category: String, selectedCard: View) {
        selectedCategory = category

        // Reset all cards
        resetCategoryCard(binding.cardCategoryHealth)
        resetCategoryCard(binding.cardCategoryMindfulness)
        resetCategoryCard(binding.cardCategoryLearning)
        resetCategoryCard(binding.cardCategoryHydration)
        resetCategoryCard(binding.cardCategorySleep)

        // Highlight selected card
        (selectedCard as com.google.android.material.card.MaterialCardView).strokeWidth = 4
    }

    private fun resetCategoryCard(card: com.google.android.material.card.MaterialCardView) {
        card.strokeWidth = 2
    }

    private fun setupTimePicker() {
        binding.cardTimePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    selectedTime = formatTime(selectedHour, selectedMinute)
                    binding.tvSelectedTime.text = selectedTime
                },
                hour,
                minute,
                false
            ).show()
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val amPm = if (hour >= 12) "PM" else "AM"
        val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
        return String.format("%d:%02d %s", displayHour, minute, amPm)
    }

    private fun setupFrequencySelection() {
        binding.chipGroupFrequency.setOnCheckedStateChangeListener { _, checkedIds ->
            selectedFrequency = when (checkedIds.firstOrNull()) {
                R.id.chip_daily -> "Daily"
                R.id.chip_weekly -> "Weekly"
                else -> "Daily"
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSaveHabit.text = "Update Habit"
        binding.btnSaveHabit.setOnClickListener {
            val habitName = binding.etHabitName.text.toString().trim()

            if (habitName.isEmpty()) {
                binding.tilHabitName.error = "Please enter a habit name"
                return@setOnClickListener
            }

            if (selectedTime.isEmpty()) {
                Snackbar.make(binding.root, "Please select a reminder time", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update habit with new data
            val updatedHabit = currentHabit.copy(
                name = habitName,
                category = selectedCategory,
                time = selectedTime,
                frequency = selectedFrequency
            )

            // Save updated habit
            val success = habitRepository.updateHabit(updatedHabit)
            
            if (success) {
                Snackbar.make(binding.root, "Habit updated successfully!", Snackbar.LENGTH_SHORT).show()
                // Navigate back to daily habits page
                findNavController().navigateUp()
            } else {
                Snackbar.make(binding.root, "Failed to update habit. Please try again.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

