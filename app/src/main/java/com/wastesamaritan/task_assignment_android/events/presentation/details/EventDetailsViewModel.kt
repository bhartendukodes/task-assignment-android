package com.wastesamaritan.task_assignment_android.events.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.events.domain.usecases.GetEventDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val eventId: Int = savedStateHandle.get<Int>("eventId") ?: 0
    
    private val _uiState = MutableStateFlow<EventDetailsUiState>(EventDetailsUiState.Loading)
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()
    
    init {
        loadEventDetails()
    }
    
    private fun loadEventDetails() {
        viewModelScope.launch {
            _uiState.value = EventDetailsUiState.Loading
            
            when (val result = getEventDetailsUseCase(eventId)) {
                is ApiResult.Success -> {
                    _uiState.value = EventDetailsUiState.Success(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = EventDetailsUiState.Error(
                        result.exception.message ?: "Failed to load event details"
                    )
                }
                is ApiResult.Loading -> {
                    // Already handled above
                }
            }
        }
    }
    
    fun refresh() {
        loadEventDetails()
    }
}