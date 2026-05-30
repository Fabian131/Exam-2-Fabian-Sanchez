package com.moviles.paninisupport.data.remote

import com.moviles.paninisupport.core.AppConstants
import com.moviles.paninisupport.data.remote.dto.CreateTicketRequest
import com.moviles.paninisupport.data.remote.dto.LoginRequest
import com.moviles.paninisupport.data.remote.dto.LoginResponse
import com.moviles.paninisupport.data.remote.dto.TicketResponse
import com.moviles.paninisupport.data.remote.dto.TicketStatus
import com.moviles.paninisupport.data.remote.dto.Priority
import com.moviles.paninisupport.data.remote.dto.UpdatePriorityRequest
import com.moviles.paninisupport.data.remote.dto.UpdateStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST(AppConstants.Api.Paths.AUTH_LOGIN)
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET(AppConstants.Api.Paths.TICKETS)
    suspend fun getTickets(): Response<List<TicketResponse>>

    @GET(AppConstants.Api.Paths.TICKET_BY_ID)
    suspend fun getTicketById(@Path("id") id: String): Response<TicketResponse>

    @POST(AppConstants.Api.Paths.TICKETS)
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<TicketResponse>

    @PATCH(AppConstants.Api.Paths.TICKET_STATUS)
    suspend fun updateTicketStatus(
        @Path("id") id: String,
        @Body request: UpdateStatusRequest
    ): Response<TicketResponse>

    @PATCH(AppConstants.Api.Paths.TICKET_PRIORITY)
    suspend fun updateTicketPriority(
        @Path("id") id: String,
        @Body request: UpdatePriorityRequest
    ): Response<TicketResponse>
}