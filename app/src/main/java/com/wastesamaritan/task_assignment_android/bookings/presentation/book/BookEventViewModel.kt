package com.wastesamaritan.task_assignment_android.bookings.presentation.book

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastesamaritan.task_assignment_android.bookings.domain.usecases.BookEventUseCase
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookEventViewModel @Inject constructor(
    private val bookEventUseCase: BookEventUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val eventId: Int = savedStateHandle.get<Int>("eventId") ?: 0
    
    private val _uiState = MutableStateFlow(BookEventUiState())
    val uiState: StateFlow<BookEventUiState> = _uiState.asStateFlow()
    
    fun bookEvent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            when (val result = bookEventUseCase(eventId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isBookingSuccessful = true
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Booking failed"
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