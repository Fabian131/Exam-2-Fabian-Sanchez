package com.moviles.paninisupport.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: String,
    val name: String,
    val email: String,
    val role: String
)