package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val guestName: String,
    val tableNumber: String,
    val dedication: String,
    val imageUri: String, // file path, drawable name, or content URI
    val tag: String = "Momentos", // "Vals", "Fiesta", "Cena", "Dedicatoria", "Momentos"
    val timestamp: Long = System.currentTimeMillis(),
    val reactionHearts: Int = 0,
    val reactionCheers: Int = 0,
    val reactionSparkles: Int = 0,
    val votes: Int = 0,
    val isPinned: Boolean = false
)
