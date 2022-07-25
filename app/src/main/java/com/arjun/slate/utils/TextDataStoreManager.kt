package com.arjun.slate.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "text_data")

class TextsDataStoreManager(context: Context) {

    private var appContext = context.applicationContext

    private object PreferenceKeys {
        val name = stringPreferencesKey("text_key")
    }

    suspend fun saveTextToDataStore(pin: String) {
        appContext.dataStore.edit { preferences ->
            preferences[PreferenceKeys.name] = pin
        }
    }

    val readTextFromDataStore: Flow<String> = appContext.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.name] ?: ""
    }
}