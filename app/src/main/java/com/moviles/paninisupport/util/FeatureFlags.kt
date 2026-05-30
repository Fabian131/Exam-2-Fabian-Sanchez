package com.moviles.paninisupport.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FeatureFlags {
    const val ENABLE_TICKET_CREATION = "ENABLE_TICKET_CREATION"
    const val ENABLE_PRIORITY_UPDATE = "ENABLE_PRIORITY_UPDATE"

    private val _flags = MutableStateFlow(
        mapOf(
            ENABLE_TICKET_CREATION to true,
            ENABLE_PRIORITY_UPDATE to true
        )
    )
    val flags: StateFlow<Map<String, Boolean>> = _flags.asStateFlow()

    fun isEnabled(flag: String): Boolean {
        return _flags.value[flag] ?: false
    }

    fun setFlag(flag: String, enabled: Boolean) {
        _flags.value = _flags.value.toMutableMap().apply {
            this[flag] = enabled
        }
    }

    fun toggle(flag: String) {
        val current = _flags.value[flag] ?: false
        setFlag(flag, !current)
    }
}