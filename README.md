# Panini Support - Proof of Concept

Mobile application proof of concept for managing support tickets related to the FIFA World Cup 2026 album distribution in Costa Rica.

## Problem Context

Panini faces operational challenges with:
- Missing packages in regional batches
- Delivery delays to remote points (Sarapiquí)
- Supplier coordination issues
- Inventory discrepancies
- Damaged products during transport

Currently managed via email/spreadsheets, leading to lost tracking, duplicates, and delays.

## Solution

A mobile PoC that centralizes support ticket management with:
- Simulated authentication
- Ticket listing with priority ordering
- Ticket creation
- Status and priority updates
- Real-time list updates via StateFlow

## Technologies

| Technology | Purpose |
|------------|---------|
| **Kotlin** | Main language |
| **Jetpack Compose** | Modern declarative UI |
| **MVVM** | Architectural pattern |
| **Navigation Compose** | Screen navigation |
| **StateFlow** | Event-based reactive communication |
| **Retrofit** | Network layer (prepared for backend) |
| **Coroutines** | Asynchronous operations |

## Project Structure

```
Exam-2-Fabian-Sanchez/
├── app/                    # Android application
│   └── src/main/java/com/moviles/paninisupport/
│       ├── data/          # Data layer (MockTickets, DTOs)
│       ├── repository/    # Repository pattern
│       ├── ui/            # Compose screens & components
│       ├── navigation/    # Navigation graph
│       └── util/         # Feature flags
├── contracts/              # API contracts (YAML)
├── docs/                  # Technical documentation
├── video/                 # Demo video link
└── README.md
```

## Screens

1. **Login** - Simulated authentication
2. **Ticket List** - Shows tickets ordered by priority (CRITICAL first)
3. **Ticket Detail** - Full ticket information with status/priority updates
4. **Create Ticket** - Form to create new support tickets
5. **Feature Flags Settings** - Toggle functionality on/off

## Key Features

### Event-Driven Updates
When a ticket is created or updated, the list automatically refreshes without manual reload. This is achieved through StateFlow-based communication.

### Priority Ordering
Tickets are always sorted: CRITICAL → HIGH → MEDIUM → LOW. When priority changes, the ticket repositions automatically.

### Feature Flags
Runtime toggles control functionality:
- `ENABLE_TICKET_CREATION` - FAB behavior
- `ENABLE_PRIORITY_UPDATE` - Edit button behavior

## Login Credentials

- **Email**: admin@gmail.com
- **Password**: Admin1234#

## Build & Run

```bash
cd Exam-2-Fabian-Sanchez
./gradlew assembleDebug
```

Then install APK on device/emulator:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Backend Preparation

The networking layer is ready for real API integration:
- `RetrofitClient.kt` - Configured with base URL
- `ApiService.kt` - Endpoints defined
- `DTOs` - Request/Response models
- `ApiResult` - Standardized error handling

See `/docs/NETWORK_LAYER.md` for details.

## API Contracts

Located in `/contracts/`:
- `auth-login.yaml` - Authentication
- `get-tickets.yaml` - List tickets
- `get-ticket-by-id.yaml` - Get single ticket
- `create-ticket.yaml` - Create ticket
- `update-ticket-status.yaml` - Update status
- `update-ticket-priority.yaml` - Update priority

## Documentation

| File | Content |
|------|---------|
| `ARCHITECTURE.md` | Architecture decisions and layer structure |
| `EVENT_FLOW.md` | How StateFlow enables reactive updates |
| `FEATURE_FLAGS.md` | Feature flag implementation |
| `MOCK_DATA.md` | Realistic mock data explanation |
| `NETWORK_LAYER.md` | Retrofit preparation for backend |

## Design Decisions

| Decision | Justification |
|----------|---------------|
| **StateFlow for events** | Native Compose integration, no EventBus |
| **Repository pattern** | Easy to swap mock → real API |
| **No Room/Firebase** | PoC scope, mock data in memory |
| **No Clean Architecture** | Overkill for PoC, keep simple |
| **Feature Flags as StateFlow** | Runtime toggles trigger UI updates |

## Scope Limitations

- No real backend (mock data only)
- No persistence (data resets on app restart)
- No offline mode
- No push notifications
- No user management beyond login simulation

## Future Enhancements

- Real backend integration
- Room database for offline support
- Push notifications for ticket updates
- Image attachments for damage documentation
- Analytics dashboard
- Firebase Remote Config for feature flags
