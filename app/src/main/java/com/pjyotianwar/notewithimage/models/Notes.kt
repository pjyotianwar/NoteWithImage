package com.pjyotianwar.notewithimage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Notes (
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0,

    @ColumnInfo(name = "note_title")
    val noteTitle: String,

    @ColumnInfo(name = "note_description")
    val noteDescription: String,

    @ColumnInfo(name = "note_images")
    val noteImages: String,

    @ColumnInfo(name = "user")
    val user: String
)
