# Architecture

## MVVM Pattern

The application follows MVVM (Model-View-ViewModel) with Repository Pattern for data management.

## Layers

```
UI Layer (Screens, Components)
    ↓
ViewModel Layer (UiState, Actions)
    ↓
Repository Layer (TicketRepository)
    ↓
Data Layer (MockTickets, ApiService)
```

## Package Structure

```
com.moviles.paninisupport/
├── data/
│   ├── MockTickets.kt          ← Mock data source with StateFlow
│   └── repository/
│       ├── ApiResult.kt        ← Success/Error wrapper
│       └── TicketRepository.kt ← Central repository
├── data/remote/
│   ├── ApiService.kt           ← Retrofit interface
│   ├── RetrofitClient.kt       ← Retrofit client
│   └── dto/                    ← API DTOs
├── ui/screens/
│   ├── login/
│   ├── tickets/
│   ├── detail/
│   └── create/
├── util/FeatureFlags.kt        ← Simple Feature Flags
└── navigation/AppNavHost.kt
```

## Decisions

- **No Clean Architecture**: PoC scope does not justify additional layers
- **No Room/Firebase**: Mock data in memory
- **Manual DI**: Simple AppContainer instead of Hilt
- **StateFlow**: For reactive communication between layers

## Backend Preparation

The structure allows replacing `MockTickets` with real `ApiService` calls without reorganizing the project.