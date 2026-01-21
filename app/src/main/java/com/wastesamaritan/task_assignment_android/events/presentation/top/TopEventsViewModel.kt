package com.wastesamaritan.task_assignment_android.events.presentation.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wastesamaritan.task_assignment_android.core.network.ApiResult
import com.wastesamaritan.task_assignment_android.events.domain.usecases.GetTopBookedEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopEventsViewModel @Inject constructor(
    private val getTopBookedEventsUseCase: GetTopBookedEventsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<TopEventsUiState>(TopEventsUiState.Loading)
    val uiState: StateFlow<TopEventsUiState> = _uiState.asStateFlow()
    
    init {
        loadTopEvents()
    }
    
    private fun loadTopEvents() {
        viewModelScope.launch {
            _uiState.value = TopEventsUiState.Loading
            
            when (val result = getTopBookedEventsUseCase()) {
                is ApiResult.Success -> {
                    _uiState.value = TopEventsUiState.Success(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = TopEventsUiState.Error(
                        result.exception.message ?: "Failed to load top events"
                    )
                }
                is ApiResult.Loading -> {
                    // Already handled above
                }
            }
        }
    }
    
    fun refresh() {
        loadTopEvents()
    }
}