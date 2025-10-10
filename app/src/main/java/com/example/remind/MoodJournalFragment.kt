package com.example.remind

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remind.databinding.FragmentMoodJournalBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * Mood Journal Fragment - Track daily moods with emoji selector
 */
class MoodJournalFragment : Fragment() {

    private var _binding: FragmentMoodJournalBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var moodRepository: MoodRepository
    private lateinit var moodAdapter: MoodAdapter
    
    private var selectedEmoji: String? = null
    private var selectedMoodName: String? = null

    private val emojiMap = mapOf(
        "😊" to "Happy",
        "🤩" to "Excited",
        "😐" to "Neutral",
        "😢" to "Sad",
        "😠" to "Angry",
        "😴" to "Tired",
        "😰" to "Anxious",
        "😌" to "Relaxed",
        "🥰" to "Loved",
        "🤒" to "Sick"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        android.util.Log.d("MoodJournalFragment", "onCreateView called")
        _binding = FragmentMoodJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repository
        moodRepository = MoodRepository(requireContext())

        // Setup RecyclerView
        setupRecyclerView()

        // Setup emoji selectors
        setupEmojiSelectors()

        // Setup save button
        setupSaveButton()

        // Load moods
        loadMoods()
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(
            onDeleteClick = { mood ->
                showDeleteConfirmation(mood)
            }
        )
        
        binding.rvMoods.apply {
            adapter = moodAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupEmojiSelectors() {
        val emojiCards = mapOf(
            binding.emojiHappy to ("😊" to "Happy"),
            binding.emojiExcited to ("🤩" to "Excited"),
            binding.emojiNeutral to ("😐" to "Neutral"),
            binding.emojiSad to ("😢" to "Sad"),
            binding.emojiAngry to ("😠" to "Angry"),
            binding.emojiTired to ("😴" to "Tired"),
            binding.emojiAnxious to ("😰" to "Anxious"),
            binding.emojiRelaxed to ("😌" to "Relaxed"),
            binding.emojiLoved to ("🥰" to "Loved"),
            binding.emojiSick to ("🤒" to "Sick")
        )

        emojiCards.forEach { (card, emojiPair) ->
            card.setOnClickListener {
                selectEmoji(card, emojiPair.first, emojiPair.second)
            }
        }
    }

    private fun selectEmoji(
        selectedCard: com.google.android.material.card.MaterialCardView,
        emoji: String,
        moodName: String
    ) {
        // Reset all cards
        resetAllEmojiCards()

        // Highlight selected card
        selectedCard.strokeWidth = 4
        selectedCard.strokeColor = requireContext().getColor(R.color.primary)

        // Store selection
        selectedEmoji = emoji
        selectedMoodName = moodName
    }

    private fun resetAllEmojiCards() {
        val allCards = listOf(
            binding.emojiHappy, binding.emojiExcited, binding.emojiNeutral,
            binding.emojiSad, binding.emojiAngry, binding.emojiTired,
            binding.emojiAnxious, binding.emojiRelaxed, binding.emojiLoved,
            binding.emojiSick
        )
        
        allCards.forEach { card ->
            card.strokeWidth = 2
            card.strokeColor = requireContext().getColor(R.color.card_stroke)
        }
    }

    private fun setupSaveButton() {
        binding.btnSaveMood.setOnClickListener {
            if (selectedEmoji == null || selectedMoodName == null) {
                Snackbar.make(binding.root, "Please select a mood", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = binding.etNote.text.toString().trim()

            val mood = Mood(
                emoji = selectedEmoji!!,
                moodName = selectedMoodName!!,
                note = note
            )

            val success = moodRepository.saveMood(mood)

            if (success) {
                Snackbar.make(binding.root, "Mood saved!", Snackbar.LENGTH_SHORT).show()
                
                // Clear form
                resetAllEmojiCards()
                binding.etNote.text?.clear()
                selectedEmoji = null
                selectedMoodName = null
                
                // Reload list
                loadMoods()
            } else {
                Snackbar.make(binding.root, "Failed to save mood", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMoods() {
        val moods = moodRepository.getAllMoods()
        
        if (moods.isEmpty()) {
            binding.emptyStateMood.visibility = View.VISIBLE
            binding.rvMoods.visibility = View.GONE
        } else {
            binding.emptyStateMood.visibility = View.GONE
            binding.rvMoods.visibility = View.VISIBLE
            moodAdapter.submitList(moods)
        }
    }

    private fun showDeleteConfirmation(mood: Mood) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Mood Entry")
            .setMessage("Are you sure you want to delete this mood entry?")
            .setPositiveButton("Delete") { _, _ ->
                deleteMood(mood)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteMood(mood: Mood) {
        val success = moodRepository.deleteMood(mood.id)
        
        if (success) {
            Snackbar.make(binding.root, "Mood deleted", Snackbar.LENGTH_SHORT).show()
            loadMoods()
        } else {
            Snackbar.make(binding.root, "Failed to delete mood", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

