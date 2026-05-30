package com.moviles.paninisupport.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moviles.paninisupport.data.AppContainer
import com.moviles.paninisupport.data.remote.dto.Priority
import com.moviles.paninisupport.data.remote.dto.TicketResponse
import com.moviles.paninisupport.data.remote.dto.TicketStatus
import com.moviles.paninisupport.data.repository.ApiResult
import com.moviles.paninisupport.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TicketDetailUiState(
    val isLoading: Boolean = true,
    val ticket: TicketResponse? = null,
    val errorMessage: String? = null
)

class TicketDetailViewModel(
    private val ticketRepository: TicketRepository,
    private val ticketId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketDetailUiState())
    val uiState: StateFlow<TicketDetailUiState> = _uiState.asStateFlow()

    init {
        loadTicket()
    }

    private fun loadTicket() {
        val ticket = ticketRepository.getTicketById(ticketId)
        _uiState.value = TicketDetailUiState(
            isLoading = false,
            ticket = ticket,
            errorMessage = if (ticket == null) "Ticket not found" else null
        )
    }

    fun updateStatus(newStatus: TicketStatus) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = ticketRepository.updateStatus(ticketId, newStatus)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, ticket = result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }

    fun updatePriority(newPriority: Priority) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = ticketRepository.updatePriority(ticketId, newPriority)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, ticket = result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
}

class TicketDetailViewModelFactory(
    private val ticketRepository: TicketRepository,
    private val ticketId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TicketDetailViewModel(ticketRepository, ticketId) as T
    }
}