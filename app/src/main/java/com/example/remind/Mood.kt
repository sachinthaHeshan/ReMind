package com.example.remind

import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Data class representing a Mood entry
 */
data class Mood(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    
    @SerializedName("emoji")
    val emoji: String,
    
    @SerializedName("moodName")
    val moodName: String,
    
    @SerializedName("note")
    val note: String = "",
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

