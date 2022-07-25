package com.arjun.slate.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TextDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(text: Text)

    @Query("SELECT * FROM text_table ORDER BY ID DESC")
    fun getAllTexts(): LiveData<List<Text>>

}