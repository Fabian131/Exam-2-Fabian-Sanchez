# Mock Data

## Overview

The application uses mock data that represents realistic enterprise scenarios related to FIFA World Cup 2026 album distribution in Costa Rica. Data is stored in memory using `MutableStateFlow` for reactive updates.

## Data Structure

### TicketResponse
```kotlin
data class TicketResponse(
    val id: String,
    val title: String,
    val description: String,
    val providerName: String,
    val category: TicketCategory,
    val priority: Priority,
    val status: TicketStatus,
    val createdAt: String,
    val updatedAt: String,
    val reportedBy: String,
    val location: String
)
```

## Enums

### Priority
- `CRITICAL` - Immediate attention required
- `HIGH` - Important, resolve soon
- `MEDIUM` - Normal priority
- `LOW` - Can wait

### TicketStatus
- `OPEN` - New ticket, not started
- `IN_PROGRESS` - Being worked on
- `RESOLVED` - Issue fixed
- `CLOSED` - Confirmed resolution

### TicketCategory
- `INVENTORY` - Stock count discrepancies
- `DISTRIBUTION` - Delivery issues
- `SUPPLIER` - Supplier-related problems
- `DAMAGED_PRODUCT` - Physical damage
- `DELIVERY_DELAY` - Late deliveries
- `OTHER` - Miscellaneous

## Sample Tickets

The mock data includes 5 realistic tickets:

| ID | Title | Priority | Status | Category |
|----|-------|----------|--------|----------|
| 1 | Missing packages in regional batch | HIGH | OPEN | INVENTORY |
| 2 | Delivery delay to Sarapiqui points | CRITICAL | IN_PROGRESS | DELIVERY_DELAY |
| 3 | Boxes with moisture damaged packages | MEDIUM | OPEN | DAMAGED_PRODUCT |
| 4 | Supplier not confirming requested restock | HIGH | OPEN | SUPPLIER |
| 5 | Inventory count error central warehouse | MEDIUM | IN_PROGRESS | INVENTORY |

## Context

Mock data reflects real operational issues in Costa Rica distribution:
- **Route GAM-03**: San José metropolitan area
- **Sarapiquí**: Remote northern region with logistics challenges
- **Regional distributors**: La Sabana, Caribe Norte, Heredia Centro, Alajuela Centro
- **Central Warehouse**: Main distribution hub

## Why Realistic Data

Generic data like "Test Ticket #1" would:
- Not demonstrate business context
- Make UI testing meaningless
- Fail to showcase domain understanding

Realistic data allows:
- Meaningful priority ordering demonstration
- Category filtering preparation
- Provider relationship展示

## Data Source

`MockTickets.kt` uses `MutableStateFlow` to enable reactive updates:

```kotlin
object MockTickets {
    private val _tickets = MutableStateFlow(initialTickets())
    val tickets: StateFlow<List<TicketResponse>> = _tickets.asStateFlow()

    fun create(request: CreateTicketRequest): TicketResponse { ... }
    fun updateStatus(id: String, newStatus: TicketStatus): TicketResponse? { ... }
    fun updatePriority(id: String, newPriority: Priority): TicketResponse? { ... }
}
```

## Backend Preparation

When connecting real backend:
1. Replace `MockTickets` operations with API calls
2. Maintain same data models (DTOs already defined)
3. Repository interface remains unchanged for UI
