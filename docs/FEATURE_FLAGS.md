# Feature Flags

## Flags Implemented

```kotlin
ENABLE_TICKET_CREATION  // Controls FAB and ticket creation
ENABLE_PRIORITY_UPDATE  // Controls priority update
```

## Usage

In UI, check the flag before showing functionality:

```kotlin
if (FeatureFlags.isEnabled(FeatureFlags.ENABLE_TICKET_CREATION)) {
    // show FAB
}

if (FeatureFlags.isEnabled(FeatureFlags.ENABLE_PRIORITY_UPDATE)) {
    // show priority selector
}
```

## Purpose

- Fast internal testing
- Disable functionality without code changes
- Preparation for Remote Config in future phase

## Adding a New Flag

1. Add constant in `util/FeatureFlags.kt`
2. Use `FeatureFlags.isEnabled(NEW_FLAG)` where needed