# Feature Flags Strategy

## 1. Concept & Objective

Feature Flags (or toggles) are dynamic boolean variables used to enable or disable specific application functionalities at runtime without modifying the source code or redeploying the app. For the Panini Support PoC, they allow the logistics team to safely test new workflows internally and simulate administrative control over the app's capabilities.

## 2. Implemented Flags

The PoC includes two primary flags scoped to the business requirements:

| Flag Key                 | Purpose                                                                                                        | Default State |
| ------------------------ | -------------------------------------------------------------------------------------------------------------- | ------------- |
| `ENABLE_CREATE_TICKET`   | Controls whether operators can report new incidents. Toggles the visibility/availability of the creation flow. | `true`        |
| `ENABLE_PRIORITY_UPDATE` | Controls whether operators have permission to escalate or de-escalate ticket priorities in the detail view.    | `true`        |

## 3. Architecture Integration (Repository Pattern)

To maintain architectural consistency and avoid anti-patterns like global singletons, feature flags are managed by a dedicated `FeatureFlagRepository` injected via the `AppContainer`. It utilizes `MutableStateFlow` to ensure any toggle instantly triggers UI recomposition.

```kotlin
class FeatureFlagRepository {
    object Key {
        const val ENABLE_CREATE_TICKET = "ENABLE_CREATE_TICKET"
        const val ENABLE_PRIORITY_UPDATE = "ENABLE_PRIORITY_UPDATE"
    }

    private val _flags = MutableStateFlow(
        mapOf(
            Key.ENABLE_CREATE_TICKET to true,
            Key.ENABLE_PRIORITY_UPDATE to true
        )
    )
    val flags: StateFlow<Map<String, Boolean>> = _flags.asStateFlow()

    fun isEnabled(flag: String): Boolean = _flags.value[flag] ?: false

    fun toggle(flag: String) {
        val currentMap = _flags.value.toMutableMap()
        currentMap[flag] = !(currentMap[flag] ?: false)
        _flags.value = currentMap
    }
}

```

## 4. Reactive UI Implementation

The UI observes these flags lifecycle-safely. When a flag is toggled, the specific UI component recomposes automatically.

### Example: TicketListScreen (Hiding the Creation FAB)

```kotlin
// In TicketListViewModel
val featureFlags = featureFlagRepository.flags

// In TicketListScreen
val flags by viewModel.featureFlags.collectAsStateWithLifecycle()
val canCreate = flags[FeatureFlagRepository.Key.ENABLE_CREATE_TICKET] ?: false

Scaffold(
    floatingActionButton = {
        if (canCreate) { // Recomposes instantly if flag changes
            FloatingActionButton(onClick = onNavigateToCreate) {
                Icon(Icons.Default.Add, contentDescription = "Create Ticket")
            }
        }
    }
) { paddingValues ->
    // List implementation
}

```

### Example: TicketDetailScreen (Disabling the Priority Dropdown)

```kotlin
// In TicketDetailScreen
val flags by viewModel.featureFlags.collectAsStateWithLifecycle()
val canUpdatePriority = flags[FeatureFlagRepository.Key.ENABLE_PRIORITY_UPDATE] ?: false

PriorityDropdown(
    currentPriority = ticket.priority,
    enabled = canUpdatePriority, // Visually disables the component if false
    onPrioritySelected = { newPriority -> viewModel.updatePriority(newPriority) }
)

```

## 5. Technical Justification

| Decision                     | Business / Technical Value                                                                                                                                                                                           |
| ---------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **StateFlow Implementation** | Fulfills the "Event-driven communication" requirement natively. Toggling a flag instantly updates all screens observing it without manual refreshes.                                                                 |
| **Repository Pattern**       | Decouples the flags from UI logic. In the future, this repository can easily be swapped to fetch flags from a remote endpoint (e.g., Firebase Remote Config or LaunchDarkly) without touching a single Compose file. |
| **Fail-Safe Defaults**       | If a flag key is not found, `isEnabled` defaults to `false`, preventing unauthorized access to unreleased features.                                                                                                  |

## 6. How to Extend

To add a new flag (e.g., `ENABLE_TICKET_DELETION`):

1. Add the new constant to `FeatureFlagRepository.Key`.
2. Add it to the initial map state within the repository.
3. Expose it through the respective ViewModel (to maintain MVVM separation of concerns).
4. Observe the state in the Compose screen using `collectAsStateWithLifecycle()` and wrap the UI component conditionally.
