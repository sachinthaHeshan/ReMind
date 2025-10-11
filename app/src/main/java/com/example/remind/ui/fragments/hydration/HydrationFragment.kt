package com.example.remind.ui.fragments.hydration

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remind.R
import com.example.remind.data.model.WaterLog
import com.example.remind.data.repository.WaterRepository
import com.example.remind.databinding.FragmentHydrationBinding
import com.example.remind.ui.adapter.WaterLogAdapter
import com.example.remind.utils.WaterNotificationHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * Hydration Fragment - Track daily water intake
 */
class HydrationFragment : Fragment() {

    private var _binding: FragmentHydrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var waterRepository: WaterRepository
    private lateinit var waterLogAdapter: WaterLogAdapter
    private lateinit var notificationHelper: WaterNotificationHelper

    // Permission launcher for notifications (Android 13+)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, enable reminders
            enableReminders()
        } else {
            // Permission denied, show message and uncheck switch
            binding.switchReminders.isChecked = false
            Snackbar.make(
                binding.root,
                "Notification permission is required for reminders",
                Snackbar.LENGTH_LONG
            ).setAction("Settings") {
                // Open app settings
                val intent = android.content.Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                }
                startActivity(intent)
            }.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HydrationFragment", "onCreateView called")
        _binding = FragmentHydrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repository and notification helper
        waterRepository = WaterRepository(requireContext())
        notificationHelper = WaterNotificationHelper(requireContext())

        setupRecyclerView()
        setupQuickAddButtons()
        setupSettings()
        
        // Load and display data
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        // Reload data when returning to this fragment
        updateUI()
    }

    private fun setupRecyclerView() {
        waterLogAdapter = WaterLogAdapter(
            onDeleteClick = { logId ->
                deleteWaterLog(logId)
            }
        )

        binding.rvWaterLogs.apply {
            adapter = waterLogAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupQuickAddButtons() {
        // 100ml button
        binding.btnAdd100.setOnClickListener {
            addWater(100)
        }

        // 250ml button
        binding.btnAdd250.setOnClickListener {
            addWater(250)
        }

        // 500ml button
        binding.btnAdd500.setOnClickListener {
            addWater(500)
        }

        // Custom amount button
        binding.btnAddCustom.setOnClickListener {
            showCustomAmountDialog()
        }
    }

    private fun setupSettings() {
        // Update daily goal text
        binding.tvDailyGoal.text = "${waterRepository.getDailyGoal()} ml"

        // Change goal button
        binding.btnChangeGoal.setOnClickListener {
            showChangeGoalDialog()
        }

        // Reminder switch
        binding.switchReminders.isChecked = waterRepository.areRemindersEnabled()
        binding.switchReminders.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Check permission before enabling
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            // Permission already granted
                            enableReminders()
                        }
                        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                            // Show explanation
                            Snackbar.make(
                                binding.root,
                                "Notification permission is needed to send water reminders",
                                Snackbar.LENGTH_LONG
                            ).setAction("Allow") {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }.show()
                            binding.switchReminders.isChecked = false
                        }
                        else -> {
                            // Request permission
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                } else {
                    // Android 12 and below - no permission needed
                    enableReminders()
                }
            } else {
                // Disable reminders
                waterRepository.setRemindersEnabled(false)
                notificationHelper.cancelHydrationReminders()
                Snackbar.make(binding.root, "Reminders disabled", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableReminders() {
        waterRepository.setRemindersEnabled(true)
        notificationHelper.scheduleHydrationReminders()
        // Show immediate confirmation notification
        Snackbar.make(binding.root, "Reminders enabled - You'll get notified every 10 seconds!", Snackbar.LENGTH_LONG).show()
    }

    private fun addWater(amount: Int) {
        val waterLog = WaterLog(amount = amount)
        val success = waterRepository.saveWaterLog(waterLog)

        if (success) {
            updateUI()
            
            // Show success message
            if (waterRepository.isGoalAchieved()) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.goal_achieved),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.water_added),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        } else {
            Snackbar.make(
                binding.root,
                "Failed to add water. Please try again.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun deleteWaterLog(logId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Entry")
            .setMessage("Are you sure you want to delete this water log?")
            .setPositiveButton("Delete") { _, _ ->
                val success = waterRepository.deleteWaterLog(logId)
                if (success) {
                    updateUI()
                    Snackbar.make(binding.root, "Entry deleted", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showCustomAmountDialog() {
        val input = EditText(requireContext()).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            hint = "Amount in ml"
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.enter_custom_amount))
            .setView(input)
            .setPositiveButton(getString(R.string.add_water)) { _, _ ->
                val amountStr = input.text.toString()
                if (amountStr.isNotEmpty()) {
                    val amount = amountStr.toIntOrNull()
                    if (amount != null && amount > 0) {
                        addWater(amount)
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Please enter a valid amount",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showChangeGoalDialog() {
        val input = EditText(requireContext()).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            hint = "Goal in ml"
            setText(waterRepository.getDailyGoal().toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.enter_daily_goal))
            .setView(input)
            .setPositiveButton(getString(R.string.change)) { _, _ ->
                val goalStr = input.text.toString()
                if (goalStr.isNotEmpty()) {
                    val goal = goalStr.toIntOrNull()
                    if (goal != null && goal > 0) {
                        waterRepository.setDailyGoal(goal)
                        binding.tvDailyGoal.text = "$goal ml"
                        updateUI()
                        Snackbar.make(
                            binding.root,
                            getString(R.string.goal_updated),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Please enter a valid goal",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateUI() {
        // Get today's logs
        val logs = waterRepository.getTodayWaterLogs()
        
        // Update RecyclerView
        if (logs.isEmpty()) {
            binding.rvWaterLogs.visibility = View.GONE
            binding.emptyStateLogs.visibility = View.VISIBLE
        } else {
            binding.rvWaterLogs.visibility = View.VISIBLE
            binding.emptyStateLogs.visibility = View.GONE
            waterLogAdapter.submitList(logs)
        }

        // Update progress
        val total = waterRepository.getTodayTotal()
        val goal = waterRepository.getDailyGoal()
        val remaining = waterRepository.getRemainingAmount()
        val progress = waterRepository.getProgressPercentage()

        binding.tvWaterAmount.text = "$total / $goal ml"
        binding.progressWater.progress = progress
        
        if (remaining > 0) {
            binding.tvRemaining.text = getString(R.string.ml_remaining, remaining)
        } else {
            binding.tvRemaining.text = getString(R.string.goal_achieved)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

