package com.arjun.slate.data

import androidx.lifecycle.LiveData

class TextRepository(private val textDao: TextDao) {

    val allTexts: LiveData<List<Text>> = textDao.getAllTexts()

    fun insert(text: Text) {
        textDao.insert(text)
    }

    fun delete(text: Text) {
        textDao.delete(text)
    }
}