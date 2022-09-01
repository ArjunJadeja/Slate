package com.arjun.slate.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TextDao {

    @Query("SELECT * FROM text_table ORDER BY ID DESC")
    fun getAllTexts(): LiveData<List<Text>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(text: Text)

    @Delete
    fun delete(text: Text)
}