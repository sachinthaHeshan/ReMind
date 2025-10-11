package com.example.remind.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.remind.data.model.WaterLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

/**
 * Repository for managing water intake data
 */
class WaterRepository(context: Context) {
    
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("water_prefs", Context.MODE_PRIVATE)
    
    private val gson = Gson()
    
    companion object {
        private const val KEY_WATER_LOGS = "water_logs"
        private const val KEY_DAILY_GOAL = "daily_goal"
        private const val KEY_REMINDERS_ENABLED = "reminders_enabled"
        private const val DEFAULT_DAILY_GOAL = 2000 // 2000 ml = 2 liters
    }
    
    /**
     * Save a water log entry
     */
    fun saveWaterLog(waterLog: WaterLog): Boolean {
        return try {
            val logs = getWaterLogs().toMutableList()
            logs.add(waterLog)
            
            val json = gson.toJson(logs)
            sharedPreferences.edit()
                .putString(KEY_WATER_LOGS, json)
                .apply()
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get all water logs
     */
    fun getWaterLogs(): List<WaterLog> {
        return try {
            val json = sharedPreferences.getString(KEY_WATER_LOGS, null)
            if (json != null) {
                val type = object : TypeToken<List<WaterLog>>() {}.type
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Get today's water logs
     */
    fun getTodayWaterLogs(): List<WaterLog> {
        val allLogs = getWaterLogs()
        return allLogs.filter { it.isToday() }.sortedByDescending { it.timestamp }
    }
    
    /**
     * Get today's total water intake in ml
     */
    fun getTodayTotal(): Int {
        return getTodayWaterLogs().sumOf { it.amount }
    }
    
    /**
     * Delete a water log by ID
     */
    fun deleteWaterLog(logId: String): Boolean {
        return try {
            val logs = getWaterLogs().toMutableList()
            logs.removeIf { it.id == logId }
            
            val json = gson.toJson(logs)
            sharedPreferences.edit()
                .putString(KEY_WATER_LOGS, json)
                .apply()
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get daily goal in ml
     */
    fun getDailyGoal(): Int {
        return sharedPreferences.getInt(KEY_DAILY_GOAL, DEFAULT_DAILY_GOAL)
    }
    
    /**
     * Set daily goal in ml
     */
    fun setDailyGoal(goal: Int): Boolean {
        return try {
            sharedPreferences.edit()
                .putInt(KEY_DAILY_GOAL, goal)
                .apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Check if reminders are enabled
     */
    fun areRemindersEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_REMINDERS_ENABLED, false)
    }
    
    /**
     * Set reminders enabled/disabled
     */
    fun setRemindersEnabled(enabled: Boolean): Boolean {
        return try {
            sharedPreferences.edit()
                .putBoolean(KEY_REMINDERS_ENABLED, enabled)
                .apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get progress percentage (0-100)
     */
    fun getProgressPercentage(): Int {
        val total = getTodayTotal()
        val goal = getDailyGoal()
        return if (goal > 0) {
            ((total.toFloat() / goal.toFloat()) * 100).toInt().coerceAtMost(100)
        } else {
            0
        }
    }
    
    /**
     * Get remaining amount to reach goal
     */
    fun getRemainingAmount(): Int {
        val total = getTodayTotal()
        val goal = getDailyGoal()
        return (goal - total).coerceAtLeast(0)
    }
    
    /**
     * Check if daily goal is achieved
     */
    fun isGoalAchieved(): Boolean {
        return getTodayTotal() >= getDailyGoal()
    }
    
    /**
     * Clear old logs (older than 30 days)
     */
    fun clearOldLogs() {
        try {
            val thirtyDaysAgo = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -30)
            }.timeInMillis
            
            val logs = getWaterLogs().toMutableList()
            logs.removeIf { it.timestamp < thirtyDaysAgo }
            
            val json = gson.toJson(logs)
            sharedPreferences.edit()
                .putString(KEY_WATER_LOGS, json)
                .apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

