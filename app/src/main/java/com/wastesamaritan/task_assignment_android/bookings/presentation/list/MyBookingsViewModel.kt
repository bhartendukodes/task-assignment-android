package com.wastesamaritan.task_assignment_android.bookings.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastesamaritan.task_assignment_android.bookings.domain.usecases.GetMyBookingsUseCase
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBookingsViewModel @Inject constructor(
    private val getMyBookingsUseCase: GetMyBookingsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<MyBookingsUiState>(MyBookingsUiState.Loading)
    val uiState: StateFlow<MyBookingsUiState> = _uiState.asStateFlow()
    
    init {
        loadBookings()
    }
    
    private fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = MyBookingsUiState.Loading
            
            when (val result = getMyBookingsUseCase()) {
                is ApiResult.Success -> {
                    _uiState.value = MyBookingsUiState.Success(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = MyBookingsUiState.Error(
                        result.exception.message ?: "Failed to load bookings"
                    )
                }
                is ApiResult.Loading -> {
                    // Already handled above
                }
            }
        }
    }
    
    fun refresh() {
        loadBookings()
    }
}