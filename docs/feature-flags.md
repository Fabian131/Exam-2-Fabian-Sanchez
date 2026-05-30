# Feature Flags

## Concepto

Los Feature Flags son variables booleanas que permiten habilitar o deshabilitar funcionalidades de la aplicación sin necesidad de modificar código o redeployar. Esta estrategia es útil para pruebas internas, release progresivo y control de funcionalidades.

## Flags Implementados

### ENABLE_CREATE_TICKET
- **Propósito**: Controla si el botón FAB para crear tickets está visible
- **Ubicación en UI**: `TicketListScreen`
- **Default**: `true`
- **Uso**: Permite deshabilitar la creación de tickets durante mantenimiento o pruebas

### ENABLE_PRIORITY_UPDATE
- **Propósito**: Controla si el usuario puede cambiar la prioridad de un ticket
- **Ubicación en UI**: `TicketDetailScreen` (deshabilita el dropdown de prioridad)
- **Default**: `true`
- **Uso**: Permite deshabilitar actualización de prioridades durante periodos de revisión

## Implementación

### FeatureFlagRepository

```kotlin
object FeatureFlagRepository {
    private val _flags = MutableStateFlow(
        mapOf(
            AppConstants.FeatureFlags.ENABLE_CREATE_TICKET to true,
            AppConstants.FeatureFlags.ENABLE_PRIORITY_UPDATE to true
        )
    )
    val flags: StateFlow<Map<String, Boolean>> = _flags.asStateFlow()

    fun isEnabled(flag: String): Boolean {
        return _flags.value[flag] ?: false
    }

    fun toggle(flag: String) {
        val current = _flags.value[flag] ?: false
        _flags.value = _flags.value.toMutableMap().apply {
            this[flag] = !current
        }
    }

    fun setFlag(flag: String, enabled: Boolean) {
        _flags.value = _flags.value.toMutableMap().apply {
            this[flag] = enabled
        }
    }
}
```

## Uso en la UI

### En TicketListScreen
```kotlin
val canCreateTicket = AppContainer.featureFlagRepository.isEnabled(
    AppConstants.FeatureFlags.ENABLE_CREATE_TICKET
)

showFab = canCreateTicket
```

### En TicketDetailScreen
```kotlin
val canUpdatePriority = AppContainer.featureFlagRepository.isEnabled(
    AppConstants.FeatureFlags.ENABLE_PRIORITY_UPDATE
)
```

## Agregar Nuevos Flags

Para agregar un nuevo Feature Flag:
1. Definir la constante en `AppConstants.FeatureFlags`
2. Agregar el flag al mapa inicial en `FeatureFlagRepository`
3. Usar `isEnabled()` donde sea necesario

## Consideraciones

- Los flags están pensados para el alcance del PoC
- No se implementó persistencia (se pierden al reiniciar la app)
- Para producción, se recomendaría integrar con Firebase Remote Config