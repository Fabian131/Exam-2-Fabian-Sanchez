# Event-Driven Architecture (StateFlow)

## 1. Overview

The application implements a fully reactive, event-driven architecture using Kotlin's **StateFlow**. This approach guarantees that UI components automatically recompose in response to underlying data mutations without requiring manual refreshes, custom event buses, or imperative UI updates.

## 2. The Reactive Loop

```text
[MockTicketDataSource] (_tickets: MutableStateFlow)
           │
           │ emits updated List<Ticket>
           ▼
[TicketRepository] (getTicketsFlow)
           │
           │ maps & sorts by Priority -> Flow<List<Ticket>>
           ▼
[TicketListViewModel] (viewModelScope.launch)
           │
           │ collects via collectLatest -> updates _uiState
           ▼
[TicketListScreen] (Compose)
           │
           │ observes via collectAsStateWithLifecycle()
           ▼
    Automatic Recomposition

```

## 3. Key Components Integration

### Data Source Level

The `MockTicketDataSource` acts as the single source of truth in memory. Every mutation (create, update) modifies the `_tickets` flow, triggering a downstream cascade.

```kotlin
object MockTicketDataSource {
    private val _tickets = MutableStateFlow<List<Ticket>>(initialMockData())
    val tickets: StateFlow<List<Ticket>> = _tickets.asStateFlow()

    fun createTicket(ticket: Ticket) {
        val currentList = _tickets.value.toMutableList()
        currentList.add(0, ticket) // Add to top
        _tickets.value = currentList // Emits new state automatically
    }

    fun updatePriority(id: String, newPriority: Priority) {
        _tickets.value = _tickets.value.map {
            if (it.id == id) it.copy(priority = newPriority) else it
        }
    }
}

```

### Repository Level

The repository intercepts the flow to apply business logic, such as sorting, ensuring the UI always receives the data in the correct order.

```kotlin
class TicketRepository(private val dataSource: MockTicketDataSource) {
    fun getTicketsFlow(): Flow<List<Ticket>> {
        return dataSource.tickets.map { ticketList ->
            ticketList.sortedBy { it.priority.ordinal } // CRITICAL(0) -> LOW(3)
        }
    }
}

```

### ViewModel Level

The ViewModel collects the domain flow and maps it to a UI-specific state holder.

```kotlin
class TicketListViewModel(
    private val repository: TicketRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketListUiState(isLoading = true))
    val uiState: StateFlow<TicketListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTicketsFlow().collectLatest { tickets ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tickets = tickets
                )
            }
        }
    }
}

```

### UI Level

Compose observes the state lifecycle-safely. Any change in the data source instantly triggers a recomposition of the `LazyColumn`.

```kotlin
@Composable
fun TicketListScreen(viewModel: TicketListViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn {
        items(
            items = uiState.tickets,
            key = { it.id } // Optimizes recomposition
        ) { ticket ->
            TicketCard(ticket = ticket)
        }
    }
}

```

## 4. Evaluated Scenarios

### Scenario 1: Ticket Creation

1. User submits the form in `CreateTicketScreen`.
2. `TicketRepository` routes the creation to `MockTicketDataSource`.
3. The data source updates its `MutableStateFlow`.
4. The downstream `Flow` recalculates sorting and emits to `TicketListViewModel`.
5. The `LazyColumn` in the list screen recomposes instantly. The user navigates back and sees the new ticket without any manual `refresh()` call.

### Scenario 2: Dynamic Priority Update

1. User changes priority from `MEDIUM` to `CRITICAL` in `TicketDetailScreen`.
2. The mutation updates the specific ticket in the `MockTicketDataSource`.
3. The Repository's `.map` operator intercepts the new list and re-sorts it (moving the updated ticket to the top).
4. The UI state updates seamlessly.
5. **Result**: Upon returning to the list, the ticket has automatically repositioned itself to the top of the list.

## 5. Architectural Justification

| Strategy                             | Why it was chosen for this PoC                                                                                                                                                                                                                                    |
| ------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **StateFlow vs SharedFlow/EventBus** | `StateFlow` always holds the latest state. If the UI is in the background when a ticket is created, it will receive the latest state immediately upon resuming. `SharedFlow` or an EventBus might drop events if there are no active subscribers.                 |
| **Lifecycle Awareness**              | `collectAsStateWithLifecycle()` prevents the app from collecting flows when the app is in the background, saving resources and preventing crashes.                                                                                                                |
| **Future Backend Integration**       | When transitioning to a real backend, this reactive architecture remains intact. The `ApiService` can provide a flow (via WebSockets or periodic polling), and the UI will continue to react exactly the same way with zero changes required to the Compose code. |
