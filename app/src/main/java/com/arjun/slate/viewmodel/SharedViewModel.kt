package com.arjun.slate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.arjun.slate.data.Text
import com.arjun.slate.data.TextDatabase
import com.arjun.slate.data.TextRepository
import com.arjun.slate.utils.DataStoreManager
import com.arjun.slate.utils.TextsDataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    // Texts RoomDB
    private val repository: TextRepository
    val allTexts: LiveData<List<Text>>

    init {
        val dao = TextDatabase.getDatabase(application).getTextDao()
        repository = TextRepository(dao)
        allTexts = repository.allTexts
    }

    fun insertText(text: Text) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(text)
        }

    // Text DataStore
    private val textDataStoreManager = TextsDataStoreManager(application)
    val readTextFromDataStore = textDataStoreManager.readTextFromDataStore.asLiveData()

    fun saveTextToDataStore(text: String) = viewModelScope.launch {
        textDataStoreManager.saveTextToDataStore(text)
    }

    // Pin DataStore
    private val pinDataStoreManager = DataStoreManager(application)
    val readPinFromDataStore = pinDataStoreManager.readPinFromDataStore.asLiveData()

    fun savePinToDataStore(pin: String) = viewModelScope.launch {
        pinDataStoreManager.savePinToDataStore(pin)
    }
}