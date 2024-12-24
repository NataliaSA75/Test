package com.example.test.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UsersEnt)

    @Query("SELECT login FROM usersEnt ORDER BY id DESC LIMIT 1")
    suspend fun getUserLogin(): String?
}