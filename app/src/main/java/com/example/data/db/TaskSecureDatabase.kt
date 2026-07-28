package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class TaskSecureDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: TaskSecureDatabase? = null

        fun getDatabase(context: Context): TaskSecureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskSecureDatabase::class.java,
                    "task_secure_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
