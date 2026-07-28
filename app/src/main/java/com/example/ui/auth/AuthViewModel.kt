package com.example.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.TaskSecureDatabase
import com.example.data.db.UserEntity
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AuthMode {
    SIGN_UP,
    SIGN_IN
}

data class AuthUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val termsAccepted: Boolean = false,
    val authMode: AuthMode = AuthMode.SIGN_UP,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    val currentUser: StateFlow<UserEntity?>

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        val database = TaskSecureDatabase.getDatabase(application)
        repository = UserRepository(database.userDao())
        currentUser = repository.currentUser.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }

    fun onFullNameChange(name: String) {
        _uiState.value = _uiState.value.copy(fullName = name, errorMessage = null)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun toggleTermsAccepted() {
        _uiState.value = _uiState.value.copy(termsAccepted = !_uiState.value.termsAccepted)
    }

    fun setAuthMode(mode: AuthMode) {
        _uiState.value = _uiState.value.copy(authMode = mode, errorMessage = null)
    }

    fun registerAccount() {
        val state = _uiState.value
        if (state.fullName.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter your full name.")
            return
        }
        if (state.email.isBlank() || !state.email.contains("@")) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid work email.")
            return
        }
        if (state.password.length < 8) {
            _uiState.value = state.copy(errorMessage = "Password must be at least 8 characters long.")
            return
        }
        if (!state.termsAccepted) {
            _uiState.value = state.copy(errorMessage = "You must agree to the Terms of Service & Privacy Policy.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)
            try {
                // Password hash simulation
                val dummyHash = "AES256_HASH_${state.password.hashCode()}"
                repository.registerUser(state.fullName, state.email, dummyHash)
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Registration failed."
                )
            }
        }
    }

    fun signIn() {
        val state = _uiState.value
        if (state.email.isBlank() || !state.email.contains("@")) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid email address.")
            return
        }
        if (state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter your password.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)
            val success = repository.loginUser(state.email)
            if (success) {
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            } else {
                // If user doesn't exist, create a demo account directly for smooth experience
                val dummyHash = "AES256_HASH_${state.password.hashCode()}"
                val nameFromEmail = state.email.substringBefore("@").replace(".", " ").capitalize()
                repository.registerUser(
                    fullName = if (nameFromEmail.isBlank()) "Task User" else nameFromEmail,
                    workEmail = state.email,
                    passwordHash = dummyHash
                )
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            }
        }
    }

    fun socialLogin(provider: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val dummyEmail = "user@$provider.com".lowercase()
            val dummyName = "Security Specialist ($provider)"
            repository.registerUser(dummyName, dummyEmail, "SOCIAL_OAUTH_TOKEN")
            _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState()
        }
    }
}
