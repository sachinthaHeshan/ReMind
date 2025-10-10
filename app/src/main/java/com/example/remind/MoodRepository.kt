package com.example.remind

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Repository class to manage Mood data in SharedPreferences
 */
class MoodRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "mood_prefs"
        private const val KEY_MOODS = "moods"
    }

    /**
     * Save a new mood entry
     */
    fun saveMood(mood: Mood): Boolean {
        return try {
            val moods = getAllMoods().toMutableList()
            moods.add(0, mood) // Add to beginning for most recent first
            saveMoods(moods)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get all mood entries (sorted by most recent first)
     */
    fun getAllMoods(): List<Mood> {
        return try {
            val json = sharedPreferences.getString(KEY_MOODS, null)
            if (json != null) {
                val type = object : TypeToken<List<Mood>>() {}.type
                val moods: List<Mood> = gson.fromJson(json, type)
                moods.sortedByDescending { it.timestamp }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Get moods for today
     */
    fun getTodayMoods(): List<Mood> {
        val startOfDay = getStartOfDayTimestamp()
        return getAllMoods().filter { it.timestamp >= startOfDay }
    }

    /**
     * Delete a mood entry
     */
    fun deleteMood(moodId: String): Boolean {
        return try {
            val moods = getAllMoods().toMutableList()
            moods.removeIf { it.id == moodId }
            saveMoods(moods)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get mood statistics
     */
    fun getMoodStatistics(): MoodStatistics {
        val allMoods = getAllMoods()
        val todayMoods = getTodayMoods()
        
        // Calculate most common mood
        val moodCounts = allMoods.groupingBy { it.moodName }.eachCount()
        val mostCommonMood = moodCounts.maxByOrNull { it.value }?.key ?: "N/A"
        
        return MoodStatistics(
            totalEntries = allMoods.size,
            entriesToday = todayMoods.size,
            mostCommonMood = mostCommonMood
        )
    }

    /**
     * Clear all moods (for testing)
     */
    fun clearAllMoods() {
        sharedPreferences.edit().remove(KEY_MOODS).apply()
    }

    // Private helper methods

    private fun saveMoods(moods: List<Mood>) {
        val json = gson.toJson(moods)
        sharedPreferences.edit().putString(KEY_MOODS, json).apply()
    }

    private fun getStartOfDayTimestamp(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

/**
 * Data class for mood statistics
 */
data class MoodStatistics(
    val totalEntries: Int,
    val entriesToday: Int,
    val mostCommonMood: String
)

