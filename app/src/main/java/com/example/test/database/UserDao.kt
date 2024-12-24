package com.example.test.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT login FROM users LIMIT 1")
    suspend fun getUserLogin(): String?
}