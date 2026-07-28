package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val fullName: String,
    val workEmail: String,
    val passwordHash: String,
    val isLoggedIn: Boolean = true,
    val termsAccepted: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
