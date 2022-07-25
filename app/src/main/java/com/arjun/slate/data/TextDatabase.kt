package com.arjun.slate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Text::class], version = 1, exportSchema = false)
abstract class TextDatabase : RoomDatabase() {

    abstract fun getTextDao(): TextDao

    companion object {
        @Volatile
        private var INSTANCE: TextDatabase? = null

        fun getDatabase(context: Context): TextDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TextDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}