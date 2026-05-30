# Architecture & Technical Decisions

## 1. Overview

The **Panini Support** application follows an **MVVM (Model-View-ViewModel)** architecture supported by the **Repository Pattern**. Designed as a Proof of Concept (PoC) for the FIFA World Cup 2026 logistics team, this architecture prioritizes maintainability, fast iteration, and a seamless future transition to a real backend, strictly adhering to the project's time and budget constraints.

## 2. Core Architectural Principles

* **Simplicity & Pragmatism:** Avoiding excessive abstraction (like full Clean Architecture Use Cases) which would introduce unjustified complexity for a PoC scope.
* **Reactive UI:** Leveraging Kotlin's `StateFlow` to build an event-driven system where UI components react to data mutations automatically, eliminating manual refreshes.
* **Minimalist Interface Components:** UI components (such as `TicketCard` and status chips) are designed with a minimalist aesthetic to prevent cognitive overload for logistics operators, ensuring fast and clear data reading.
* **Future-Proof Networking:** Fully structured networking layers (Retrofit, DTOs, API Contracts) ready for backend integration without requiring architectural refactoring.

## 3. Architecture Layers

```text
┌────────────────────────────────────────────────────────┐
│                      UI Layer (Compose)                │
│    Screens: Login, TicketList, Detail, CreateTicket    │
│    Reacts to UiState and propagates user intents       │
└───────────────────────────┬────────────────────────────┘
                            │ observes (StateFlow)
┌───────────────────────────▼────────────────────────────┐
│                    ViewModel Layer                     │
│    Transforms repository data into UI State            │
│    Handles business logic and UI events                │
└───────────────────────────┬────────────────────────────┘
                            │ calls
┌───────────────────────────▼────────────────────────────┐
│                    Repository Layer                    │
│    TicketRepository, AuthRepository, FeatureFlags      │
│    Single Source of Truth / Data Mediation             │
└───────────────────────────┬────────────────────────────┘
                            │ interacts
┌───────────────────────────▼────────────────────────────┐
│                      Data Layer                        │
│ ┌─────────────────┐ ┌────────────────────────────────┐ │
│ │  Local (Mock)   │ │         Remote (Future)        │ │
│ │ MockDataSource  │ │ ApiService, RetrofitClient     │ │
│ └─────────────────┘ └────────────────────────────────┘ │
└────────────────────────────────────────────────────────┘

```

## 4. Package Structure

The package organization strictly follows separation of concerns:

```text
com.moviles.paninisupport/
├── core/
│   ├── AppConstants.kt          # API URLs, technical constants
│   └── UserMessages.kt          # Centralized UI strings
├── data/
│   ├── AppContainer.kt          # Manual Dependency Injection container
│   ├── local/
│   │   └── MockTicketDataSource.kt # In-memory reactive state (MutableStateFlow)
│   ├── remote/
│   │   ├── ApiService.kt        # Retrofit interface (Endpoints mapping to YAML contracts)
│   │   ├── RetrofitClient.kt    # Configured networking client
│   │   └── dto/                 # Data Transfer Objects (AuthDto, TicketDto)
│   └── repository/
│       ├── ApiResult.kt         # Sealed class for Success/Error handling
│       ├── AuthRepository.kt
│       ├── TicketRepository.kt
│       └── FeatureFlagRepository.kt
├── navigation/
│   ├── AppDestinations.kt       # Route definitions
│   └── AppNavHost.kt            # Compose Navigation graph
└── ui/
    ├── components/              # Reusable minimalist UI elements (Cards, Chips, Buttons)
    ├── screens/                 # Organized by feature (login, tickets, detail, create)
    └── theme/                   # Material 3 Theme, Typography, Panini Color Palette

```

## 5. Key Technical Decisions

| Decision | Justification |
| --- | --- |
| **StateFlow over EventBus** | Native Coroutines integration. `MockTicketDataSource` holds a `MutableStateFlow<List<Ticket>>`. ViewModels collect this flow, meaning any creation or priority update instantly triggers UI recomposition. No explicit callbacks needed. |
| **Repository Pattern** | Acts as an abstraction layer. The ViewModel doesn't know if data comes from memory (`MockTicketDataSource`) or the network (`ApiService`), fulfilling the "easy to continue by other engineers" requirement. |
| **Manual DI (`AppContainer`)** | Frameworks like Hilt or Dagger introduce boilerplate and build-time overhead. A manual container provides dependency injection benefits (testability, decoupling) while keeping the PoC lightweight. |
| **Feature Flags via StateFlow** | Handled in `FeatureFlagRepository`. Enables instant, runtime toggling of features (`ENABLE_CREATE_TICKET`, `ENABLE_PRIORITY_UPDATE`) directly affecting UI composition without rebuilding the app. |
| **Mocking Realistic Scenarios** | Mock data explicitly models business domain entities (e.g., missing inventory from QuadGraphics, routing errors from DHL) rather than generic `Test1` strings, aligning with enterprise PoC standards. |

## 6. Backend Integration Readiness

Although the app operates on mock data, the transition to a live backend requires **zero structural changes**. The architecture is pre-wired:

1. **API Contracts:** `/contracts/tickets-api.yaml` defines the exact specifications.
2. **DTOs Implementation:** Request and Response models (`data/remote/dto/`) are already mapped to the YAML contract.
3. **Networking Client:** `RetrofitClient.kt` and `ApiService.kt` are fully declared.
4. **Handoff Steps:** To activate the backend, an engineer simply needs to:
* Update `Api.BASE_URL` in `AppConstants.kt`.
* Swap the `MockTicketDataSource` calls inside `TicketRepository` with the corresponding `ApiService` suspend functions.
* The UI and ViewModel layers will remain completely untouched.



---

*Note: For a detailed breakdown of the reactive flow mechanics, please refer to `event-driven.md`.*