package com.moviles.paninisupport.data

import com.moviles.paninisupport.data.remote.dto.Priority
import com.moviles.paninisupport.data.remote.dto.TicketCategory
import com.moviles.paninisupport.data.remote.dto.TicketResponse
import com.moviles.paninisupport.data.remote.dto.TicketStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MockTickets {
    private val _tickets = MutableStateFlow(initialTickets())
    val tickets: StateFlow<List<TicketResponse>> = _tickets.asStateFlow()

    fun getAll(): List<TicketResponse> = _tickets.value

    fun getById(id: String): TicketResponse? = _tickets.value.find { it.id == id }

    fun create(request: CreateTicketRequest): TicketResponse {
        val now = java.time.Instant.now().toString()
        val ticket = TicketResponse(
            id = java.util.UUID.randomUUID().toString(),
            title = request.title,
            description = request.description,
            priority = request.priority,
            status = TicketStatus.OPEN,
            category = request.category,
            providerName = request.providerName,
            createdAt = now,
            updatedAt = now,
            reportedBy = request.reportedBy,
            location = request.location
        )
        _tickets.value = listOf(ticket) + _tickets.value
        return ticket
    }

    fun updateStatus(id: String, newStatus: TicketStatus): TicketResponse? {
        val ticket = getById(id) ?: return null
        val updated = ticket.copy(status = newStatus, updatedAt = java.time.Instant.now().toString())
        _tickets.value = _tickets.value.map { if (it.id == id) updated else it }
        return updated
    }

    fun updatePriority(id: String, newPriority: Priority): TicketResponse? {
        val ticket = getById(id) ?: return null
        val updated = ticket.copy(priority = newPriority, updatedAt = java.time.Instant.now().toString())
        _tickets.value = _tickets.value.map { if (it.id == id) updated else it }
        return updated
    }

    private fun initialTickets(): List<TicketResponse> = listOf(
        TicketResponse(
            id = "1",
            title = "Missing packages in regional batch",
            description = "Point of sale reports 120 packages less than confirmed dispatch for route GAM-03.",
            priority = Priority.HIGH,
            status = TicketStatus.OPEN,
            category = TicketCategory.INVENTORY,
            providerName = "Distribuidora La Sabana",
            createdAt = "2026-03-15T10:30:00Z",
            updatedAt = "2026-03-15T14:20:00Z",
            reportedBy = "Carlos Mendoza",
            location = "San Jose, GAM"
        ),
        TicketResponse(
            id = "2",
            title = "Delivery delay to Sarapiqui points",
            description = "Morning route did not arrive to three registered shops, affecting album sticker availability.",
            priority = Priority.CRITICAL,
            status = TicketStatus.IN_PROGRESS,
            category = TicketCategory.DELIVERY_DELAY,
            providerName = "Logistica Caribe Norte",
            createdAt = "2026-03-14T09:15:00Z",
            updatedAt = "2026-03-14T09:15:00Z",
            reportedBy = "Laura Sanchez",
            location = "Sarapiqui"
        ),
        TicketResponse(
            id = "3",
            title = "Boxes with moisture damaged packages",
            description = "Boxes with physical damage to packaging detected during weekly restock inventory reception.",
            priority = Priority.MEDIUM,
            status = TicketStatus.OPEN,
            category = TicketCategory.DAMAGED_PRODUCT,
            providerName = "Central de Distribucion Heredia",
            createdAt = "2026-03-13T16:45:00Z",
            updatedAt = "2026-03-13T16:45:00Z",
            reportedBy = "Roberto Jimenez",
            location = "Heredia Centro"
        ),
        TicketResponse(
            id = "4",
            title = "Supplier not confirming requested restock",
            description = "Shop reports no response to restock request generated more than 48 hours ago.",
            priority = Priority.HIGH,
            status = TicketStatus.OPEN,
            category = TicketCategory.SUPPLIER,
            providerName = "Punto Venta Alajuela Centro",
            createdAt = "2026-03-12T11:00:00Z",
            updatedAt = "2026-03-12T11:00:00Z",
            reportedBy = "Maria Gonzalez",
            location = "Alajuela Centro"
        ),
        TicketResponse(
            id = "5",
            title = "Inventory count error central warehouse",
            description = "Difference of 85 units between system record and physical count performed yesterday.",
            priority = Priority.MEDIUM,
            status = TicketStatus.IN_PROGRESS,
            category = TicketCategory.INVENTORY,
            providerName = "Bodega Central Regional",
            createdAt = "2026-03-11T08:30:00Z",
            updatedAt = "2026-03-14T17:00:00Z",
            reportedBy = "Ana Rodriguez",
            location = "Central Warehouse"
        )
    )
}

data class CreateTicketRequest(
    val title: String,
    val description: String,
    val priority: Priority,
    val category: TicketCategory,
    val providerName: String,
    val reportedBy: String,
    val location: String
)