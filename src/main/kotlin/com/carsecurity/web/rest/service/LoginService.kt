package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Token
import com.carsecurity.web.rest.model.User

interface LoginService {

    fun login(username: String, password: String): Token
    fun refresh(refreshToken: String): Token
    fun register(username: String, password: String): Int
}