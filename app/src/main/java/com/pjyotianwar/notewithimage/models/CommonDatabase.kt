package com.pjyotianwar.notewithimage.models

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pjyotianwar.notewithimage.daos.NoteDao
import com.pjyotianwar.notewithimage.daos.UserDao

@Database(entities = [Notes::class, Users::class], version = 1, exportSchema = false)
abstract class CommonDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun userDao(): UserDao
}