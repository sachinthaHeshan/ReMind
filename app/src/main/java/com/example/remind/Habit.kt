package com.example.remind

import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Data class representing a Habit
 */
data class Habit(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("time")
    val time: String,
    
    @SerializedName("frequency")
    val frequency: String,
    
    @SerializedName("isCompleted")
    var isCompleted: Boolean = false,
    
    @SerializedName("completedDates")
    val completedDates: MutableList<String> = mutableListOf(),
    
    @SerializedName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerializedName("currentStreak")
    var currentStreak: Int = 0,
    
    @SerializedName("bestStreak")
    var bestStreak: Int = 0
) {
    /**
     * Get the category color based on category name
     */
    fun getCategoryColor(): String {
        return when (category) {
            "Health" -> "#10B981"
            "Mindfulness" -> "#8B5CF6"
            "Learning" -> "#F59E0B"
            "Hydration" -> "#3B82F6"
            "Sleep" -> "#6366F1"
            else -> "#10B981"
        }
    }
    
    /**
     * Get the category icon resource based on category name
     */
    fun getCategoryIconRes(): Int {
        return when (category) {
            "Health" -> R.drawable.ic_fitness
            "Mindfulness" -> R.drawable.ic_meditation
            "Learning" -> R.drawable.ic_book
            "Hydration" -> R.drawable.ic_water
            "Sleep" -> R.drawable.ic_sleep
            else -> R.drawable.ic_habit
        }
    }
}

