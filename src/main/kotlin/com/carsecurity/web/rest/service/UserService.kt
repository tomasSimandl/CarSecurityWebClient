package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Token

interface UserService {

    fun login(username: String, password: String): Token
    fun refresh(refreshToken: String): Token
}