package com.moviles.paninisupport.util

object FeatureFlags {
    const val ENABLE_TICKET_CREATION = "ENABLE_TICKET_CREATION"
    const val ENABLE_PRIORITY_UPDATE = "ENABLE_PRIORITY_UPDATE"

    private val flags = mapOf(
        ENABLE_TICKET_CREATION to true,
        ENABLE_PRIORITY_UPDATE to true
    )

    fun isEnabled(flag: String): Boolean {
        return flags[flag] ?: false
    }
}