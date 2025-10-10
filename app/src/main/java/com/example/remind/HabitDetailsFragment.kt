package com.example.remind

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.remind.databinding.FragmentHabitDetailsBinding

/**
 * Habit Details Fragment - Shows detailed statistics and information about a specific habit
 */
class HabitDetailsFragment : Fragment() {

    private var _binding: FragmentHabitDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHabitDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar with back navigation
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup Edit button
        binding.btnEditHabit.setOnClickListener {
            // TODO: Show edit habit dialog
        }

        // Setup Delete button
        binding.btnDeleteHabit.setOnClickListener {
            // TODO: Show delete confirmation dialog
        }

        // TODO: Load habit details from arguments
        // TODO: Display habit statistics
        // TODO: Load and display weekly progress
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

