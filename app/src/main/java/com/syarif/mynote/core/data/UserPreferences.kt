package com.syarif.mynote.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("user_prefs")

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    val userTokenFlow: Flow<String?> = dataStore.data
        .catch { exception ->
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[USER_TOKEN]
        }

    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    val userToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_TOKEN]
    }

    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    suspend fun saveUser(token: String, name: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
            preferences[USER_NAME] = name
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
            preferences.remove(USER_NAME)
        }
    }

}
