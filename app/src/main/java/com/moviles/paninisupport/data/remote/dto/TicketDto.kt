package com.moviles.paninisupport.data.remote.dto

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

enum class TicketCategory {
    INVENTORY,
    DISTRIBUTION,
    SUPPLIER,
    DAMAGED_PRODUCT,
    DELIVERY_DELAY,
    OTHER
}

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

data class CreateTicketRequest(
    val title: String,
    val description: String,
    val priority: Priority,
    val category: TicketCategory,
    val providerName: String,
    val reportedBy: String,
    val location: String
)

data class UpdateStatusRequest(
    val status: TicketStatus
)

data class UpdatePriorityRequest(
    val priority: Priority
)