package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val status: String = "TODO", // TODO, IN_PROGRESS, COMPLETED
    val priority: String = "MEDIUM", // LOW, MEDIUM, HIGH
    val category: String = "Security",
    val isEncrypted: Boolean = true,
    val dueDate: Long = System.currentTimeMillis() + 86400000L,
    val createdAt: Long = System.currentTimeMillis()
)
