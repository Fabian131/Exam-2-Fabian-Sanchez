package com.moviles.paninisupport.ui.screens.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moviles.paninisupport.data.AppContainer
import com.moviles.paninisupport.data.remote.dto.TicketResponse
import com.moviles.paninisupport.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class TicketListUiState(
    val isLoading: Boolean = true,
    val tickets: List<TicketResponse> = emptyList(),
    val errorMessage: String? = null
)

class TicketListViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketListUiState())
    val uiState: StateFlow<TicketListUiState> = _uiState.asStateFlow()

    init {
        loadTickets()
    }

    private fun loadTickets() {
        viewModelScope.launch {
            ticketRepository.getTicketsFlow().collectLatest { tickets ->
                _uiState.value = TicketListUiState(
                    isLoading = false,
                    tickets = tickets
                )
            }
        }
    }
}

class TicketListViewModelFactory(
    private val ticketRepository: TicketRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TicketListViewModel(ticketRepository) as T
    }
}