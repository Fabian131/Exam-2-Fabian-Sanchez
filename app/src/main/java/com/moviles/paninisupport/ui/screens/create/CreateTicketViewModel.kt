package com.moviles.paninisupport.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moviles.paninisupport.data.AppContainer
import com.moviles.paninisupport.data.CreateTicketRequest
import com.moviles.paninisupport.data.remote.dto.Priority
import com.moviles.paninisupport.data.remote.dto.TicketCategory
import com.moviles.paninisupport.data.repository.ApiResult
import com.moviles.paninisupport.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateTicketUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class CreateTicketViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState: StateFlow<CreateTicketUiState> = _uiState.asStateFlow()

    fun createTicket(
        title: String,
        description: String,
        providerName: String,
        location: String,
        category: TicketCategory,
        priority: Priority
    ) {
        if (title.isBlank() || description.isBlank() || providerName.isBlank()) {
            _uiState.value = CreateTicketUiState(errorMessage = "Please fill in all required fields")
            return
        }

        _uiState.value = CreateTicketUiState(isLoading = true)
        viewModelScope.launch {
            val request = CreateTicketRequest(
                title = title,
                description = description,
                priority = priority,
                category = category,
                providerName = providerName,
                reportedBy = "Admin Panini",
                location = location
            )
            when (val result = ticketRepository.createTicket(request)) {
                is ApiResult.Success -> {
                    _uiState.value = CreateTicketUiState(isSuccess = true)
                }
                is ApiResult.Error -> {
                    _uiState.value = CreateTicketUiState(errorMessage = result.message)
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class CreateTicketViewModelFactory(
    private val ticketRepository: TicketRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateTicketViewModel(ticketRepository) as T
    }
}