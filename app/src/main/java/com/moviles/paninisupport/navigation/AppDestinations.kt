package com.moviles.paninisupport.navigation

object AppDestinations {
    const val LOGIN = "login"
    const val TICKET_LIST = "ticketList"
    const val TICKET_DETAIL = "ticketDetail"
    const val CREATE_TICKET = "createTicket"

    fun ticketDetailRoute(id: String): String = "$TICKET_DETAIL/$id"
}