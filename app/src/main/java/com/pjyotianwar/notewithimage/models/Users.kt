package com.pjyotianwar.notewithimage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class Users(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "user_mobile")
    val userMobile: String,

    @ColumnInfo(name = "user_email")
    val userEmail: String,

    @ColumnInfo(name = "user_password")
    val userPassword: String
)
