package com.example.test.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usersEnt")
data class UsersEnt (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val login: String,
    val password: String
    )