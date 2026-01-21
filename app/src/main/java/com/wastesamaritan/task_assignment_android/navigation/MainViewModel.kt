package com.wastesamaritan.task_assignment_android.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastesamaritan.task_assignment_android.auth.domain.repository.AuthRepository
import com.wastesamaritan.task_assignment_android.auth.domain.usecases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    // Initialize login state immediately
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
            initialValue = false
        )
    
    init {
        // Read initial login state synchronously
        viewModelScope.launch {
            _isLoggedIn.value = authRepository.isLoggedInSync()
        }
        // Then observe changes
        viewModelScope.launch {
            authRepository.isLoggedIn().collect { loggedIn ->
                _isLoggedIn.value = loggedIn
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = false
)