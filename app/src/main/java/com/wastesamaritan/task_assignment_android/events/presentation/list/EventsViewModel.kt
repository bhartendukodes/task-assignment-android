package com.wastesamaritan.task_assignment_android.events.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.events.domain.usecases.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()
    
    init {
        loadEvents()
    }
    
    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventsUiState.Loading
            
            when (val result = getEventsUseCase()) {
                is ApiResult.Success -> {
                    _uiState.value = EventsUiState.Success(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = EventsUiState.Error(
                        result.exception.message ?: "Failed to load events"
                    )
                }
                is ApiResult.Loading -> {
                    // Already handled above
                }
            }
        }
    }
    
    fun refresh() {
        loadEvents()
    }
}