package com.fairyub.composetutorial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyub.composetutorial.data.User
import com.fairyub.composetutorial.data.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao,
) : ViewModel() {
    private val _users = MutableStateFlow<List<User?>>(arrayListOf())
    val users get() = _users.asStateFlow()

    fun userFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            _users.value = userDao.getAll()
        }
    }

    fun addUserToDB(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertAll(user)
            userFromDB()
        }
    }
}