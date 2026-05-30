package com.moviles.paninisupport.core

object AppConstants {
    const val LOG_TAG = "PaniniSupport"

    object Api {
        const val BASE_URL = "http://10.0.2.2:8080/"

        object Paths {
            const val AUTH_LOGIN = "api/auth/login"
            const val TICKETS = "api/tickets"
            const val TICKET_BY_ID = "api/tickets/{id}"
            const val TICKET_STATUS = "api/tickets/{id}/status"
            const val TICKET_PRIORITY = "api/tickets/{id}/priority"
        }
    }

    object FeatureFlags {
        const val ENABLE_CREATE_TICKET = "ENABLE_CREATE_TICKET"
        const val ENABLE_PRIORITY_UPDATE = "ENABLE_PRIORITY_UPDATE"
    }
}