# Networking Layer & API Contracts

## 1. Overview

Due to time and budget constraints, this PoC operates without a live backend. However, to ensure immediate scalability and a smooth transition for future engineers, the networking layer is fully structured. It implements the **Retrofit2** library and clearly defined Data Transfer Objects (DTOs), completely decoupled from the UI layer via the Repository pattern.

## 2. API Contracts

As requested by the business requirements, the API integration contracts are documented using OpenAPI/YAML standards.

Instead of fragmenting the documentation, all endpoints are centralized in a single file located at the root of the repository:
**Location:** `/contracts/tickets-api.yaml`

| Endpoint                     | Method  | Purpose                                    |
| ---------------------------- | ------- | ------------------------------------------ |
| `/api/auth/login`            | `POST`  | Authenticate logistics operators           |
| `/api/tickets`               | `GET`   | Retrieve the list of all tickets           |
| `/api/tickets/{id}`          | `GET`   | Retrieve full details of a specific ticket |
| `/api/tickets`               | `POST`  | Report a new logistics incident            |
| `/api/tickets/{id}/status`   | `PATCH` | Update the resolution stage of a ticket    |
| `/api/tickets/{id}/priority` | `PATCH` | Escalate or de-escalate ticket urgency     |

## 3. Core Components

### 3.1. Retrofit Client Setup (`RetrofitClient.kt`)

Configured to handle JSON serialization and ready to accept an interceptor for Bearer tokens in future iterations.

```kotlin
object RetrofitClient {
    // Placeholder URL. Replace during backend integration.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // .client(okHttpClientWithAuthInterceptor) // Ready for future use
            .build()
            .create(ApiService::class.java)
    }
}

```

### 3.2. API Service Interface (`ApiService.kt`)

Translates the YAML contracts into callable Kotlin suspend functions.

```kotlin
interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/tickets")
    suspend fun getTickets(): Response<List<TicketResponse>>

    @POST("api/tickets")
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<TicketResponse>

    @PATCH("api/tickets/{id}/status")
    suspend fun updateStatus(
        @Path("id") id: String,
        @Body request: UpdateStatusRequest
    ): Response<TicketResponse>

    @PATCH("api/tickets/{id}/priority")
    suspend fun updatePriority(
        @Path("id") id: String,
        @Body request: UpdatePriorityRequest
    ): Response<TicketResponse>
}

```

### 3.3. Data Transfer Objects (DTOs)

Located in `data/remote/dto/`. These mirror the exact schema definitions from the YAML contract.

```kotlin
// Requests
data class LoginRequest(val email: String, val password: String)
data class CreateTicketRequest(val title: String, val description: String, val supplier: String, val priority: String, val category: String)
data class UpdateStatusRequest(val status: String)
data class UpdatePriorityRequest(val priority: String)

// Responses
data class AuthResponse(val id: String, val name: String, val token: String)
data class TicketResponse(val id: String, val title: String, val status: String, val priority: String, val supplier: String)

```

## 4. State Management Integration

To handle network responses cleanly, the application uses a sealed class wrapper. This standardizes success/error states before they reach the ViewModel.

```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val exception: Throwable? = null) : ApiResult<Nothing>()
}

```

## 5. Handoff: How to Connect the Real API

When the backend is deployed, engineers must follow these steps. **No changes to the UI (`ui/`) or ViewModel layers are required.**

1. **Update Base URL:** Change `BASE_URL` in `RetrofitClient.kt`.
2. **Swap Data Source in Repository:** Inside `TicketRepository.kt`, replace the `MockTicketDataSource` calls with `ApiService` calls.
3. **Maintain Reactivity:** To keep the event-driven architecture intact, the repository should fetch data from the API and emit it into a private `MutableStateFlow`, returning it as an immutable `StateFlow` to the ViewModel.

**Migration Example:**

```kotlin
class TicketRepository(private val apiService: ApiService) {
    // 1. Maintain a StateFlow to keep UI reactive
    private val _ticketsFlow = MutableStateFlow<List<Ticket>>(emptyList())
    val ticketsFlow: StateFlow<List<Ticket>> = _ticketsFlow.asStateFlow()

    // 2. Fetch from real API and update the flow
    suspend fun fetchTickets() {
        try {
            val response = apiService.getTickets()
            if (response.isSuccessful) {
                // UI automatically recomposes when this value updates
                _ticketsFlow.value = response.body()?.map { it.toDomain() } ?: emptyList()
            }
        } catch (e: Exception) {
            // Handle network failure
        }
    }
}

```
