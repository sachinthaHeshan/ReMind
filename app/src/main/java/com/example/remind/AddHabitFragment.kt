package com.example.remind

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.remind.databinding.FragmentAddHabitBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

/**
 * Add Habit Fragment - Allows users to create new habits
 */
class AddHabitFragment : Fragment() {

    private var _binding: FragmentAddHabitBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory: String = "Health"
    private var selectedTime: String = ""
    private var selectedFrequency: String = "Daily"
    
    private lateinit var habitRepository: HabitRepository

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

        setupToolbar()
        setupCategorySelection()
        setupTimePicker()
        setupFrequencySelection()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
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

        // Select Health by default
        selectCategory("Health", binding.cardCategoryHealth)
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
                    selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    binding.tvSelectedTime.text = formatTime(selectedHour, selectedMinute)
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

            // Create new habit
            val habit = Habit(
                name = habitName,
                category = selectedCategory,
                time = binding.tvSelectedTime.text.toString(),
                frequency = selectedFrequency
            )

            // Save habit to SharedPreferences
            val success = habitRepository.saveHabit(habit)
            
            if (success) {
                Snackbar.make(binding.root, "Habit created successfully!", Snackbar.LENGTH_SHORT).show()
                // Navigate back to daily habits page
                findNavController().navigateUp()
            } else {
                Snackbar.make(binding.root, "Failed to save habit. Please try again.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

