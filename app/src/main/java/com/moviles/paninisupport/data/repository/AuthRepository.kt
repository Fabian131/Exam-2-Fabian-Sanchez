package com.moviles.paninisupport.data.repository

import com.moviles.paninisupport.core.UserMessages
import com.moviles.paninisupport.data.remote.dto.LoginRequest
import com.moviles.paninisupport.data.remote.dto.LoginResponse
import kotlinx.coroutines.delay

object AuthRepository {
    private const val MOCK_EMAIL = "admin@gmail.com"
    private const val MOCK_PASSWORD = "Admin1234#"

    suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
        delay(800)
        return if (email.trim() == MOCK_EMAIL && password == MOCK_PASSWORD) {
            ApiResult.Success(
                LoginResponse(
                    id = "user-001",
                    name = "Admin Panini",
                    email = MOCK_EMAIL,
                    role = "support_agent"
                )
            )
        } else {
            ApiResult.Error(message = UserMessages.Auth.INVALID_CREDENTIALS, statusCode = 401)
        }
    }
}