package com.arjun.slate.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_pin")

class DataStoreManager(context: Context) {

    private var appContext = context.applicationContext

    private object PreferenceKeys {
        val name = stringPreferencesKey("pin_key")
    }

    suspend fun savePinToDataStore(pin: String) {
        appContext.dataStore.edit { preferences ->
            preferences[PreferenceKeys.name] = pin
        }
    }

    val readPinFromDataStore: Flow<String> = appContext.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.name] ?: "none"
    }
}