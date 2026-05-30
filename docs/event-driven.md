# Comunicación Basada en Eventos

## Concepto

La aplicación implementa un sistema de comunicación reactiva basado en **StateFlow** de Kotlin. Este patrón permite que los cambios en los datos se propaguen automáticamente a todas las partes de la aplicación que observan esos datos, sin necesidad de recargar manualmente las pantallas.

## Cómo Funciona

### Flujo de Datos

```
MockTicketDataSource (MutableStateFlow)
        │
        ▼
TicketRepository (expone Flow)
        │
        ▼
TicketListViewModel (collects Flow)
        │
        ▼
TicketListScreen (LazyColumn se actualiza)
```

### Componentes Clave

1. **MockTicketDataSource**: Fuente de datos mock que usa `MutableStateFlow<List<Ticket>>`. Cuando se modifica la lista de tickets, el StateFlow emite el nuevo valor automáticamente.

2. **TicketRepository**: Expoine el flujo de datos a través de `getTicketsFlow()` que retorna un `Flow<List<TicketResponse>>`.

3. **TicketListViewModel**: Colecciona el Flow usando `collectLatest` y actualiza el estado del UI con los nuevos datos.

4. **TicketListScreen**: El UI reacciona a los cambios del ViewModel a través de `collectAsStateWithLifecycle()`.

## Escenarios Implementados

### Escenario 1: Creación de Tickets

Cuando un usuario crea un nuevo ticket:
1. `CreateTicketViewModel.createTicket()` llama a `TicketRepository.createTicket()`
2. `TicketRepository` llama a `MockTicketDataSource.create()`
3. `MockTicketDataSource` actualiza el `MutableStateFlow` con el nuevo ticket al inicio de la lista
4. El `StateFlow` emite el nuevo estado automáticamente
5. `TicketListViewModel` recibe el nuevo estado y actualiza `TicketListUiState`
6. `TicketListScreen` se redibuja con el nuevo ticket visible

**Resultado**: El nuevo ticket aparece inmediatamente en la lista sin que el usuario tenga que recargar la pantalla.

### Escenario 2: Actualización de Prioridad

Cuando un usuario cambia la prioridad de un ticket:
1. `TicketDetailViewModel.updatePriority()` llama a `TicketRepository.updatePriority()`
2. `TicketRepository` llama a `MockTicketDataSource.updatePriority()`
3. `MockTicketDataSource` actualiza el ticket y el `MutableStateFlow`
4. El `StateFlow` emite la lista actualizada
5. Los tickets se reordenan según prioridad (CRITICAL primero)
6. `TicketListScreen` muestra el ticket en su nueva posición

**Resultado**: El ticket se reposiciona automáticamente según su nueva prioridad.

## Beneficios

- **Desacoplamiento**: Los componentes no necesitan conocer directamente a otros
- **Reactividad**: La UI siempre muestra el estado actual de los datos
- **Mantenibilidad**: Agregar nuevas funcionalidades es simple y predecible
- **Testabilidad**: Los flujos pueden ser facilmente testeados

## Preparación para Backend Real

La estructura actual permite cambiar de datos mock a API real sin modificar la arquitectura:
1. Crear una implementación real de `ApiService`
2. Modificar `TicketRepository` para usar el API en lugar de `MockTicketDataSource`
3. Mantener la misma interfaz de `Flow` para la UI

No se requiere reorganizar la estructura del proyecto.