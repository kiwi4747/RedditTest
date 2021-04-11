package com.example.reddittest.ui.main.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserPreferencesRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val USER_PREFERENCES_NAME = "user_preferences"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

    private val LAST_QUERY_KEY = stringPreferencesKey("LAST_QUERY_KEY")
    val lastQueryFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_QUERY_KEY] ?: ""
        }

    suspend fun setLastQuery(query: String) {
        context.dataStore.edit { settings ->
            settings[LAST_QUERY_KEY] = query
        }
    }

}
