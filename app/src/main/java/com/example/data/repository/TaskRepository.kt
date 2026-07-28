package com.example.data.repository

import com.example.data.db.TaskDao
import com.example.data.db.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()

    fun getTasksByStatus(status: String): Flow<List<TaskEntity>> = taskDao.getTasksByStatus(status)

    suspend fun addTask(
        title: String,
        description: String,
        status: String = "TODO",
        priority: String = "MEDIUM",
        category: String = "Security",
        isEncrypted: Boolean = true
    ): Long {
        val task = TaskEntity(
            title = title,
            description = description,
            status = status,
            priority = priority,
            category = category,
            isEncrypted = isEncrypted
        )
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteTaskById(id: Int) {
        taskDao.deleteTaskById(id)
    }
}
