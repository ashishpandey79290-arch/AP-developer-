package com.example.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.TaskEntity
import com.example.data.db.TaskSecureDatabase
import com.example.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TaskDashboardUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All", // All, Security, Engineering, Operations, Compliance
    val selectedStatus: String = "All", // All, TODO, IN_PROGRESS, COMPLETED
    val isAddTaskDialogOpen: Boolean = false,
    val isSecurityModalOpen: Boolean = false,
    val selectedTaskForSecurity: TaskEntity? = null
)

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository

    private val _uiState = MutableStateFlow(TaskDashboardUiState())
    val uiState: StateFlow<TaskDashboardUiState> = _uiState.asStateFlow()

    val allTasks: StateFlow<List<TaskEntity>>

    val filteredTasks: StateFlow<List<TaskEntity>>

    init {
        val database = TaskSecureDatabase.getDatabase(application)
        repository = TaskRepository(database.taskDao())

        allTasks = repository.allTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        filteredTasks = combine(allTasks, _uiState) { tasks, state ->
            tasks.filter { task ->
                val matchesCategory = state.selectedCategory == "All" || task.category.equals(state.selectedCategory, ignoreCase = true)
                val matchesStatus = state.selectedStatus == "All" || task.status.equals(state.selectedStatus, ignoreCase = true)
                val matchesQuery = state.searchQuery.isBlank() ||
                        task.title.contains(state.searchQuery, ignoreCase = true) ||
                        task.description.contains(state.searchQuery, ignoreCase = true)
                matchesCategory && matchesStatus && matchesQuery
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed default tasks if empty
        viewModelScope.launch {
            val initial = repository.allTasks.first()
            if (initial.isEmpty()) {
                seedInitialTasks()
            }
        }
    }

    private suspend fun seedInitialTasks() {
        repository.addTask(
            title = "AES-256 Vault Keys Inspection",
            description = "Audit end-to-end task encryption keys and verify HMAC signature validity.",
            status = "IN_PROGRESS",
            priority = "HIGH",
            category = "Security",
            isEncrypted = true
        )
        repository.addTask(
            title = "SOC2 Compliance Verification",
            description = "Review automated audit logs and encrypted database snapshot backups.",
            status = "TODO",
            priority = "HIGH",
            category = "Compliance",
            isEncrypted = true
        )
        repository.addTask(
            title = "Database Partition Migration",
            description = "Migrate task tables to zero-trust encrypted partitions with KSP.",
            status = "COMPLETED",
            priority = "MEDIUM",
            category = "Engineering",
            isEncrypted = true
        )
        repository.addTask(
            title = "Server Operational Health Check",
            description = "Verify TLS 1.3 network transport security and background worker health.",
            status = "TODO",
            priority = "LOW",
            category = "Operations",
            isEncrypted = true
        )
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setCategoryFilter(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setStatusFilter(status: String) {
        _uiState.value = _uiState.value.copy(selectedStatus = status)
    }

    fun openAddTaskDialog() {
        _uiState.value = _uiState.value.copy(isAddTaskDialogOpen = true)
    }

    fun closeAddTaskDialog() {
        _uiState.value = _uiState.value.copy(isAddTaskDialogOpen = false)
    }

    fun openSecurityModal(task: TaskEntity?) {
        _uiState.value = _uiState.value.copy(isSecurityModalOpen = true, selectedTaskForSecurity = task)
    }

    fun closeSecurityModal() {
        _uiState.value = _uiState.value.copy(isSecurityModalOpen = false, selectedTaskForSecurity = null)
    }

    fun addNewTask(
        title: String,
        description: String,
        category: String,
        priority: String,
        isEncrypted: Boolean
    ) {
        viewModelScope.launch {
            repository.addTask(
                title = title,
                description = description,
                status = "TODO",
                priority = priority,
                category = category,
                isEncrypted = isEncrypted
            )
            closeAddTaskDialog()
        }
    }

    fun updateTaskStatus(task: TaskEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateTask(task.copy(status = newStatus))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
