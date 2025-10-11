package com.example.remind.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Data class representing a water intake log entry
 */
data class WaterLog(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    
    @SerializedName("amount")
    val amount: Int, // Amount in ml
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Get formatted time string (e.g., "10:30 AM")
     */
    fun getFormattedTime(): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        
        val hour = calendar.get(java.util.Calendar.HOUR)
        val minute = calendar.get(java.util.Calendar.MINUTE)
        val amPm = if (calendar.get(java.util.Calendar.AM_PM) == java.util.Calendar.AM) "AM" else "PM"
        
        val displayHour = if (hour == 0) 12 else hour
        return String.format("%d:%02d %s", displayHour, minute, amPm)
    }
    
    /**
     * Get formatted amount string (e.g., "250 ml")
     */
    fun getFormattedAmount(): String {
        return "$amount ml"
    }
    
    /**
     * Check if this log is from today
     */
    fun isToday(): Boolean {
        val today = java.util.Calendar.getInstance()
        val logDate = java.util.Calendar.getInstance()
        logDate.timeInMillis = timestamp
        
        return today.get(java.util.Calendar.YEAR) == logDate.get(java.util.Calendar.YEAR) &&
                today.get(java.util.Calendar.DAY_OF_YEAR) == logDate.get(java.util.Calendar.DAY_OF_YEAR)
    }
}

