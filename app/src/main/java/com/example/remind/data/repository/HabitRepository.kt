package com.example.remind.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.remind.data.model.Habit
import com.example.remind.widget.HabitWidgetProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitRepository(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "habit_prefs"
        private const val KEY_HABITS = "habits"
    }

    /**
     * Save a new habit
     */
    fun saveHabit(habit: Habit): Boolean {
        return try {
            val habits = getAllHabits().toMutableList()
            habits.add(habit)
            saveHabits(habits)
            
            // Update widget to show new habit
            HabitWidgetProvider.updateAllWidgets(context)
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get all habits
     */
    fun getAllHabits(): List<Habit> {
        return try {
            val json = sharedPreferences.getString(KEY_HABITS, null)
            if (json != null) {
                val type = object : TypeToken<List<Habit>>() {}.type
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    fun getTodayHabits(): List<Habit> {
        val habits = getAllHabits()
        val today = getCurrentDate()
        
        return habits.map { habit ->
            // Reset completion if it's a new day
            val wasCompletedToday = habit.completedDates.lastOrNull() == today
            habit.copy(isCompleted = wasCompletedToday)
        }
    }


    fun updateHabitCompletion(habitId: String, isCompleted: Boolean): Boolean {
        return try {
            val habits = getAllHabits().toMutableList()
            val index = habits.indexOfFirst { it.id == habitId }
            
            if (index != -1) {
                val habit = habits[index]
                val today = getCurrentDate()
                
                if (isCompleted && !habit.completedDates.contains(today)) {
                    habit.completedDates.add(today)
                    habit.currentStreak = calculateStreak(habit.completedDates)
                    if (habit.currentStreak > habit.bestStreak) {
                        habit.bestStreak = habit.currentStreak
                    }
                } else if (!isCompleted && habit.completedDates.contains(today)) {
                    habit.completedDates.remove(today)
                    habit.currentStreak = calculateStreak(habit.completedDates)
                }
                
                habit.isCompleted = isCompleted
                habits[index] = habit
                saveHabits(habits)
                
                // Update widget to reflect changes
                HabitWidgetProvider.updateAllWidgets(context)
                
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Update a habit
     */
    fun updateHabit(habit: Habit): Boolean {
        return try {
            val habits = getAllHabits().toMutableList()
            val index = habits.indexOfFirst { it.id == habit.id }
            
            if (index != -1) {
                habits[index] = habit
                saveHabits(habits)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Delete a habit
     */
    fun deleteHabit(habitId: String): Boolean {
        return try {
            val habits = getAllHabits().toMutableList()
            habits.removeIf { it.id == habitId }
            saveHabits(habits)
            
            // Update widget after deletion
            HabitWidgetProvider.updateAllWidgets(context)
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get habit by ID
     */
    fun getHabitById(habitId: String): Habit? {
        return getAllHabits().find { it.id == habitId }
    }

    /**
     * Get statistics
     */
    fun getStatistics(): HabitStatistics {
        val habits = getTodayHabits()
        val completedToday = habits.count { it.isCompleted }
        val totalHabits = habits.size
        val totalCompleted = habits.sumOf { it.completedDates.size }
        val averageStreak = if (habits.isNotEmpty()) {
            habits.sumOf { it.currentStreak } / habits.size
        } else {
            0
        }
        
        return HabitStatistics(
            totalHabits = totalHabits,
            completedToday = completedToday,
            totalCompleted = totalCompleted,
            averageStreak = averageStreak
        )
    }

    /**
     * Clear all habits (for testing)
     */
    fun clearAllHabits() {
        sharedPreferences.edit().remove(KEY_HABITS).apply()
    }

    // Private helper methods

    private fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        sharedPreferences.edit().putString(KEY_HABITS, json).apply()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun calculateStreak(completedDates: List<String>): Int {
        if (completedDates.isEmpty()) return 0
        
        val sortedDates = completedDates.sorted().reversed()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var streak = 0
        var previousDate: Date? = null
        
        for (dateString in sortedDates) {
            try {
                val date = dateFormat.parse(dateString) ?: continue
                
                if (previousDate == null) {
                    // First date
                    val today = Date()
                    val diffInMillis = today.time - date.time
                    val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
                    
                    if (diffInDays <= 1) {
                        streak = 1
                        previousDate = date
                    } else {
                        break
                    }
                } else {
                    // Check if consecutive
                    val diffInMillis = previousDate.time - date.time
                    val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
                    
                    if (diffInDays == 1L) {
                        streak++
                        previousDate = date
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        return streak
    }
}

/**
 * Data class for habit statistics
 */
data class HabitStatistics(
    val totalHabits: Int,
    val completedToday: Int,
    val totalCompleted: Int,
    val averageStreak: Int
)

