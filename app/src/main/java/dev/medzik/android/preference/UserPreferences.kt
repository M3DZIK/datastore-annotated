package dev.medzik.android.preference

import dev.medzik.android.datastore.DataStore
import kotlinx.serialization.Serializable

@Serializable
@DataStore("user_preferences")
data class UserPreferences(
    val theme: Int = 0
)

@Serializable
@DataStore("user_preferences")
data class Settings(
    val some: String = "test"
)
