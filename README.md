# Annotation-based DataStore for Android

A type-safe and easy-to-use data storage solution for Android applications using annotations.

## Quick Start

```kotlin
@Serializable
@DataStore("user_preferences")
data class UserPreferences(
    val theme: Int = 0
)

// Read data
val userPreferences: Flow<UserPreferences> = context.readFromUserPreferences()

// Write data
context.writeToUserPreferences(UserPreferences(theme = 1))
```
