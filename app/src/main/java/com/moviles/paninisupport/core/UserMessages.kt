package com.moviles.paninisupport.core

object UserMessages {

    object LoginUi {
        const val EMAIL_LABEL = "Email"
        const val EMAIL_PLACEHOLDER = "admin@panini.com"
        const val PASSWORD_LABEL = "Password"
        const val BUTTON_LOGIN = "Login"
        const val BUTTON_LOADING = "Loading..."
    }

    object Auth {
        const val LOGIN_EMPTY_FIELDS = "Please fill in all fields"
        const val INVALID_CREDENTIALS = "Invalid email or password"
        const val LOGIN_FAILED = "Login failed"
        const val LOGIN_SUCCESS = "Login successful"
    }

    object Tickets {
        const val LOADING = "Loading tickets..."
        const val EMPTY = "No tickets available"
        const val FETCH_FAILED = "Could not load tickets"
        const val TICKET_CREATED = "Ticket created successfully"
        const val TICKET_UPDATED = "Ticket updated"
    }

    object CreateTicket {
        const val TITLE = "Create Ticket"
        const val TITLE_LABEL = "Title"
        const val TITLE_PLACEHOLDER = "Enter ticket title"
        const val DESCRIPTION_LABEL = "Description"
        const val DESCRIPTION_PLACEHOLDER = "Enter detailed description"
        const val SUPPLIER_LABEL = "Supplier"
        const val SUPPLIER_PLACEHOLDER = "Enter supplier name"
        const val CATEGORY_LABEL = "Category"
        const val PRIORITY_LABEL = "Priority"
        const val BUTTON_CREATE = "Create Ticket"
        const val BUTTON_CREATING = "Creating..."
        const val VALIDATION_ERROR = "Please fill in all required fields"
    }

    object TicketDetail {
        const val TITLE = "Ticket Detail"
        const val STATUS_LABEL = "Status"
        const val PRIORITY_LABEL = "Priority"
        const val CATEGORY_LABEL = "Category"
        const val SUPPLIER_LABEL = "Supplier"
        const val REPORTED_BY_LABEL = "Reported by"
        const val CREATED_AT_LABEL = "Created"
        const val UPDATED_AT_LABEL = "Updated"
        const val DESCRIPTION_LABEL = "Description"
    }

    object Navigation {
        const val BACK = "Back"
    }

    object Accessibility {
        const val BACK = "Back"
        const val CREATE_TICKET = "Create ticket"
    }

    object Network {
        const val COULD_NOT_CONNECT = "Could not connect to server"
    }
}