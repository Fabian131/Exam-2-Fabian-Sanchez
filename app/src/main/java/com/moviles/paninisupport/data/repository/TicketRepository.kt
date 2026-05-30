package com.moviles.paninisupport.data.repository

import com.moviles.paninisupport.data.CreateTicketRequest
import com.moviles.paninisupport.data.MockTickets
import com.moviles.paninisupport.data.remote.dto.Priority
import com.moviles.paninisupport.data.remote.dto.TicketResponse
import com.moviles.paninisupport.data.remote.dto.TicketStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object TicketRepository {
    fun getTicketsFlow(): Flow<List<TicketResponse>> {
        return MockTickets.tickets.map { tickets ->
            tickets.sortedByDescending { priorityOrder(it.priority) }
        }
    }

    fun getTicketById(id: String): TicketResponse? {
        return MockTickets.getById(id)
    }

    suspend fun createTicket(request: CreateTicketRequest): ApiResult<TicketResponse> {
        return try {
            val ticket = MockTickets.create(request)
            ApiResult.Success(ticket)
        } catch (e: Exception) {
            ApiResult.Error(message = "Could not create ticket")
        }
    }

    suspend fun updateStatus(id: String, newStatus: TicketStatus): ApiResult<TicketResponse> {
        return try {
            val ticket = MockTickets.updateStatus(id, newStatus)
            if (ticket != null) {
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(message = "Could not update status")
            }
        } catch (e: Exception) {
            ApiResult.Error(message = "Could not update status")
        }
    }

    suspend fun updatePriority(id: String, newPriority: Priority): ApiResult<TicketResponse> {
        return try {
            val ticket = MockTickets.updatePriority(id, newPriority)
            if (ticket != null) {
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(message = "Could not update priority")
            }
        } catch (e: Exception) {
            ApiResult.Error(message = "Could not update priority")
        }
    }

    private fun priorityOrder(priority: Priority): Int {
        return when (priority) {
            Priority.CRITICAL -> 4
            Priority.HIGH -> 3
            Priority.MEDIUM -> 2
            Priority.LOW -> 1
        }
    }
}