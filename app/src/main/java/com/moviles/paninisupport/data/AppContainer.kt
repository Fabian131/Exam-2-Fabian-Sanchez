package com.moviles.paninisupport.data

import com.moviles.paninisupport.data.repository.AuthRepository
import com.moviles.paninisupport.data.repository.TicketRepository

object AppContainer {
    val authRepository: AuthRepository by lazy { AuthRepository }
    val ticketRepository: TicketRepository by lazy { TicketRepository }
}