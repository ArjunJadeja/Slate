package com.arjun.slate.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "text_table")
class Text(
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "posting_time") val postingTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}