package com.wastesamaritan.task_assignment_android.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastesamaritan.task_assignment_android.auth.domain.usecases.RegisterUseCase
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    
    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = null,
            error = null
        )
    }
    
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null,
            error = null
        )
    }
    
    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            error = null
        )
    }
    
    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null,
            error = null
        )
    }
    
    fun register() {
        val currentState = _uiState.value
        
        // Validate passwords match
        if (currentState.password != currentState.confirmPassword) {
            _uiState.value = currentState.copy(
                confirmPasswordError = "Passwords do not match"
            )
            return
        }
        
        // Clear previous errors
        _uiState.value = currentState.copy(
            isLoading = true,
            error = null,
            nameError = null,
            emailError = null,
            passwordError = null,
            confirmPasswordError = null
        )
        
        viewModelScope.launch {
            when (val result = registerUseCase(
                currentState.name,
                currentState.email,
                currentState.password
            )) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRegistrationSuccessful = true
                    )
                }
                is ApiResult.Error -> {
                    val errorMessage = result.exception.message ?: "Registration failed"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
                is ApiResult.Loading -> {
                    // Already handled above
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}