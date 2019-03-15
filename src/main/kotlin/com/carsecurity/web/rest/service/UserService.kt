package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.User

interface UserService {
    fun getUser(username: String): User
    fun updateUserEmail(userId: Long, email: String)
    fun removeUser(userId: Long)
}