# Event Flow with StateFlow

## How it works

`MockTickets` maintains a `MutableStateFlow<List<TicketResponse>>`. When the list is modified, the StateFlow automatically emits the new value to all observers.

## Ticket Creation

1. `CreateTicketScreen` sends action to `CreateTicketViewModel`
2. `CreateTicketViewModel` calls `TicketRepository.createTicket()`
3. `MockTickets.create()` adds the ticket to the StateFlow
4. `TicketListViewModel` receives the new list automatically
5. `TicketListScreen` recomposes without manual reload

## Priority Change

1. `TicketDetailScreen` sends action to `TicketDetailViewModel`
2. `TicketDetailViewModel` calls `TicketRepository.updatePriority()`
3. `MockTickets.updatePriority()` updates the ticket in the StateFlow
4. The list is reordered by priority automatically
5. `TicketListScreen` shows the ticket in its new position

## Why StateFlow

- No complex EventBus required
- Native Compose integration
- Automatic UI updates
- Easy to test