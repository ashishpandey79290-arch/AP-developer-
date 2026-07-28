package com.example.data.repository

import com.example.data.db.UserDao
import com.example.data.db.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val currentUser: Flow<UserEntity?> = userDao.getUser()

    suspend fun registerUser(fullName: String, workEmail: String, passwordHash: String): UserEntity {
        val user = UserEntity(
            id = 1,
            fullName = fullName,
            workEmail = workEmail,
            passwordHash = passwordHash,
            isLoggedIn = true,
            termsAccepted = true
        )
        userDao.insertOrUpdateUser(user)
        return user
    }

    suspend fun loginUser(email: String): Boolean {
        val existing = userDao.getUserOnce()
        if (existing != null && existing.workEmail.equals(email, ignoreCase = true)) {
            userDao.setLoggedInState(true)
            return true
        }
        return false
    }

    suspend fun logout() {
        userDao.setLoggedInState(false)
    }
}
