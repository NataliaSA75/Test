package com.example.test.database
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    // Сохраняем пользователя
    fun saveUser(login: String, password: String) {
        viewModelScope.launch {
            val user = UsersEnt(login = login, password = password)
            userDao.insertUser(user)
        }
    }

    // Получаем логин пользователя
    suspend fun getUserLogin(): String? {
        return userDao.getUserLogin()
    }
}

class UserViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}